package mapek;

public class TrafficProbability {
	int moteId;
	double load;
	
	public TrafficProbability(int moteId, double load) {
		this.moteId = moteId;
		this.load = load;
	}

	public TrafficProbability getCopy() {
		return new TrafficProbability(moteId, load);
	}
}
