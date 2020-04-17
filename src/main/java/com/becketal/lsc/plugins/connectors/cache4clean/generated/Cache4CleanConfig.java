//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.04.17 um 10:25:26 AM CEST 
//


package com.becketal.lsc.plugins.connectors.cache4clean.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dryRun" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dataSource" type="{http://lsc-project.org/XSD/lsc-cache4clean-plugin-1.0.xsd}sourceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dryRun",
    "dataSource"
})
@XmlRootElement(name = "Cache4CleanConfig")
public class Cache4CleanConfig {

    protected boolean dryRun;
    @XmlElement(required = true)
    protected SourceType dataSource;

    /**
     * Ruft den Wert der dryRun-Eigenschaft ab.
     * 
     */
    public boolean isDryRun() {
        return dryRun;
    }

    /**
     * Legt den Wert der dryRun-Eigenschaft fest.
     * 
     */
    public void setDryRun(boolean value) {
        this.dryRun = value;
    }

    /**
     * Ruft den Wert der dataSource-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SourceType }
     *     
     */
    public SourceType getDataSource() {
        return dataSource;
    }

    /**
     * Legt den Wert der dataSource-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceType }
     *     
     */
    public void setDataSource(SourceType value) {
        this.dataSource = value;
    }

}
