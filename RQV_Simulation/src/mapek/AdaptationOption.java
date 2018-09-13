package mapek;

import com.google.gson.Gson;

public class AdaptationOption{
	public ManagedSystem system;
	public Qualities verificResults = new Qualities();
	
	protected AdaptationOption getCopy() {
		AdaptationOption newOption = new AdaptationOption();
		newOption.system = system.getCopy();
		newOption.verificResults = verificResults.getCopy();
		return newOption;
	}
	
	@Override
	public String toString() {
		Gson gsn = new Gson();
		return gsn.toJson(this);
	}
}
