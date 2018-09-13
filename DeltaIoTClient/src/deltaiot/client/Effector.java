package deltaiot.client;

import java.net.URL;
import java.util.List;
import deltaiot.services.DeltaIoTWebService;
import deltaiot.services.DeltaIoTWebServiceService;
import deltaiot.services.LinkSettings;

public class Effector {
	
	String token;
	URL url;
	DeltaIoTWebService impl;
	
	public Effector(){
		Configurations.loadConfigurations();
		this.token = Configurations.token;
		this.url = Configurations.url;
		initializeClient();
	}
	public Effector(String token, URL url){
		this.token = token;
		this.url = url;
		initializeClient();
	}
	
	void initializeClient(){
		DeltaIoTWebServiceService service = new DeltaIoTWebServiceService(url);
		impl = service.getDeltaIoTWebServicePort();
	}
//	
//	public void setLinkSF(int src, int dest, int sf){
//		impl.setLinkSF(token, src, dest, sf);
//	}
//	
//	public void setLinkPower(int src, int dest, int power){
//		impl.setLinkPower(token, src, dest, power);
//	}
//		
//	public void setLinkDistributionFactor(String token, int src, int dest, int distributionFactor){
//		impl.setLinkDistributionFactor(token, src, dest, distributionFactor);
//	}
	
	public void setMoteSettings(int moteId, List<LinkSettings> linkSettings){
		impl.setMoteSettings(token, moteId, linkSettings);
	}
	
	public void setDefaultConfiguration(){
		impl.setDefaultConfiguration(token);
	}
}
