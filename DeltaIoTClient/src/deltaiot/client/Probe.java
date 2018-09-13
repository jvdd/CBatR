package deltaiot.client;

import java.net.URL;
import java.util.ArrayList;

import deltaiot.services.DeltaIoTWebService;
import deltaiot.services.DeltaIoTWebServiceService;
import deltaiot.services.Mote;
import deltaiot.services.QoS;

public class Probe {
	
	String token;
	URL url;
	DeltaIoTWebService impl;
	
	public Probe(){
		Configurations.loadConfigurations();
		this.token = Configurations.token;
		this.url = Configurations.url;
		initializeClient();
	}
	
	public Probe(String token, URL url){
		this.token = token;
		this.url = url;
		initializeClient();
	}
	
	private void initializeClient(){
		DeltaIoTWebServiceService service = new DeltaIoTWebServiceService(this.url);
		impl = service.getDeltaIoTWebServicePort();
	}
	
	public ArrayList<Mote> getAllMotes(){
		return (ArrayList<Mote>) impl.getAllMotes(token);
	}
	
	public double getMoteEnergyLevel(int moteId){
		return impl.getMoteEnergyLevel(token, moteId);
	}
	
	public double getMoteTrafficLoad(int moteId){
		return impl.getMoteTrafficLoad(token, moteId);
	}
	
	public int getLinkPowerSetting(int src, int dest){
		return impl.getLinkPowerSetting(token, src, dest);
	}
	
	public int getLinkSpreadingFactor(int src, int dest){
		return impl.getLinkSpreadingFactor(token, src, dest);
	}
	
	public double getLinkSignalNoise(int src, int dest){
		return impl.getLinkSignalNoise(token, src, dest);
	}
	
	public double getLinkDistributionFactor(int src, int dest){
		return impl.getLinkDistributionFactor(token, src, dest);
	}
	
	public ArrayList<QoS> getNetworkQoS(int period){
		return (ArrayList<QoS>) impl.getNetworkQoS(token, period);
	}
}
