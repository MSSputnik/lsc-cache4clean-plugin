//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.04.17 um 10:25:26 AM CEST 
//


package com.becketal.lsc.plugins.connectors.cache4clean.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.lsc.configuration.AsyncLdapSourceServiceType;
import org.lsc.configuration.DatabaseSourceServiceType;
import org.lsc.configuration.GoogleAppsServiceType;
import org.lsc.configuration.LdapSourceServiceType;
import org.lsc.configuration.PluginSourceServiceType;


/**
 * <p>Java-Klasse für sourceType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="sourceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="databaseSourceService" type="{http://lsc-project.org/XSD/lsc-core-2.1.xsd}databaseSourceServiceType"/>
 *         &lt;element name="googleAppsSourceService" type="{http://lsc-project.org/XSD/lsc-core-2.1.xsd}googleAppsServiceType"/>
 *         &lt;element name="ldapSourceService" type="{http://lsc-project.org/XSD/lsc-core-2.1.xsd}ldapSourceServiceType"/>
 *         &lt;element name="asyncLdapSourceService" type="{http://lsc-project.org/XSD/lsc-core-2.1.xsd}asyncLdapSourceServiceType"/>
 *         &lt;element name="pluginSourceService" type="{http://lsc-project.org/XSD/lsc-core-2.1.xsd}pluginSourceServiceType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sourceType", propOrder = {
    "databaseSourceService",
    "googleAppsSourceService",
    "ldapSourceService",
    "asyncLdapSourceService",
    "pluginSourceService"
})
public class SourceType {

    protected DatabaseSourceServiceType databaseSourceService;
    protected GoogleAppsServiceType googleAppsSourceService;
    protected LdapSourceServiceType ldapSourceService;
    protected AsyncLdapSourceServiceType asyncLdapSourceService;
    protected PluginSourceServiceType pluginSourceService;

    /**
     * Ruft den Wert der databaseSourceService-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DatabaseSourceServiceType }
     *     
     */
    public DatabaseSourceServiceType getDatabaseSourceService() {
        return databaseSourceService;
    }

    /**
     * Legt den Wert der databaseSourceService-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DatabaseSourceServiceType }
     *     
     */
    public void setDatabaseSourceService(DatabaseSourceServiceType value) {
        this.databaseSourceService = value;
    }

    /**
     * Ruft den Wert der googleAppsSourceService-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GoogleAppsServiceType }
     *     
     */
    public GoogleAppsServiceType getGoogleAppsSourceService() {
        return googleAppsSourceService;
    }

    /**
     * Legt den Wert der googleAppsSourceService-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GoogleAppsServiceType }
     *     
     */
    public void setGoogleAppsSourceService(GoogleAppsServiceType value) {
        this.googleAppsSourceService = value;
    }

    /**
     * Ruft den Wert der ldapSourceService-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LdapSourceServiceType }
     *     
     */
    public LdapSourceServiceType getLdapSourceService() {
        return ldapSourceService;
    }

    /**
     * Legt den Wert der ldapSourceService-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LdapSourceServiceType }
     *     
     */
    public void setLdapSourceService(LdapSourceServiceType value) {
        this.ldapSourceService = value;
    }

    /**
     * Ruft den Wert der asyncLdapSourceService-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AsyncLdapSourceServiceType }
     *     
     */
    public AsyncLdapSourceServiceType getAsyncLdapSourceService() {
        return asyncLdapSourceService;
    }

    /**
     * Legt den Wert der asyncLdapSourceService-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AsyncLdapSourceServiceType }
     *     
     */
    public void setAsyncLdapSourceService(AsyncLdapSourceServiceType value) {
        this.asyncLdapSourceService = value;
    }

    /**
     * Ruft den Wert der pluginSourceService-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PluginSourceServiceType }
     *     
     */
    public PluginSourceServiceType getPluginSourceService() {
        return pluginSourceService;
    }

    /**
     * Legt den Wert der pluginSourceService-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PluginSourceServiceType }
     *     
     */
    public void setPluginSourceService(PluginSourceServiceType value) {
        this.pluginSourceService = value;
    }

}
