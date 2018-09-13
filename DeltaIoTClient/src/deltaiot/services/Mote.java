
package deltaiot.services;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mote complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mote">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="battery" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="currentQSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="dataProbability" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="links" type="{http://services.deltaiot/}link" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="load" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="moteid" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="parents" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="queueLoss" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="queueSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mote", propOrder = {
    "battery",
    "currentQSize",
    "dataProbability",
    "links",
    "load",
    "moteid",
    "parents",
    "queueLoss",
    "queueSize"
})
public class Mote {

    protected double battery;
    protected int currentQSize;
    protected int dataProbability;
    @XmlElement(nillable = true)
    protected List<Link> links;
    protected int load;
    protected Integer moteid;
    protected int parents;
    protected int queueLoss;
    protected int queueSize;

    /**
     * Gets the value of the battery property.
     * 
     */
    public double getBattery() {
        return battery;
    }

    /**
     * Sets the value of the battery property.
     * 
     */
    public void setBattery(double value) {
        this.battery = value;
    }

    /**
     * Gets the value of the currentQSize property.
     * 
     */
    public int getCurrentQSize() {
        return currentQSize;
    }

    /**
     * Sets the value of the currentQSize property.
     * 
     */
    public void setCurrentQSize(int value) {
        this.currentQSize = value;
    }

    /**
     * Gets the value of the dataProbability property.
     * 
     */
    public int getDataProbability() {
        return dataProbability;
    }

    /**
     * Sets the value of the dataProbability property.
     * 
     */
    public void setDataProbability(int value) {
        this.dataProbability = value;
    }

    /**
     * Gets the value of the links property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the links property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinks().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Link }
     * 
     * 
     */
    public List<Link> getLinks() {
        if (links == null) {
            links = new ArrayList<Link>();
        }
        return this.links;
    }

    /**
     * Gets the value of the load property.
     * 
     */
    public int getLoad() {
        return load;
    }

    /**
     * Sets the value of the load property.
     * 
     */
    public void setLoad(int value) {
        this.load = value;
    }

    /**
     * Gets the value of the moteid property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMoteid() {
        return moteid;
    }

    /**
     * Sets the value of the moteid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMoteid(Integer value) {
        this.moteid = value;
    }

    /**
     * Gets the value of the parents property.
     * 
     */
    public int getParents() {
        return parents;
    }

    /**
     * Sets the value of the parents property.
     * 
     */
    public void setParents(int value) {
        this.parents = value;
    }

    /**
     * Gets the value of the queueLoss property.
     * 
     */
    public int getQueueLoss() {
        return queueLoss;
    }

    /**
     * Sets the value of the queueLoss property.
     * 
     */
    public void setQueueLoss(int value) {
        this.queueLoss = value;
    }

    /**
     * Gets the value of the queueSize property.
     * 
     */
    public int getQueueSize() {
        return queueSize;
    }

    /**
     * Sets the value of the queueSize property.
     * 
     */
    public void setQueueSize(int value) {
        this.queueSize = value;
    }

}
