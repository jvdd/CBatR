package mapek;

public class Goals {

	static public boolean optimizationGoalEnergyCosnumption(AdaptationOption bestAdaptationOption,
			AdaptationOption adaptationOption) {
		if (bestAdaptationOption == null && adaptationOption != null)
			return true;
		return adaptationOption.verificResults.energyConsumption < bestAdaptationOption.verificResults.energyConsumption;
	}

	static public boolean satisfyGoalPacketLoss(AdaptationOption adaptationOption) {
		return adaptationOption.verificResults.packetLoss < 0.1;
	}

	static public boolean optimizationGoalPacketLoss(AdaptationOption bestAdaptationOption,
			AdaptationOption adaptationOption) {
		return adaptationOption.verificResults.packetLoss <= bestAdaptationOption.verificResults.packetLoss;
	}
}
