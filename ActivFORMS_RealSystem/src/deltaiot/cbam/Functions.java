package deltaiot.cbam;

public class Functions {

	/**
	 * Class containing the utility-response curves of the 2
	 * quality attributes investigated until now:
	 * 		- Energy Consumption
	 * 		- Packet Loss
	 * 
	 * Different versions of these utility curves can be selected
	 * by adding the according version numbers as parameters in the
	 * main method:
	 * [x, y] (with x version for energy utility and y version for packet loss utility)
	 */
	
	private static double energyConsumptionUtilityV0 (double energyConsumption) {
		double utility = 100;
		if (13.05 < energyConsumption && energyConsumption <= 14.2 ) { // 13.05 = mean of ActivFORMS
			utility -= (50 / 1.15) * (energyConsumption - 13.05);	   // 14.2 = mean of ActivFORMS w/ cost upstream energy consumption included
		}
		else if (14.2 < energyConsumption && energyConsumption <= 16.95) { // 16.95 = mean of reference model
			utility = 10;
		}
		else if (energyConsumption > 16.95) {
			utility = 0;
		}
		
		return utility;
	}
	
	
	private static double energyConsumptionUtilityV1 (double energyConsumption) {
		double utility = 100;
		if (12 < energyConsumption && energyConsumption <= 13.2 ) { 
			utility -= (50 / 1.2) * (energyConsumption - 12);
		}
		else if (13.2 < energyConsumption && energyConsumption <= 15) { // 16.95 = mean of reference model
			utility = 10;
		}
		else {
			utility = 0;
		}
		
		return utility;
	}
	
	/*
	public static double getEnergyConsumpUtility(double energyConsumption) {
		double utility;
		int version = RunStatisticResults.getUtilityVersions().get(0); // Index 0 for energy consump
		switch(version) {
			case 0 : utility = energyConsumptionUtilityV0(energyConsumption);
					 break;
			case 1 : utility = energyConsumptionUtilityV1(energyConsumption);
					 break;
			default: utility = energyConsumptionUtilityV0(energyConsumption);
					 break;
		}
		
		return utility;
	}
	
	
	private static double packetLossUtilityV0 (double packetLoss) {
		double utility = 100;
		if (packetLoss <= 0.05) {
			utility -= 400 * packetLoss;
		}
		else if (utility <= 10) {
			utility = 80;
			utility -= 1600 * (packetLoss - 0.05);
		}
		else {
			utility = 0;
		}
		
		return utility;
	}
	
	
	public static double getPacketLossUtility(double packetLoss) {
		double utility;
		int version = RunStatisticResults.getUtilityVersions().get(1); // Index 1 for packet loss
		switch(version) {
			case 0 : utility = packetLossUtilityV0(packetLoss);
					 break;
			default: utility = packetLossUtilityV0(packetLoss);
					 break;
		}
		
		return utility;
	}
	*/
}
