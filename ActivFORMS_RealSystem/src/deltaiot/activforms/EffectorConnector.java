package deltaiot.activforms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import deltaiot.cbam.Adaptation;
import deltaiot.cbam.AdaptationOption;
import deltaiot.cbam.AdaptationOptionsCollector;
import deltaiot.cbam.AdaptationsCollector;
import deltaiot.cbam.Functions;
import deltaiot.cbam.RunStatisticResults;
import deltaiot.client.Effector;
import deltaiot.client.Probe;
import deltaiot.services.LinkSettings;
import deltaiot.services.Mote;
import deltaiot.types.DeltaIoT;
import deltaiot.types.Link;

public class EffectorConnector extends Synchronizer {

	ActivFORMSEngine engine;
	int changePower;
	int changeDistribution;
	int feedbackLoopCompleted;
	Effector effector;
	HashMap<Integer, List<LinkSettings>> moteSettings = new LinkedHashMap<>();
	Probe probe;

	@Override
	public synchronized void receive(int channelId, HashMap moteHashMap) {
		if (channelId == changePower || channelId == changeDistribution) {
						
			int newValue = (int) moteHashMap.get("newValue");
			Link link = Link.parseLink((HashMap) moteHashMap.get("&link"));
			LinkSettings setting = null;
			
			if (!moteSettings.containsKey(link.getSource())) {
				moteSettings.put(link.getSource(), new LinkedList<LinkSettings>());
			} else if (moteSettings.containsKey(link.getSource())) {
				List<LinkSettings> linkSettings = moteSettings.get(link.getSource());
				for (LinkSettings linkSetting : linkSettings) {
					if (linkSetting.getDest() == link.getDestination()) {
					      setting = linkSetting;
					}
				}
			}
			if (setting == null) {
				setting = new LinkSettings();
				setting.setSrc(link.getSource());
				setting.setDest(link.getDestination());
				moteSettings.get(link.getSource()).add(setting);
			}

			if (channelId == changeDistribution){
				setting.setDistributionFactor(newValue);
				System.out.println(String.format("Change Distribution: %d --> %d: %d", link.getSource(), link.getDestination(), newValue));
				// TODO: dit heb ik toegevoegd
				Adaptation adaptation = new Adaptation(link, newValue, Adaptation.AdaptationOptionType.DISTRIBUTIONCHANGE);
				AdaptationsCollector.addAdaptation(adaptation);
				// TODO: tot hier
			}
			else if (channelId == changePower){
				setting.setPowerSettings(newValue);
				if (link.getSource() != 7 && link.getSource() != 12 && link.getSource() != 10)
					setting.setDistributionFactor(100);				System.out.println(String.format("Change Power: %d --> %d: %d", link.getSource(), link.getDestination(), newValue));
				// TODO: code toegevoegd
				Adaptation adaptation = new Adaptation(link, newValue, Adaptation.AdaptationOptionType.POWERCHANGE);
				AdaptationsCollector.addAdaptation(adaptation);
				// TODO: tot hier
			}

		} else if (channelId == feedbackLoopCompleted) {
			System.out.println("Adaptation Completed:" + System.currentTimeMillis());
			
			// TODO: code underneath is added
			/*
			HashMap bestAdaptationOptionDeltaIoT = (HashMap) ((HashMap) moteHashMap.get("&K.bestAdaptationOption")).get("deltaIoT");
			HashMap<String, HashMap> uppaalMotes = new HashMap<String, HashMap>();
			uppaalMotes.put("motes", (HashMap) bestAdaptationOptionDeltaIoT.get("motes"));
			
			DeltaIoT deltaIoTUppaal = DeltaIoT.parseDeltaIoT(uppaalMotes);
			String bestAdaptationOptionStrUppaal = deltaIoTUppaal.toModelString();
			//System.out.println("Choosen Strategy (Uppaal):" + bestAdaptationOptionStrUppaal); // TODO heb dit in commentaar gezet
			//System.out.println("\n\nChoosen Strategy (Java)" + AdaptationOptionsCollector.getBestAdaptationOption().getAdaptationOption());
			System.out.println("Chosen Strategy (Uppaal): " + ((HashMap) moteHashMap.get("&K.bestAdaptationOption")).get("verificationResults"));			
			System.out.println("Chosen Strategy (Java): EnergyConsump=" + AdaptationOptionsCollector.getBestAdaptationOption().getEnergyConsumption() + "   PacketLoss=" + AdaptationOptionsCollector.getBestAdaptationOption().getPacketLoss());
			
			boolean correct = true;
			if (bestAdaptationOptionStrUppaal.equals(AdaptationOptionsCollector.getBestAdaptationOption().getAdaptationOption())) {
				System.out.println("CORRECT - BEIDE STRINGS MATCHEN" );
			}
			else {
				correct = false;
				System.out.println("\n\nFOUT - VERSCHIL IN BEIDE STRINGS");
				System.out.println("Choosen Strategy (Uppaal):" + bestAdaptationOptionStrUppaal); // TODO heb dit in commentaar gezet
				System.out.println("\nChoosen Strategy (Java)" + AdaptationOptionsCollector.getBestAdaptationOption().getAdaptationOption());
			}
			
			String path = RunStatisticResults.getPath();
			
			try {
				if(correct) {
					Files.write(Paths.get(path), String.format("\r\nCorrect").getBytes(), StandardOpenOption.APPEND);
				}
				else {
					Files.write(Paths.get(path), String.format("\r\nFOUT").getBytes(), StandardOpenOption.APPEND);
					Files.write(Paths.get(path), String.format("\r\nUppaal: %s", bestAdaptationOptionStrUppaal).getBytes(), StandardOpenOption.APPEND);
					Files.write(Paths.get(path), String.format("\r\nJava: %s", AdaptationOptionsCollector.getBestAdaptationOption().getAdaptationOption()).getBytes(), StandardOpenOption.APPEND);
				}
				Files.write(Paths.get(path), String.format("\r\n(Uppaal): %s", ((HashMap) moteHashMap.get("&K.bestAdaptationOption")).get("verificationResults")).getBytes(), StandardOpenOption.APPEND);
				Files.write(Paths.get(path), String.format("\r\n(Java): %s=%s,   %s=%s", "PacketLoss",AdaptationOptionsCollector.getBestAdaptationOption().getPacketLoss(), "EnergyConsumption", AdaptationOptionsCollector.getBestAdaptationOption().getEnergyConsumption()).getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {
				//exception handling left as an exercise for the reader
				System.out.println (e.toString());
				System.out.println("Could not find file " + path);
			}
			*/		
			// TODO: code above is added
			List<Mote> motes = probe.getAllMotes();
			Mote mote;
			
			System.out.println("Before");
			for (Entry<Integer, List<LinkSettings>> moteSetting : moteSettings.entrySet()) { for(LinkSettings setting: moteSetting.getValue()) {System.out.println(String.format("%d, %d, %d, %d, %d", setting.getSrc(), setting.getDest(), setting.getPowerSettings(), setting.getDistributionFactor(), setting.getSpreadingFactor()));} }

			
			if (moteSettings.containsKey(10)) {
				mote = getMote(10, motes);
				checkSettings(mote, 10);
			}
	
			if (moteSettings.containsKey(12)) {
				mote = getMote(12, motes);
				checkSettings(mote, 12);
			}
	
			if (moteSettings.containsKey(7)) {
				mote = getMote(7, motes);
				checkSettings(mote, 7);
			}
	
			System.out.println("After");
			
			for (Entry<Integer, List<LinkSettings>> moteSetting : moteSettings.entrySet()) {
				for(LinkSettings setting: moteSetting.getValue()) {System.out.println(String.format("%d, %d, %d, %d, %d", setting.getSrc(), setting.getDest(), setting.getPowerSettings(), setting.getDistributionFactor(), setting.getSpreadingFactor()));}
				effector.setMoteSettings(moteSetting.getKey(), moteSetting.getValue());
			}
			notifyAdaptationPerformed();
			
			// TODO: code toegevoegd
			AdaptationsCollector.feedBackLoopCompleted();
			// TODO: tot hier
		}
	}

