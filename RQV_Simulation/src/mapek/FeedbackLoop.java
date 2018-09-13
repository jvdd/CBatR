package mapek;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import deltaiot.client.Effector;
import deltaiot.services.LinkSettings;
import deltaiot.services.QoS;
import deltaiot.client.Probe;

public class FeedbackLoop {

	Probe probe;
	Effector effector;

	// Knowledge
	public static final int DISTRIBUTION_GAP = 20;
	Configuration currentConfiguration;
	Configuration previousConfiguration;
	List<PlanningStep> steps = new LinkedList<>();
	List<SNREquation> snrEquations = new LinkedList<>();
	List<AdaptationOption> adaptationOptions = new LinkedList<>();
	List<AdaptationOption> verifiedOptions;
	PrismConnector prismConnector = new PrismConnector();

	public FeedbackLoop() {
		snrEquations.add(new SNREquation(2, 4, 0.0169, 7.4076));
		snrEquations.add(new SNREquation(3, 1, 0.4982, 1.2468));
		snrEquations.add(new SNREquation(4, 1, 0.8282, -8.1246));
		snrEquations.add(new SNREquation(5, 9, 0.4932, -4.4898));
		snrEquations.add(new SNREquation(6, 4, 0.6199, -9.8051));
		snrEquations.add(new SNREquation(7, 3, 0.5855, -6.644));
		snrEquations.add(new SNREquation(7, 2, 0.5398, -2.0549));
		snrEquations.add(new SNREquation(8, 1, 0.5298, -0.1031));
		snrEquations.add(new SNREquation(9, 1, 0.8284, -7.2893));
		snrEquations.add(new SNREquation(10, 6, 0.8219, -7.3331));
		snrEquations.add(new SNREquation(10, 5, 0.6463, -3.0037));
		snrEquations.add(new SNREquation(11, 7, 0.714, -3.1985));
		snrEquations.add(new SNREquation(12, 7, 0.9254, -16.21));
		snrEquations.add(new SNREquation(12, 3, 0.1, 6));
		snrEquations.add(new SNREquation(13, 11, 0.6078, -3.6005));
		snrEquations.add(new SNREquation(14, 12, 0.4886, -4.7704));
		snrEquations.add(new SNREquation(15, 12, 0.5899, -7.1896));
	}

	public void setProbe(Probe probe) {
		this.probe = probe;
	}

	public void setEffector(Effector effector) {
		this.effector = effector;
	}

	public void start() {
		System.out.println("Feedback loop started.");
		// for (int i = 0; i < 96; i++) {
		while (true) {
			// System.out.println("Runnumber:" + (i + 1));
			monitor();
			try {
				Thread.currentThread().sleep(100);

			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	void monitor() {
		System.out.println("Monitoring started:" + System.currentTimeMillis());
		ArrayList<deltaiot.services.Mote> motes = probe.getAllMotes();
		List<Mote> newMotes = new LinkedList<>();

		previousConfiguration = currentConfiguration;
		currentConfiguration = new Configuration();

		Mote newMote;
		Link newLink;
		for (deltaiot.services.Mote mote : motes) {
			newMote = new Mote();
			newMote.moteId = mote.getMoteid();
			newMote.energyLevel = mote.getBattery();
			currentConfiguration.environment.motesLoad.add(new TrafficProbability(mote.getMoteid(), mote.getDataProbability()));
			for (deltaiot.services.Link link : mote.getLinks()) {
				newLink = new Link();
				newLink.source = link.getSource();
				newLink.destination = link.getDest();
				newLink.distribution = link.getDistribution();
				newLink.power = link.getPower();
				newMote.links.add(newLink);
				currentConfiguration.environment.linksSNR.add(new SNR(link.getSource(), link.getDest(), link.getSNR()));
			}
			newMotes.add(newMote);
		}
		currentConfiguration.system = new ManagedSystem(newMotes);
		QoS qos = probe.getNetworkQoS(1).get(0);
		currentConfiguration.qualities.packetLoss = qos.getPacketLoss();
		currentConfiguration.qualities.energyConsumption = qos.getEnergyConsumption();

		analysis();
	}

	void analysis() {

		// analyze all link settings
		boolean adaptationRequired = analysisRequired();

		if (!adaptationRequired)
			return;

		//
		AdaptationOption newPowerSettingsConfig = new AdaptationOption();
		newPowerSettingsConfig.system = currentConfiguration.system.getCopy();
		analyzePowerSettings(newPowerSettingsConfig);
		// System.out.println(newPowerSettingsConfig);
		removePacketDuplication(newPowerSettingsConfig);
		// System.out.println(newPowerSettingsConfig);
		composeAdaptationOptions(newPowerSettingsConfig);

		// adaptationOptions.forEach(o -> System.out.println(o));
		prismConnector.setAdaptationOptions(adaptationOptions, currentConfiguration.environment);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				prismConnector.startVerification();
			}
		});

		boolean interrupted = false;
		thread.start();
		try {
			// let the thread run for 5 minutes
			thread.join(480000);
			if (thread.isAlive()) {
				interrupted = true;
				prismConnector.stopVerification();
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		verifiedOptions = prismConnector.getVerifiedOptions();
		// if (interrupted) {
		// check if any configuration satisfies packet loss
		boolean sufficientResultsAvailable = false;
		if (verifiedOptions.size() > 0) {
			sufficientResultsAvailable = true;
			System.out.println("Partial verification results are being used!");
		}

		if (!sufficientResultsAvailable) {
			// create failsafe configuration
			verifiedOptions.clear();
			AdaptationOption failSafeConfig = new AdaptationOption();
			failSafeConfig.system = currentConfiguration.system.getCopy();
			for (Mote mote : failSafeConfig.system.motes.values()) {
				for (Link link : mote.links) {
					link.setPower(15);
					link.setDistribution(100);
				}
			}
			System.err.println("Fail safe configuration is being used!");
			verifiedOptions.add(failSafeConfig);
		}
		// }

		planning();

	}

	void composeAdaptationOptions(AdaptationOption newConfiguration) {
		List<Mote> moteOptions = new LinkedList<>();
		if (adaptationOptions.isEmpty()) {
			// adaptationOptions.add(newConfiguration);
			// generate adaptation options for the first time
			for (Mote mote : newConfiguration.system.motes.values()) {
				if (mote.getLinks().size() == 2) {
					mote = mote.getCopy();
					moteOptions.clear();
					for (int i = 0; i <= 100; i += DISTRIBUTION_GAP) {
						mote.getLink(0).setDistribution(i);
						mote.getLink(1).setDistribution(100 - i);
						moteOptions.add(mote.getCopy());
					}
					saveAdaptationOptions(newConfiguration, moteOptions, mote.getMoteId());
				}
			}
		}
	}

	private void saveAdaptationOptions(AdaptationOption firstConfiguration, List<Mote> moteOptions, int moteId) {
		AdaptationOption newAdaptationOption;
		if (adaptationOptions.isEmpty()) {
			for (int j = 0; j < moteOptions.size(); j++) {
				newAdaptationOption = firstConfiguration.getCopy();
				newAdaptationOption.system.motes.put(moteId, moteOptions.get(j));
				adaptationOptions.add(newAdaptationOption);
			}
		} else {
			int size = adaptationOptions.size();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < moteOptions.size(); j++) {
					newAdaptationOption = adaptationOptions.get(i).getCopy();
					newAdaptationOption.system.motes.put(moteId, moteOptions.get(j));
					adaptationOptions.add(newAdaptationOption);
				}
			}
		}
	}

