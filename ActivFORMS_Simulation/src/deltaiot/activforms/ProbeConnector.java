package deltaiot.activforms;

import static deltaiot.activforms.Settings.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import activforms.engine.ActivFORMSEngine;
import activforms.types.Int;
import deltaiot.cbam.Adaptation;
import deltaiot.cbam.AdaptationOption;
import deltaiot.cbam.AdaptationOptionsCollector;
import deltaiot.cbam.RunStatisticResults;
import deltaiot.client.Probe;
import deltaiot.services.Link;
import deltaiot.services.Mote;
import deltaiot.services.QoS;
import deltaiot.types.DeltaIoT;

public class ProbeConnector {
	ActivFORMSEngine engine;
	int monitor;
	
	public static int FINISHED = 1;// TODO dit toegevoegd

	Probe probe;
	HashMap deltaIoTModel;
	
	
	public void sendMessageToFeedbackLoop(List<Mote> motes, QoS qos) {

		// Prepare message to send		
		/*List<String> data = new ArrayList<>();
		int id, i;
		for(Mote mote: motes){
			id = mote.getMoteid();
			data.add(String.format("deltaIoT.motes[%d].moteId=%d;", id, id));
			data.add(String.format("deltaIoT.motes[%d].load=%d;", id, mote.getLoad()));
			data.add(String.format("deltaIoT.motes[%d].energyLevel=%d;", id, Math.round(mote.getBattery())));
			data.add(String.format("deltaIoT.motes[%d].parents=%d;", id, mote.getParents()));
			data.add(String.format("deltaIoT.motes[%d].dataProbability=%d;", id, mote.getDataProbability()));
			data.add(String.format("deltaIoT.motes[%d].queueSize=%d;", id, mote.getQueueSize()));
			i = 0;
			for(Link link: mote.getLinks()){
				data.add(String.format("deltaIoT.motes[%d].links[%d].source=%d;", id, i,link.getSource()));
				data.add(String.format("deltaIoT.motes[%d].links[%d].destination=%d;", id, i,link.getDest()));
				data.add(String.format("deltaIoT.motes[%d].links[%d].powerSetting=%d;", id, i,link.getPower()));
				data.add(String.format("deltaIoT.motes[%d].links[%d].packetLoss=%d;", id, i,link.getPacketLoss()));
				data.add(String.format("deltaIoT.motes[%d].links[%d].SNR=%d;", id, i,Math.round(link.getSNR())));
				data.add(String.format("deltaIoT.motes[%d].links[%d].distribution=%d;", id, i,link.getDistribution()));
				i++;
			}
		}*/
		
		// ORIGINAL CODE
		// Copy QoS properties
		((Int) ((HashMap)deltaIoTModel.get("qos")).get("packetLoss")).setValue(toInt(qos.getPacketLoss()));
		((Int) ((HashMap)deltaIoTModel.get("qos")).get("energyConsumption")).setValue(toInt(qos.getEnergyConsumption()));
						
		//AdaptationOptionsCollector.initializeCurrentConfiguration(qos.getEnergyConsumption(), qos.getPacketLoss());
		
		// Copy Mote properties
		for(Mote mote: motes){
			HashMap<String, Int> moteModel = (HashMap<String, Int>) ((HashMap)deltaIoTModel.get("motes")).get(mote.getMoteid());
			moteModel.get("moteId").setValue(mote.getMoteid());
			moteModel.get("load").setValue(mote.getLoad());
			moteModel.get("energyLevel").setValue(((Double)mote.getBattery()).intValue());
			moteModel.get("parents").setValue(mote.getParents());
			moteModel.get("dataProbability").setValue(mote.getDataProbability());
			moteModel.get("queueSize").setValue(mote.getCurrentQSize());
	
			// Set Link properties
			int i = 0;
			for(Link link: mote.getLinks()){
				HashMap<String, Int> linkMote = (HashMap<String, Int>) ((HashMap)(Object)moteModel.get("links")).get(i++);
					
				linkMote.get("source").setValue(link.getSource());
				linkMote.get("destination").setValue(link.getDest());
				linkMote.get("powerSetting").setValue(link.getPower());
				linkMote.get("SNR").setValue(link.getSNR().intValue());
				linkMote.get("distribution").setValue(link.getDistribution());
				linkMote.get("SF").setValue(link.getSF());
			}
		}
		// TODO: HIERBOVEN TOEGEVOEGD
		
		// TODO: HIERONDER TOEGEVOEGD 
		/*
		double totalEnergyConsumption = qos.getEnergyConsumption() + AdaptationsCollector.getAdaptationsEnergyConsumption();
		String path = RunStatisticResults.getPath();
		System.out.println("Energy Consumption: " + qos.getEnergyConsumption());
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			//Files.write(Paths.get(path), "".getBytes(), StandardOpenOption.APPEND); // Niks wegschrijven
			Files.write(Paths.get(path), String.format("\r\n%24s, %24s, %24s, %22s, %22s, %22s, %8s, %12s, %10s,", qos.getEnergyConsumption(), AdaptationsCollector.getAdaptationsEnergyConsumption(), totalEnergyConsumption, qos.getPacketLoss(), qos.getLatency(), qos.getQueueLoss(), qos.getSent(), dateFormat.format(date), timeFormat.format(date)).getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			System.out.println (e.toString());
			System.out.println("Could not find file " + path);
		}
		System.out.println("Number of links adapted: " + AdaptationsCollector.getAdaptations().size());
		System.out.println("Number of unique links adapted: " + AdaptationsCollector.getUniqueLinks().size());
		System.out.println("Energy consumption of adaptations: " + AdaptationsCollector.getAdaptationsEnergyConsumption());
		System.out.println("Total energy consumption: " + totalEnergyConsumption);
		System.out.println(); */
		// TODO: TOT HIER TOEGEVOEGD
		
		// send signal
		try {
			engine.send(monitor);//, data.toArray(new String[data.size()]));
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void setEngine(ActivFORMSEngine engine) {
		this.engine = engine;

		// receive monitor channel identifier
		monitor = engine.getChannel("monitor");
		deltaIoTModel = (HashMap) engine.getData("&deltaIoT").get("&deltaIoT");
	}

	public ActivFORMSEngine getEngine() {
		return engine;
	}

	public void start() {
		thread.start();
	}

	Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			int runs = 96;
			for(int i = 0; i < runs; i++) {
				System.out.println("\n\nRun: " + i); // TODO dit erbij gezet
				startTime = System.currentTimeMillis();
				List<Mote> motes = probe.getAllMotes();
				List<QoS> qosList = probe.getNetworkQoS(1);
				adaptationDone.set(false);
				System.out.println("Sending message to feedback loop at time:" + System.currentTimeMillis());
				sendMessageToFeedbackLoop(motes, qosList.get(0));
				waitAdaptationPerformed();
				//printAdaptationInfo(); // TODO: this is added
				//AdaptationOptionsCollector.initialize
				RunStatisticResults.writeDataToFile(qosList.get(0));
				System.gc();
			}
			probe.getNetworkQoS(runs).forEach(qos -> System.out.println(qos));
			FINISHED += 1; // TODO: dit toegevoegd
		}
	});
	

	public void setProbe(Probe probe) {
		this.probe = probe;
	}

	void waitAdaptationPerformed() {
		while(Settings.adaptationDone.get() == false){
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
