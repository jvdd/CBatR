package mapek;

import java.util.LinkedList;
import java.util.List;

public class Mote {
	int moteId;
	double energyLevel;
	
	List<Link> links = new LinkedList<>();
	
	public int getMoteId() {
		return moteId;
	}
	
	public List<Link> getLinks() {
		return links;
	}
	
	public Link getLink(int index) {
		return links.get(index);
	}
	
	public double getEnergyLevel(){
		return energyLevel;
	}
	
	public Mote getCopy(){
		Mote mote = new Mote();
		mote.moteId = this.moteId;
		mote.energyLevel = this.energyLevel;
		
		for(Link link: links){
			mote.links.add(link.getCopy());
		}
		return mote;
	}
}
