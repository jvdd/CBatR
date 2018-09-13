package deltaiot.activforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import activforms.types.UppaalType;
import activforms.types.Int;
import deltaiot.smc.SMCChecker;
import deltaiot.types.DeltaIoT;
import deltaiot.types.Environment;

public class SMCConnector extends Synchronizer {

	ActivFORMSEngine engine;
	SMCChecker smcChecker;

	int invokeVerifier, verificationCompleted, stopVerification, verificationInterrupted;

	public SMCConnector(SMCChecker smcChecker, ActivFORMSEngine engine) {
		setEngine(engine);
		this.smcChecker = smcChecker;
	}

	@Override
	public void receive(int channelId, HashMap<String, Object> data) {

		if (channelId == invokeVerifier) { // Gives you the possibility to receive multiple signals in 1 class

			HashMap adaptationOptions = (HashMap) ((HashMap<String, Object>) data.get("&adaptationOptions"))
					.get("options");
			int size = ((Int)((HashMap) data.get("&adaptationOptions")).get("size")).getValue();

			HashMap adaptationOption;
			Environment environment = Environment
					.parseEnvironment((HashMap) ((HashMap) data.get("&adaptationOptions")).get("environment"));
			String environmentStr = environment.toModelString();
			String adaptationOptionStr;
			DeltaIoT deltaIoT;
			HashMap<String, Int> verificationResults;
			//HashMap<String, Int> CBAMResults;
			List<String> results = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				adaptationOption = (HashMap) adaptationOptions.get(i);
				//System.out.println(adaptationOption);
				//System.out.println(adaptationOption.get("deltaIoT"));
				deltaIoT = DeltaIoT.parseDeltaIoT((HashMap) adaptationOption.get("deltaIoT"));
				adaptationOptionStr = deltaIoT.toModelString();
				verificationResults = (HashMap<String, Int>) adaptationOption.get("verificationResults");
				//CBAMResults = (HashMap<String, Int>) adaptationOption.get("CBAMResults");
				smcChecker.checkCAO(adaptationOptionStr, environmentStr, verificationResults);
				//for (Entry<String, Integer> result : verificationResults.entrySet())
				//	results.add(String.format("adaptationOptions.options[%d].verificationResults.%s=%d;", i,
				//			result.getKey(), result.getValue()));
			}
			
			// TODO: code underneath is added
			//AdaptationOptionsCollector.analyzeAdaptationOptions();
			//AdaptationOptionsCollector.generationCompleted();
			// TODO: code above is added
			try {
				engine.send(verificationCompleted);//, results.toArray(new String[results.size()]));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (channelId == stopVerification) {
			engine.send(verificationInterrupted);
		}
	}

	private void calculateBestCAO(HashMap CAO) {
		int R1 = 10;
		int R2 = 10;
		HashMap cao;
		int packetLoss, energyConsumption, latency;
		int i;
		int bestEConsumption = Integer.MAX_VALUE, bestPacketLoss = Integer.MAX_VALUE, bestLatency = Integer.MAX_VALUE;

		int bestIndex = -1;
		if (R1 == 0) {
			for (i = 0; i < 294; i++) {
				cao = (HashMap) CAO.get(i);
				if ((int) cao.get("packetLoss") < bestPacketLoss || (int) cao.get("packetLoss") == bestPacketLoss
						&& (int) cao.get("energyConsumption") < bestEConsumption) {
					bestIndex = i;
					bestEConsumption = (int) cao.get("energyConsumption");
					bestPacketLoss = (int) cao.get("packetLoss");
				}
			}
		} else {
			for (i = 0; i < 294; i++) {
				cao = (HashMap) CAO.get(i);
				packetLoss = (int) ((UppaalType) cao.get("packetLoss")).getValue();
				energyConsumption = (int) ((UppaalType) cao.get("energyConsumption")).getValue();
				latency = (int) ((UppaalType) cao.get("latency")).getValue();

				if (packetLoss <= R1 && ((latency < bestLatency)
						|| (latency == bestLatency && energyConsumption < bestEConsumption))) {
					bestIndex = i;
					bestEConsumption = energyConsumption;
					bestPacketLoss = packetLoss;
					bestLatency = latency;
				}
			}
		}
		if (bestIndex == 0) {
			for (i = 0; i < 294; i++) {
				cao = (HashMap) CAO.get(i);
				if ((int) cao.get("packetLoss") <= R1 && (int) cao.get("energyConsumption") < bestEConsumption) {
					bestIndex = i;
					bestEConsumption = (int) cao.get("energyConsumption");
					bestPacketLoss = (int) cao.get("packetLoss");
				}
			}
		}

		System.out.println(CAO.get(bestIndex != -1 ? bestIndex : 0));
	}

	public void setEngine(ActivFORMSEngine engine) {
		this.engine = engine;

		invokeVerifier = engine.getChannel("invokeVerifier");
		stopVerification = engine.getChannel("stopVerification");
		verificationCompleted = engine.getChannel("verificationCompleted");
		verificationInterrupted = engine.getChannel("verificationInterrupted");
		
		//engine.register(invokeVerifier, this, "&adaptationOptions", "&K.currentConfig"); // Register the signal you want to receive + this = class dat wil ontvagen
		// TODO: nagaan of dit hieronder voldoende is of dit hierboven ook nog moet
		engine.register(invokeVerifier, this, "&adaptationOptions");
		engine.register(stopVerification, this);
	}

	void setSMCChecker(SMCChecker smcChecker) {
		this.smcChecker = smcChecker;
	}

	SMCChecker getSMCChecker() {
		return smcChecker;
	}
}
