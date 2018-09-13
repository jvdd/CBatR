package domain;

import java.util.List;

import simulator.FileHandler;

public class FileProfile implements Profile<Double> {

	private List<Double> values;
	private Double defaultValue;
	
	public FileProfile(String relPath, Double defaultValue) {
		this.values = FileHandler.parseNumberList(relPath);
		this.defaultValue = defaultValue;
	}
	
	@Override
	public Double get(int runNumber) {
		if (runNumber >= 0 && runNumber < values.size()) {
			double val = values.get(runNumber);
			return val;
		}
		else {
			System.out.println("Unknown value data for run " + runNumber + " returning default (" + defaultValue + ").");
			return defaultValue;
		}

	}

}
