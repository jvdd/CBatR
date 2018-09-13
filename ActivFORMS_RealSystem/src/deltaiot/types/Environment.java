package deltaiot.types;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Environment {
	
	private List<LinkSNR>linksSNR = new LinkedList<LinkSNR>();
	private List<MoteTraffic> motesTraffic = new LinkedList<MoteTraffic>();
	
	public List<LinkSNR> getLinksSNR() {
		return linksSNR;
	}

	public List<MoteTraffic> getMotesTraffic() {
		return motesTraffic;
	}

	public static Environment parseEnvironment(HashMap envModel) {
		Environment environment = new Environment();
		LinkSNR snr;
		HashMap<Integer, HashMap> linksSNR = (HashMap<Integer, HashMap>) envModel.get("linksSNR");
		for(Entry model: linksSNR.entrySet()){
			snr = LinkSNR.parseLinkSNR((HashMap)model.getValue());
			environment.linksSNR.add(snr);
		}
		MoteTraffic traffic;
		HashMap<Integer, HashMap> motesTraffic = (HashMap<Integer, HashMap>) envModel.get("motesTraffic");
		for(Entry model: motesTraffic.entrySet()){
			traffic = MoteTraffic.parseMoteTraffic((HashMap)model.getValue());
			environment.motesTraffic.add(traffic);
		}
		
		return environment;
	}
	
	public String toModelString(){
		StringBuilder str = new StringBuilder();
		str.append("Environment environment = {\n");
		
		str.append("{");
		for(LinkSNR snr: linksSNR){
			str.append(snr.toModelString());
			str.append(",");
		}
		str.setLength(str.length()-1);
		str.append("},\n{");
		
		for(MoteTraffic traffic: motesTraffic){
			str.append(traffic.toModelString());
			str.append(",");
		}
		str.setLength(str.length()-1);
		str.append("}};\n");
		return str.toString();
	}
}
