package deltaiot.types;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import activforms.types.Int;

public class Mote {
	
	private int moteId;
	private int load;
	private int energyLevel;
	private int parents;
	private int queueSize;
	private List<Link> links = new LinkedList<Link>();
	
	public Mote(){}
	
	public Mote(int moteId, int load, int energyLevel, int parents, int queueSize) {
		this.moteId = moteId;
		this.load = load;
		this.energyLevel = energyLevel;
		this.parents = parents;
		this.queueSize = queueSize;
	}	
	
	public Mote(int moteId, int load, int energyLevel, int parents, int queueSize, Link link) {
		this(moteId, load, energyLevel, parents, queueSize);
		this.links.add(link);
	}
	
	// TODO: code underneath is added
	public Mote(int moteId, int load, int energyLevel, int parents, int queueSize, List<Link> links) {
		this(moteId, load, energyLevel, parents, queueSize);
		this.links = links;
	}
	// TODO: code above is added
	
	public int getMoteId() {
		return moteId;
	}
	public void setMoteId(int moteId) {
		this.moteId = moteId;
	}
	public int getLoad() {
		return load;
	}
	public void setLoad(int load) {
		this.load = load;
	}
	public int getEnergyLevel() {
		return energyLevel;
	}
	public void setEnergyLevel(int energyLevel) {
		this.energyLevel = energyLevel;
	}
	public int getParents() {
		return parents;
	}

	public void setParents(int parents) {
		this.parents = parents;
	}
	
	public List<Link> getLinks() {
		return links;
	}
	
	public static Mote parseMote(HashMap<String, Int> moteModel){
		Mote mote = new Mote();
		mote.moteId = moteModel.get("moteId").getValue();
		mote.load = (int)moteModel.get("load").getValue();
		mote.energyLevel = (int)moteModel.get("energyLevel").getValue();
		mote.parents = (int)moteModel.get("parents").getValue();
		mote.queueSize = (int)moteModel.get("queueSize").getValue();
		HashMap<Integer, HashMap> links = (HashMap)(Object)moteModel.get("links");
		for(Entry<Integer, HashMap> link: links.entrySet()){
			Link l = Link.parseLink((HashMap)link.getValue());
			mote.getLinks().add(l);
		}
		return mote;
	}
	
	public String toModelString(){
		return String.format("{%d, %d, %d, %d, %d,{%s, %s}}", moteId, load, energyLevel, getParents(), queueSize, getLinkString(0), getLinkString(1));
	}
	
	private String getLinkString(int linkIndex){
		if (linkIndex < parents)
			return links.get(linkIndex).toModelString();
		return "{0,0,0,0}";
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}
}
