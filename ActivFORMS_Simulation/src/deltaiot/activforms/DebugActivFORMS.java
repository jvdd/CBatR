package deltaiot.activforms;

import java.util.HashMap;

import activforms.adaptation.ActiveModelProbeInterface;
import activforms.engine.LocationList;

public class DebugActivFORMS implements ActiveModelProbeInterface{

	@Override
	public void executionStarted(HashMap<Integer, String> instances, LocationList allLocs,
			HashMap<Integer, HashMap<String, Object>> data) {
		System.out.println(instances);
	}

	@Override
	public void dataUpdated(int templateId, HashMap<String, Object> data) {
		//System.out.println(data);
	}

	@Override
	public void locationUpdated(int templateId, String locationId, LocationList currentLocs) {
		System.out.println(currentLocs);
	}

	@Override
	public void modelChanged(String model, HashMap<Integer, String> instances, LocationList initialLocs) {
		
	}

	@Override
	public void executionEnded() {
		
	}
	
}