	private void checkSettings(Mote mote, int id) {
		if (moteSettings.containsKey(id)) {
			List<LinkSettings> settings = moteSettings.get(id);
			for(LinkSettings setting:settings) {
				if (setting.getPowerSettings() == -4) {
					for(deltaiot.services.Link link:mote.getLinks()) {
						if (link.getDest()== setting.getDest()) {
							setting.setPowerSettings(link.getPower());
						}
					}
				}
				else if (setting.getDistributionFactor() == -1) {
					for(deltaiot.services.Link link:mote.getLinks()) {
						if (link.getDest()== setting.getDest()) {
							setting.setDistributionFactor(link.getDistribution());
						}
					}
				}	
			}
		}
	}
	

	private Mote getMote(int i, List<Mote> motes) {
		for(Mote m: motes)
			if (m.getMoteid() == i)
				return m;
		return null;
	}


	public void setEngine(ActivFORMSEngine engine) {
		this.engine = engine;
		changePower = engine.getChannel("changePower");
		changeDistribution = engine.getChannel("changeDistribution");
		feedbackLoopCompleted = engine.getChannel("feedbackLoopCompleted");
		engine.register(changePower, this, "&link", "newValue");
		engine.register(changeDistribution, this, "&link", "newValue");
		engine.register(feedbackLoopCompleted, this, "&K.bestAdaptationOption");
	}

	public void setEffector(Effector effector) {
		this.effector = effector;
	}

	void notifyAdaptationPerformed() {
		Settings.adaptationDone.set(true);
	}

	public void setProbe(Probe probe) {
		this.probe = probe;
	}

}
