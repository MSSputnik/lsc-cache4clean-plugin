package com.becketal.lsc.plugins.connectors.cache4clean;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lsc.LscDatasets;
import org.lsc.beans.IBean;
import org.lsc.beans.SimpleBean;
import org.lsc.configuration.LscConfiguration;
import org.lsc.configuration.PluginSourceServiceType;
import org.lsc.configuration.ServiceType;
import org.lsc.configuration.TaskType;
import org.lsc.configuration.TaskType.AuditLog;
import org.lsc.exception.LscServiceConfigurationException;
import org.lsc.exception.LscServiceException;
import org.lsc.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.becketal.lsc.plugins.connectors.cache4clean.generated.Cache4CleanConfig;
import com.becketal.lsc.plugins.connectors.cache4clean.generated.SourceType;

public class Cache4CleanSrcService implements IService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(Cache4CleanSrcService.class);

	protected IService dataSourceService;
	protected ServiceType dataService;
	protected TaskType dataTaskType;

	protected boolean dryRun = true;
	protected List<Map<String, Object>> cache = null;
	protected boolean cacheListFilled = false;

	private long cacheCopyTime = 0L;
	private long cacheCheckTime = 0L;
	private long realCheckTime = 0L;
	private long cacheDifferences = 0L;

	public Cache4CleanSrcService(final TaskType task) throws LscServiceConfigurationException {
		// get the plug-in configuration
		// try to get my configuration. Should be of type databaseConnection
		if (task != null) {
			try {
				PluginSourceServiceType pluginSourceServiceType = task.getPluginSourceService();
				if (pluginSourceServiceType != null) {
					// find our configuration
					Cache4CleanConfig config = null;
					for (Object o : task.getPluginSourceService().getAny()) {
						LOGGER.debug("getAny Object Type is : " + o.getClass().getName());
						if (o instanceof Cache4CleanConfig) {
							config = (Cache4CleanConfig)o;
							break;
						}
					}
					if (config != null) {

						// Continue to read the configuration from the pluginSourceServiceType
						LOGGER.debug("configType is " + pluginSourceServiceType.getClass().getName());
						LOGGER.debug("Read config from " + pluginSourceServiceType.getName());

						// dryRun
						dryRun = config.isDryRun();

						// Initialize data source
						SourceType dataSource = config.getDataSource();
						dataService = getSourceService(dataSource);
						if (dataService == null)
							throw new LscServiceConfigurationException("Missing source data service for plugin=" + pluginSourceServiceType.getName());
						dataTaskType = createFakeTaskType(task, dataSource);

						// Instantiate data source service and pass parameters
						Constructor<?> constrDataSrcService = LscConfiguration.getServiceImplementation(dataService).getConstructor(new Class[]{TaskType.class});
						dataSourceService = (IService) constrDataSrcService.newInstance(new Object[]{dataTaskType});
						LOGGER.debug("dataSourceService is " + dataSourceService.getClass().getName());

						cache = new ArrayList<Map<String,Object>>();

					} else {
						LOGGER.debug("Cache4CleanConfig not found");
						throw new LscServiceConfigurationException("Unable to identify the Cache4Clean service configuration " + "inside the plugin source node of the task: " + task.getName());
					}
				} else {
					LOGGER.debug("pluginSourceServiceType not found");
					throw new LscServiceConfigurationException("Unable to identify the pluginSourceServiceType service configuration " + "inside the plugin source node of the task: " + task.getName());
				}
			} catch (Exception e) {
				LOGGER.error("Exception during Cache4Clean initialisation");
				if (LOGGER.isDebugEnabled())
					e.printStackTrace();
				throw new LscServiceConfigurationException(e.getCause().getMessage());
			}
		} else {
			LOGGER.debug("task object is null");
			throw new LscServiceConfigurationException("task object is null");
		}
	}

	public IBean getBean(String pivotName, LscDatasets pivotAttributes, boolean fromSameService)
			throws LscServiceException {
		IBean dataIBean = null;
		long startTime;
		boolean inCache = false;
		if (fromSameService) {
			// No special handling of sync process
			return  dataSourceService.getBean(pivotName, pivotAttributes, fromSameService);
		} else {
			// fromSameService is false, so it is the clean task.
			// only the keySourceService is used.
			LOGGER.debug("Chache clean search for pivotName: " + pivotName + " - pivotAttributes: " + pivotAttributes.toString());
			if (cacheListFilled) {
				//LOGGER.debug("cache exists.");
				if (dryRun) {
					// Dry Run. Use both for clean.
					LOGGER.info("CACHE check for " + pivotName);
					startTime = System.nanoTime();
					if (cache.contains(pivotAttributes.getDatasets())) {
						inCache = true;
					}
					cacheCheckTime += (System.nanoTime() - startTime);

					// use normal process for clean
					startTime = System.nanoTime();
					dataIBean = dataSourceService.getBean(pivotName, pivotAttributes, fromSameService);
					realCheckTime += (System.nanoTime() - startTime);

					if (inCache == (dataIBean != null)) {
						LOGGER.info("CACHE and REAL result are consistent");
					} else {
						cacheDifferences += 1;
						LOGGER.warn("CACHE and REAL result differ");
					}
					LOGGER.info("CACHE => Aggregated cache status: difference count: " + String.valueOf(cacheDifferences) + " - cache check time: " + Duration.ofNanos(cacheCheckTime).toString() + " - real check time: " + Duration.ofNanos(realCheckTime).toString());
				} else {
					// Use cache for clean.
					if (cache.contains(pivotAttributes.getDatasets())) {
						LOGGER.debug("Cache: keep");
						dataIBean = new SimpleBean(); // just to return anything. The return value does not matter.
					} else {
						LOGGER.debug("Cache: delete");
					}
				}
			} else {
				LOGGER.debug("no cache exists. Running normal clean process.");
				dataIBean = dataSourceService.getBean(pivotName, pivotAttributes, fromSameService);
			}
		}
		return dataIBean;
	}

	public Map<String, LscDatasets> getListPivots() throws LscServiceException {
		LOGGER.debug("getListPivots is called");

		Map<String, LscDatasets> result =  dataSourceService.getListPivots();
		// unknown if the LscDataset is modified. Current assumption is that it is not modified.
		if (result != null) {
			long startTime = System.nanoTime();
			copy2Cache(result);
			cacheCopyTime = System.nanoTime() - startTime;
		}
		if (dryRun) {
			LOGGER.info("Cache Copy Time: " + Duration.ofNanos(cacheCopyTime).toString());
		}
		LOGGER.debug("Resulting list: " + result.toString());
		return result;
	}


	private void copy2Cache(Map<String, LscDatasets> sourceList) {
		if (sourceList != null) {
			if (cache != null) {
				for (Map.Entry<String, LscDatasets> entry : sourceList.entrySet()) {
					cache.add(entry.getValue().getDatasets());
				}
				cacheListFilled = true;
			} else {
				LOGGER.error("copy2Cache:cache == null");
			}
		} else {
			LOGGER.debug("copy2Cache:sourceList == null");
		}
	}

	// copy from org.lsc.configuration.LscConfiguration
	public static ServiceType getSourceService(SourceType t) {
		if(t.getAsyncLdapSourceService() != null) {
			return t.getAsyncLdapSourceService();
		} else if (t.getLdapSourceService() != null) {
			return t.getLdapSourceService();
		} else if (t.getGoogleAppsSourceService() != null) {
			return t.getGoogleAppsSourceService();
		} else if (t.getDatabaseSourceService() != null) {
			return t.getDatabaseSourceService();
		} else if (t.getPluginSourceService() != null) {
			return t.getPluginSourceService();
		}
		return null;
	}

	/**
	 * Create a fake TaskType by copying everything from the TaskType given to the plug in but replace the 
	 * Data souceTypes with the specific sourceTypes
	 * @param sourceTask
	 * @param source
	 * @return
	 */
	public TaskType createFakeTaskType(TaskType sourceTask, SourceType source) {
		TaskType result = new TaskType();
		result.setName(sourceTask.getName());
		result.setBean(sourceTask.getBean());
		result.setCleanHook(sourceTask.getCleanHook());
		result.setSyncHook(sourceTask.getSyncHook());
		result.setDatabaseDestinationService(sourceTask.getDatabaseDestinationService());
		result.setGoogleAppsDestinationService(sourceTask.getGoogleAppsDestinationService());
		result.setLdapDestinationService(sourceTask.getLdapDestinationService());
		result.setMultiDestinationService(sourceTask.getMultiDestinationService());
		result.setXaFileDestinationService(sourceTask.getXaFileDestinationService());
		result.setPluginDestinationService(sourceTask.getPluginDestinationService());
		result.setPropertiesBasedSyncOptions(sourceTask.getPropertiesBasedSyncOptions());
		result.setForceSyncOptions(sourceTask.getForceSyncOptions());
		result.setPluginSyncOptions(sourceTask.getPluginSyncOptions());
		result.setCustomLibrary(sourceTask.getCustomLibrary());
		result.setScriptInclude(sourceTask.getScriptInclude());
		result.setId(sourceTask.getId());
		// copy auditlog List
		List<AuditLog> srcList = sourceTask.getAuditLog();
		List<AuditLog> destList = result.getAuditLog();
		// destList is always empty as it is a new instance.
		if (destList != null && srcList != null)
			destList.addAll(srcList);

		// Source Type specific settings
		result.setDatabaseSourceService(source.getDatabaseSourceService());
		result.setGoogleAppsSourceService(source.getGoogleAppsSourceService());
		result.setLdapSourceService(source.getLdapSourceService());
		result.setAsyncLdapSourceService(source.getAsyncLdapSourceService());
		result.setPluginSourceService(source.getPluginSourceService());

		return result;
	}

}
