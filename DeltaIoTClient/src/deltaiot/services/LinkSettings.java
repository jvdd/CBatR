
package deltaiot.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for linkSettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="linkSettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dest" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="distributionFactor" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="powerSettings" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="spreadingFactor" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="src" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "linkSettings", propOrder = {
    "dest",
    "distributionFactor",
    "powerSettings",
    "spreadingFactor",
    "src"
})
public class LinkSettings {

    protected int dest;
    protected int distributionFactor = -1;
    protected int powerSettings = -4;
    protected int spreadingFactor = 0;
    protected int src;

    /**
     * Gets the value of the dest property.
     * 
     */
    public int getDest() {
        return dest;
    }

    /**
     * Sets the value of the dest property.
     * 
     */
    public void setDest(int value) {
        this.dest = value;
    }

    /**
     * Gets the value of the distributionFactor property.
     * 
     */
    public int getDistributionFactor() {
        return distributionFactor;
    }

    /**
     * Sets the value of the distributionFactor property.
     * 
     */
    public void setDistributionFactor(int value) {
        this.distributionFactor = value;
    }

    /**
     * Gets the value of the powerSettings property.
     * 
     */
    public int getPowerSettings() {
        return powerSettings;
    }

    /**
     * Sets the value of the powerSettings property.
     * 
     */
    public void setPowerSettings(int value) {
        this.powerSettings = value;
    }

    /**
     * Gets the value of the spreadingFactor property.
     * 
     */
    public int getSpreadingFactor() {
        return spreadingFactor;
    }

    /**
     * Sets the value of the spreadingFactor property.
     * 
     */
    public void setSpreadingFactor(int value) {
        this.spreadingFactor = value;
    }

    /**
     * Gets the value of the src property.
     * 
     */
    public int getSrc() {
        return src;
    }

    /**
     * Sets the value of the src property.
     * 
     */
    public void setSrc(int value) {
        this.src = value;
    }

}
