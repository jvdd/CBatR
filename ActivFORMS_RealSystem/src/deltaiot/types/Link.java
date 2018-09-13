package deltaiot.types;

import java.util.HashMap;

import activforms.types.Int;

public class Link {
    private int source;
    private int destination;
    private int powerSetting;
    private int distribution;
    
    public Link(){}
    
	public Link(int source, int destination, int powerSetting, int distribution) {
		this.source = source;
		this.destination = destination;
		this.powerSetting = powerSetting;
		this.distribution = distribution;
	}

	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getDestination() {
		return destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	public int getPowerSetting() {
		return powerSetting;
	}
	public void setPowerSetting(int powerSetting) {
		this.powerSetting = powerSetting;
	}
	public int getDistribution() {
		return distribution;
	}
	public void setDistribution(int distribution) {
		this.distribution = distribution;
	}
    
    public String toModelString(){
    	return String.format("{%d, %d, %d, %d}", source, destination, powerSetting, distribution);
    }
    
    public static Link parseLink(HashMap<String, Int> linkModel){
    	Link link = new Link();
    	link.source = linkModel.get("source").getValue();
    	link.destination = linkModel.get("destination").getValue();
    	link.powerSetting = linkModel.get("powerSetting").getValue();
    	link.distribution = linkModel.get("distribution").getValue();
    	return link;
    }
}
