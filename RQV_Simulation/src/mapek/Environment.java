package mapek;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class Environment {
	List<SNR> linksSNR = new LinkedList<>();
	List<TrafficProbability> motesLoad = new LinkedList<>();
	
	public double getSNR(Link link){
		for(SNR snr: linksSNR){
			if (snr.source == link.getSource() && link.destination == link.getDestination()){
				return snr.SNR;
			}
		}
		throw new RuntimeException("SNR not found of the given link:" + link);
	}
	
	public void setSNR(Link link, double newSNR){
		for(SNR snr: linksSNR){
			if (snr.source == link.getSource() && link.destination == link.getDestination()){
				snr.SNR = newSNR;
				return;
			}
		}
		throw new RuntimeException("SNR not found of the given link:" + link);
	}
	
	@Override
	public String toString() {
		Gson gsn = new Gson();
		return gsn.toJson(this);
	}

	public Environment getCopy() {
		Environment env = new Environment();
		for(SNR snr: linksSNR){
			env.linksSNR.add(snr.getCopy());
		}
		
		for(TrafficProbability load: motesLoad){
			env.motesLoad.add(load.getCopy());
		}
		return env;
	}
}
