package simulator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import deltaiot.DeltaIoTSimulator;
import deltaiot.services.QoS;
import domain.Gateway;

public class Main {
	
	public static void main(String [ ] args) {
		run();
	}
	
	public static void run() {
		Simulator simul = DeltaIoTSimulator.createSimulatorForDeltaIoT();
		
		// Do logic
	    for (int i = 0; i < 96; ++i) {
	    	simul.doSingleRun();
			//simul.doMultipleRuns(96);
	    	
	    	//for(Gateway gateway: simul.getGateways()) {
			//	System.out.println(gateway);
			//}
			/*for(Mote mote: motes) {
				System.out.println(mote);
			}*/
		}
		QoS qos;
		// TODO: HIERONDER TOEGEVOEGD
		String path = "C:\\Users\\Jeroen\\Documents\\BachelorProef\\Resultaten\\Reference.txt";
		try {
			Files.write(Paths.get(path), String.format("\r\n%24s, %22s, %22s, %22s, %8s, %12s, %10s,", "EnergyConsumption", "PacketLoss", "Latency", "QueueLoss", "Sent", "Date", "Time").getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			System.out.println (e.toString());
			System.out.println("Could not find file " + path);
		}
		// TODO: HIERBOVEN TOEGEVOEGD
	    for(int i = 0; i < simul.getQosValues().size(); i++){
	    	qos = simul.getQosValues().get(i);
	    	//System.out.format("%d, %f, %f, %f, %f\n", i, qos.getPacketLoss(), qos.getEnergyConsumption(), qos.getLatency(), qos.getQueueLoss()); // TODO: Dit heb ik in commentaar gezet
			// TODO: HIERONDER TOEGEVOEGD
	    	System.out.format("%d, %f, %f, %f, %f\n", i, qos.getPacketLoss(), qos.getEnergyConsumption(), qos.getLatency(), qos.getQueueLoss());
			try {
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date();
				//Files.write(Paths.get(path), "".getBytes(), StandardOpenOption.APPEND); // Niks wegschrijven
				Files.write(Paths.get(path), String.format("\r\n%24s, %22s, %22s, %22s, %8s, %12s, %10s,", qos.getEnergyConsumption(), qos.getPacketLoss(), qos.getLatency(), qos.getQueueLoss(), qos.getSent() ,dateFormat.format(date), timeFormat.format(date)).getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {
				//exception handling left as an exercise for the reader
				System.out.println (e.toString());
				System.out.println("Could not find file " + path);
			}
			// TODO: TOT HIER TOEGEVOEGD
	    }
	    
	}
	
}
