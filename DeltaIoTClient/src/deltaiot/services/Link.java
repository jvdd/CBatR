
package deltaiot.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for link complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="link">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dest" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="distribution" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="latency" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="packetLoss" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="power" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SF" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SNR" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "link", propOrder = {
    "dest",
    "distribution",
    "latency",
    "packetLoss",
    "power",
    "sf",
    "snr",
    "source"
})
public class Link {

    protected Integer dest;
    protected int distribution;
    protected double latency;
    protected int packetLoss;
    protected int power;
    @XmlElement(name = "SF")
    protected int sf;
    @XmlElement(name = "SNR")
    protected Double snr;
    protected Integer source;

    /**
     * Gets the value of the dest property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDest() {
        return dest;
    }

    /**
     * Sets the value of the dest property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDest(Integer value) {
        this.dest = value;
    }

    /**
     * Gets the value of the distribution property.
     * 
     */
    public int getDistribution() {
        return distribution;
    }

    /**
     * Sets the value of the distribution property.
     * 
     */
    public void setDistribution(int value) {
        this.distribution = value;
    }

    /**
     * Gets the value of the latency property.
     * 
     */
    public double getLatency() {
        return latency;
    }

    /**
     * Sets the value of the latency property.
     * 
     */
    public void setLatency(double value) {
        this.latency = value;
    }

    /**
     * Gets the value of the packetLoss property.
     * 
     */
    public int getPacketLoss() {
        return packetLoss;
    }

    /**
     * Sets the value of the packetLoss property.
     * 
     */
    public void setPacketLoss(int value) {
        this.packetLoss = value;
    }

    /**
     * Gets the value of the power property.
     * 
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the value of the power property.
     * 
     */
    public void setPower(int value) {
        this.power = value;
    }

    /**
     * Gets the value of the sf property.
     * 
     */
    public int getSF() {
        return sf;
    }

    /**
     * Sets the value of the sf property.
     * 
     */
    public void setSF(int value) {
        this.sf = value;
    }

    /**
     * Gets the value of the snr property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSNR() {
        return snr;
    }

    /**
     * Sets the value of the snr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSNR(Double value) {
        this.snr = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSource(Integer value) {
        this.source = value;
    }

}