	private void analyzePowerSettings(AdaptationOption newConfiguration) {
		int powerSetting;
		double newSNR;
		for (Mote mote : newConfiguration.system.motes.values()) {
			for (Link link : mote.getLinks()) {
				powerSetting = link.getPower();
				newSNR = currentConfiguration.environment.getSNR(link);
				// find interference
				double diffSNR = getSNR(link.getSource(), link.getDestination(), powerSetting) - newSNR;
				if (powerSetting < 15 & newSNR < 0 && newSNR != -50) {
					while (powerSetting < 15 && newSNR < 0) {
						newSNR = getSNR(link.getSource(), link.getDestination(), ++powerSetting) - diffSNR;
					}
				} else if (newSNR > 0 && powerSetting > 0) {
					do {
						newSNR = getSNR(link.getSource(), link.getDestination(), powerSetting - 1) - diffSNR;
						if (newSNR >= 0) {
							powerSetting--;
						}
					} while (powerSetting > 0 && newSNR >= 0);
				}
				if (link.getPower() != powerSetting) {
					link.setPower(powerSetting);
					currentConfiguration.environment.setSNR(link,
							getSNR(link.getSource(), link.getDestination(), powerSetting - 1) - diffSNR);
				}
			}
		}
	}

	private void removePacketDuplication(AdaptationOption newConfiguration) {
		for (Mote mote : newConfiguration.system.motes.values()) {
			if (mote.getLinks().size() == 2) {
				if (mote.getLink(0).getDistribution() == 100 && mote.getLink(1).getDistribution() == 100) {
					mote.getLink(0).setDistribution(50);
					mote.getLink(1).setDistribution(50);
				}
			}
		}
	}

	double getSNR(int source, int destination, int newPowerSetting) {
		for (SNREquation equation : snrEquations) {
			if (equation.source == source && equation.destination == destination) {
				return equation.multiplier * newPowerSetting + equation.constant;
			}
		}
		throw new RuntimeException("Link not found:" + source + "-->" + destination);
	}

