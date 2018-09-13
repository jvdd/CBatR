package deltaiot.types;

import java.util.HashMap;

import activforms.types.Int;

public class MoteTraffic {
	private int moteId;
	private int traffic;
	
	public MoteTraffic(){}
	
	public MoteTraffic(int moteId, int traffic) {
		this.moteId = moteId;
		this.traffic = traffic;
	}
	public int getMoteId() {
		return moteId;
	}
	public void setMoteId(int moteId) {
		this.moteId = moteId;
	}
	public int getTraffic() {
		return traffic;
	}
	public void setTraffic(int traffic) {
		this.traffic = traffic;
	}
	
	public String toModelString(){
		return String.format("{%d, %d}", moteId, traffic);
	}
	
	public static MoteTraffic parseMoteTraffic(HashMap<String, Int> model){
		MoteTraffic traffic = new MoteTraffic();
		
		traffic.moteId = model.get("moteId").getValue();
		traffic.traffic = model.get("traffic").getValue();
		
		return traffic;
	}
}
