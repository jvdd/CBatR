package deltaiot.cbam;

public class AdaptationOption {
	
	/**
	 * Class for an adaptation option, which is actually a complete setting
	 * for the IoT network (for each mote and its links are the settings given)
	 * Including even the overall energy consumption and packet loss.
	 * The whole setting is saved in a special formatted string: (See example underneath)
	 * ManagedSystem deltaIoT = {{
     * {moteid, load, battery, parents, {{source, destination, energy level, distribution}, {source, destination, energy level, distribution}}}
	 * 
	 * 
	 * ManagedSystem deltaIoT = {{
	 * {2, 10, 11878, 1, 0,{{2, 4, 15, 100}, {0,0,0,0}}},
	 * {3, 10, 11877, 1, 0,{{3, 1, 1, 100}, {0,0,0,0}}},
	 * {4, 10, 11877, 1, 0,{{4, 1, 1, 100}, {0,0,0,0}}},
	 * {5, 10, 11878, 1, 0,{{5, 9, 1, 100}, {0,0,0,0}}},
	 * {6, 10, 11878, 1, 0,{{6, 4, 15, 100}, {0,0,0,0}}},
	 * {7, 10, 11877, 2, 0,{{7, 2, 1, 0}, {7, 3, 1, 100}}},
	 * {8, 10, 11879, 1, 0,{{8, 1, 1, 100}, {0,0,0,0}}},
	 * {9, 10, 11878, 1, 0,{{9, 1, 1, 100}, {0,0,0,0}}},
	 * {10, 10, 11880, 2, 0,{{10, 6, 1, 50}, {10, 5, 1, 50}}},
	 * {11, 10, 11878, 1, 0,{{11, 7, 1, 100}, {0,0,0,0}}},
	 * {12, 10, 11877, 2, 0,{{12, 7, 15, 50}, {12, 3, 15, 50}}},
	 * {13, 10, 11880, 1, 0,{{13, 11, 1, 100}, {0,0,0,0}}},
	 * {14, 10, 11879, 1, 0,{{14, 12, 1, 100}, {0,0,0,0}}},
	 * {15, 10, 11879, 1, 0,{{15, 12, 1, 100}, {0,0,0,0}}}}};
	 */
	
	private String adaptationOption;
	private double energyConsumption;
	private double packetLoss;
	
	public AdaptationOption(String adaptationOption) {
		this.adaptationOption = adaptationOption;
	}
	
	public void setEnergyConsumption(double energyConsumption) {
		this.energyConsumption = energyConsumption;
	}
	
	public void setPacketLoss(double packetLoss) {
		this.packetLoss = packetLoss;
	}
	
	public double getEnergyConsumption() {
		return energyConsumption;
	}
	
	public double getPacketLoss() {
		return packetLoss;
	}
	
	public String getAdaptationOption() {
		return adaptationOption;
	}

}
