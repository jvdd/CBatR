package domain;

import java.util.HashMap;
import java.util.Map;

public class DoubleRange implements Profile<Double> {

	private double min;
	private double max;
	private Map<Integer, Double> memory = new HashMap<>();
	
	public DoubleRange(Double min, Double max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public Double get(int runNumber) {
		if (memory.containsKey(runNumber)) {
			return memory.get(runNumber);
		}
		else {
			double random = min + Math.random() * (max - min);
			memory.put(runNumber, random);
			return random;
		}
	}
	
	

}
