package deltaiot.cbam;

import deltaiot.types.Link;

public class Adaptation {
	
	/** 
	 * Class for a (possible) adaptation necessary for using the new 
	 * adaptation option. Each adaptation is specific for a link.
	 * Until now there are 2 types of adaptations:
	 *    - Power setting is changed 	
	 *    - Distribution factor is changed 
	 */

	private Link link;
	private int newValue;
	private AdaptationOptionType adaptationOption;
	
	public static enum AdaptationOptionType { DISTRIBUTIONCHANGE, POWERCHANGE };
	
	public Adaptation(Link link, int newValue, AdaptationOptionType adaptationOption) {
		this.link = link;
		this.newValue = newValue;
		this.adaptationOption = adaptationOption;
	}
	
	public Link getLink() {
		return link;
	}
	
	public int getNewValue() {
		return newValue;
	}
	
	public AdaptationOptionType getAdaptationOptionType() {
		return adaptationOption;
	}
	
	public String getAdaptationOptionString() {
		if (adaptationOption == AdaptationOptionType.POWERCHANGE) {
			return "changePower";
		} 
		else if (adaptationOption == AdaptationOptionType.DISTRIBUTIONCHANGE) {
			return "changeDistribution";
		}
		else {
			return "ERROR: adaptation option not initialized";
		}
	}
}
