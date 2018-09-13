package deltaiot.client;

import java.util.List;

import deltaiot.services.LinkSettings;

public interface Effector {
	
	public void setMoteSettings(int moteId, List<LinkSettings> linkSettings);
	
	public void setDefaultConfiguration();
}
