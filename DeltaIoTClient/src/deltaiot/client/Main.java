package deltaiot.client;

import java.util.List;

import deltaiot.services.Mote;
import deltaiot.services.QoS;

public class Main {

	public static void main(String[] args) {
		
		Probe probe = new Probe();
		
		List<Mote> motes = probe.getAllMotes();
		
		motes.forEach(mote -> System.out.println(String.format("MoteId: %d, BatteryRemaining: %f, Parents:%d", mote.getMoteid(), mote.getBattery(), mote.getParents())));
		
		List<QoS> qosList = probe.getNetworkQoS(10);
		for(QoS qos: qosList){
			System.out.println(String.format("%s, %f, %f", qos.getPeriod(), qos.getPacketLoss(), qos.getEnergyConsumption()));
		}
		
	}

}
