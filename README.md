# Foreign Key LSC plug-in

This project implements a plugin for the [LDAP Synchronization Connector](https://lsc-project.org/).

## Goal

The intention of this plug-in is to enrich an existing data source (A) with some values from a different data source (B).

Usually this is done by iterating the complete data source (B) and adding values to matching entries in data source (A) only allowing updates. 
This process is fine as long as the number of possible matches are above 90%. When the number of possible matches are below this mark or you want to make as least as possible requests to the data source (B), you can uses this plug-in.

This plug-in allows you to configure 2 data sources. The first one, called the key source is used to retrieve the list of users to synchronize and the 2nd one, called the data source is used to retrieve the details which are then used to update the destination data source.

This process works only when all 3 data sources use the same pivot attribute. 

## Configuration

To use a plug-in, you need to place the provided 'lsc-foreign-key-plugin-1.0.xsd' into your lsc etc diretory. 

In the lsc.xml xml header, add the reference to the xsd:

```
<lsc xmlns="http://lsc-project.org/XSD/lsc-core-2.1.xsd"
	xmlns:foreignkey="http://lsc-project.org/XSD/lsc-foreign-key-plugin-1.0.xsd"
	revision="0">
```

You can now use the `pluginSourceService`

The configuration allows you to configure 2 source services. 
The allowed services are identical with the one from LSC v2.1. You need to prefix the source service with `foreignkey:` so it is recognized in the configuration. 
The configuration options are the one defined by the service and are not prefixed as their definition comes from the lsc-core-2.1.xsd.

In this example, you see that a database is enriched with values from the Microsoft Graph API. To uses this plug-in, a fork was created to allow the usage of the userPrincipalName as pivot attribute. 

```
			<pluginSourceService
				implementationClass="com.becketal.lsc.plugins.connectors.foreignkeysrc.ForeignKeySrcService">
				<name>TestTask1-src</name>
				<connection reference="jdbcCSV" />
				<foreignkey:ForeignKeySrcServiceConfig>
					<foreignkey:keySource>
						<foreignkey:databaseSourceService>
							<name>TestTask1-src1</name>
							<connection reference="jdbcDest"></connection>
							<requestNameForList>getUserDstList</requestNameForList>
						</foreignkey:databaseSourceService>
					</foreignkey:keySource>
					<foreignkey:dataSource>
						<foreignkey:pluginSourceService
							implementationClass="org.lsc.plugins.connectors.msgraphapi.MsGraphApiUsersSrcService">
							<name>msgraphapisrc</name>
							<connection reference="msgraphapi" />
							<msgraphapi:msGraphApiUsersService>
								<name>msgraphapi-users-service-src</name>
								<connection reference="msgraphapi" />
								<msgraphapi:pivot>userPrincipalName</msgraphapi:pivot>
								<msgraphapi:pivotInternal>userPrincipalName</msgraphapi:pivotInternal>
								<msgraphapi:select>id,userPrincipalName,displayName,skills,schools,aboutMe,birthday,interests,pastProjects,preferredName,responsibilities</msgraphapi:select>
							</msgraphapi:msGraphApiUsersService>
						</foreignkey:pluginSourceService>
					</foreignkey:dataSource>
				</foreignkey:ForeignKeySrcServiceConfig>
			</pluginSourceService>
```


## Usage

Use this plug-in to enrich a data set with values from a different data source.

Run lsc by specifying the plugin packagepath option: `-DLSC.PLUGINS.PACKAGEPATH=com.becketal.lsc.plugins.connectors.foreignkeysrc.generated`  
In case you want to use multiple plug-ins, as the example above. Use a colon (:) and make sure you use the latest patched version in case you are using Windows. 

## Open Topics

1. Currently tested only with a databaseSourceService and the modified msGraphApiUsersService. Open are the ldapSourceService and possible combinations.
2. Plug-in does not implement the asynchronous interface as not all possible source services allow this. 
3. Currently no test code exists.

