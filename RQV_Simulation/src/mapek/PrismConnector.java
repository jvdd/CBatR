package mapek;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrismConnector {

	List<AdaptationOption> adaptationOptions;
	Environment environment;
	//DecimalFormat df = new DecimalFormat("#.##");
	PrismChecker prismChecker = new PrismChecker();
	List<AdaptationOption> verifiedOptions;
	AtomicBoolean flag = new AtomicBoolean(true);

	public void setAdaptationOptions(List<AdaptationOption> adaptationOptions, Environment environment) {
		this.adaptationOptions = adaptationOptions;
		this.environment = environment;
		System.out.println("Environment:" + environment);
		
	}

	void startVerification() {
		flag.set(true);
		verifiedOptions = new LinkedList<>();
		// Invoke the Prism checker
		for (AdaptationOption config : adaptationOptions) {
			if (flag.get()) {
				prismChecker.checkPacketLoss(config, environment);
				//config.verificResults.packetLoss = Double.valueOf(df.format(config.verificResults.packetLoss));
			}
		}

		// all packet loss configurations checked. Now check energy consumption
		// for configurations that has minimum packet loss
		
		List<AdaptationOption> packetLossSatisfiedOptions = new LinkedList<>();
		for (AdaptationOption config : adaptationOptions) {
			if (Goals.satisfyGoalPacketLoss(config)) {
				packetLossSatisfiedOptions.add(config);
			}
		}
		System.out.println("No. of options to check for energy consumption:" + packetLossSatisfiedOptions.size() + ", Time:" + System.currentTimeMillis());
		packetLossSatisfiedOptions.forEach(option -> System.out.println(option.verificResults.packetLoss));
		// Sort configuration according to packet loss
		packetLossSatisfiedOptions.sort(new Comparator<AdaptationOption>() {
			@Override
			public int compare(AdaptationOption o1, AdaptationOption o2) {
				return Double.compare(o1.verificResults.packetLoss,o2.verificResults.packetLoss);
			}
		});
		
		System.out.println("Sorted:");
		packetLossSatisfiedOptions.forEach(option -> System.out.println(option.verificResults.packetLoss));
		
		for (AdaptationOption config : packetLossSatisfiedOptions) {
			if (flag.get()) {
				prismChecker.checkEnergyConsumption(config, environment);
				System.out.println(config);
				verifiedOptions.add(config);
			}
		}
	}

	void stopVerification() {
		flag.set(false);
	}
	
	List<AdaptationOption> getVerifiedOptions(){
		return verifiedOptions;
	}
}