	static final int SNR_BELOW_THRESHOLD = 0;
	static final int SNR_UPPER_THRESHOLD = 5;
	static final int ENERGY_CONSUMPTION_THRESHOLD = 5;
	static final int PACKET_LOSS_THRESHOLD = 5;
	static final int MOTES_TRAFFIC_THRESHOLD = 10;
	static final int MAX_LINKS = 17;
	static final int MAX_MOTES = 15;
	int i;
	boolean analysisRequired() {
		// for simulation we use adaptation after 4 periods
		return i++%4 == 0;
/*
		// if first time perform adaptation
		if (previousConfiguration == null)
			return true;

		// Check LinksSNR
		double linksSNR;
		for (int j = 0; j < MAX_LINKS; j++) {
			linksSNR = currentConfiguration.environment.linksSNR.get(j).SNR;
			if (linksSNR < SNR_BELOW_THRESHOLD || linksSNR > SNR_UPPER_THRESHOLD) {
				return true;
			}
		}

		// Check MotesTraffic
		double diff;

		for (int i = 2; i <= MAX_MOTES; i++) {
			diff = currentConfiguration.environment.motesLoad.get(i).load
					- previousConfiguration.environment.motesLoad.get(i).load;
			if (diff > MOTES_TRAFFIC_THRESHOLD || diff > -MOTES_TRAFFIC_THRESHOLD) {
				return true;
			}
		}

		// check qualities
		if ((currentConfiguration.qualities.packetLoss > previousConfiguration.qualities.packetLoss
				+ PACKET_LOSS_THRESHOLD)
				|| (currentConfiguration.qualities.energyConsumption > previousConfiguration.qualities.energyConsumption
						+ ENERGY_CONSUMPTION_THRESHOLD)) {
			return true;
		}

		// check if system settings are not what should be
		return !currentConfiguration.system.toString().equals(previousConfiguration.system.toString());
	*/
	}

	void planning() {

		AdaptationOption bestAdaptationOption = null;

		// boolean packetLossSatisfied, energyConsumptionSatisfied;
		// bestAdaptationOption = adaptationOptions.get(0);

		for (int i = 0; i < verifiedOptions.size(); i++) {
			if (Goals.satisfyGoalPacketLoss(verifiedOptions.get(i))
					&& Goals.optimizationGoalEnergyCosnumption(bestAdaptationOption, verifiedOptions.get(i))) {

				bestAdaptationOption = verifiedOptions.get(i);
			}
		}

		if (bestAdaptationOption == null) {
			throw new RuntimeException("Use faile safety configuration");
		} else {
			System.out.println("SelectedOption:" + bestAdaptationOption);
		}

		// Go through all links
		Link newLink, oldLink;
		for (Mote mote : bestAdaptationOption.system.motes.values()) {
			for (int i = 0; i < mote.getLinks().size(); i++) {
				newLink = mote.getLinks().get(i);
				oldLink = currentConfiguration.system.motes.get(mote.moteId).getLink(i);
				if (newLink.getPower() != oldLink.getPower()) {
					steps.add(new PlanningStep(Step.CHANGE_POWER, newLink, newLink.getPower()));
				}
				if (newLink.getDistribution() != oldLink.getDistribution()) {
					steps.add(new PlanningStep(Step.CHANGE_DIST, newLink, newLink.getDistribution()));
				}
			}
		}

		if (steps.size() > 0) {
			execution();
		}
	}

	void execution() {
		boolean addMote;
		List<Mote> motesEffected = new LinkedList<Mote>();
		for (Mote mote : currentConfiguration.system.motes.values()) {
			addMote = false;
			for (PlanningStep step : steps) {
				if (step.link.getSource() == mote.getMoteId()) {
					addMote = true;
					if (step.step == Step.CHANGE_POWER) {
						findLink(mote, (step.link.getDestination())).setPower(step.value);
					} else if (step.step == Step.CHANGE_DIST) {
						findLink(mote, (step.link.getDestination())).setDistribution(step.value);
					}
				}
			}
			if (addMote)
				motesEffected.add(mote);
		}
		List<LinkSettings> newSettings;

		System.out.println("Adaptations:");
		for (Mote mote : motesEffected) {
			printMote(mote);
			newSettings = new LinkedList<LinkSettings>();
			for (Link link : mote.getLinks()) {
				newSettings.add(newLinkSettings(mote.getMoteId(), link.getDestination(), link.getPower(),
						link.getDistribution(), 0));
			}
			effector.setMoteSettings(mote.getMoteId(), newSettings);
		}
		steps.clear();
		System.out.println("Executor finished:" + System.currentTimeMillis());
	}

	Link findLink(Mote mote, int dest) {
		for (Link link : mote.getLinks()) {
			if (link.getDestination() == dest)
				return link;
		}
		throw new RuntimeException(String.format("Link %d --> %d not found", mote.getMoteId(), dest));
	}

	public LinkSettings newLinkSettings(int src, int dest, int power, int distribution, int sf) {
		LinkSettings settings = new LinkSettings();
		settings.setSrc(src);
		settings.setDest(dest);
		settings.setPowerSettings(power);
		settings.setDistributionFactor(distribution);
		settings.setSpreadingFactor(sf);
		return settings;
	}

	void printMote(Mote mote) {
		System.out.println(String.format("MoteId: %d, BatteryRemaining: %f, Links:%s", mote.getMoteId(),
				mote.getEnergyLevel(), getLinkString(mote.getLinks())));
	}

	String getLinkString(List<Link> links) {
		StringBuilder strBuilder = new StringBuilder();
		for (Link link : links) {
			strBuilder.append(String.format("[Dest: %d, Power:%d, DistributionFactor:%d]", link.getDestination(),
					link.getPower(), link.getDistribution()));
		}
		return strBuilder.toString();
	}
}