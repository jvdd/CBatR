package deltaiot.cbam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import deltaiot.types.DeltaIoT;
import deltaiot.types.Link;
import deltaiot.types.Mote;

public class AdaptationOptionsCollector {
	
	/** 
	 * Class keeping track of all adaptation options. 
	 * Each adaptation option is defined by a special formatted string.
	 * 		-> Method for converting this string to DeltaIoT net
	 * The current configuration is also stored and updated.
	 * 
	 * In this class the cost benefit analysis is implemented.
	 * Here you can also find a function to initialize your file
	 * and write data to your file.
	 */

	/*
	private static List<AdaptationOption> adaptationOptions = new ArrayList<AdaptationOption>();
	private static AdaptationOption currentConfiguration = null;
	private static boolean generationCompleted = false;
	private static AdaptationOption bestAdaptationOption = null;
	
	public static List<AdaptationOption> getAdaptationOptions(){
		return adaptationOptions;
	}
	
	public static void addAdaptationOption(AdaptationOption adaptOption) {
		if (generationCompleted) { // TODO: nagaan of dit niet door multithreading fout kan gaan
			//System.out.println("Previous Generation Processed" + adaptationOptions.size() +"   -> AdaptationOptions emptied");
			adaptationOptions = new ArrayList<AdaptationOption>();
			generationCompleted = false;
		}
		adaptationOptions.add(adaptOption);
	}
	
	public static void generationCompleted() {
		generationCompleted = true;
	}
	
	// The actual CBAM analysis happens in this function
	public static void analyzeAdaptationOptions() {
		double benefit, cost, vfc;
		double bestVFC = -Double.MAX_VALUE, bestBenefit = 0, bestCost = 0;
		AdaptationOption bestOption = null; 
		
		List<Integer> energyVersions = RunStatisticResults.getEnergyVersions();
		Map<Integer, double []> energyVersionValues = new HashMap<>(); 
		String energyVersionsValues = "";
		int nbLinksAdapted = 0, nbUniqueLinksAdapted = 0;
		
		for (AdaptationOption option : adaptationOptions) {
			benefit = calculateBenefit(option);
			cost = calculateCost(option);
			vfc = benefit / (1 + cost);
			//System.out.println("Benefit: " + benefit + "  Cost: " + cost + "  VFC: " + vfc);
			if (vfc > bestVFC) {
				bestBenefit = benefit;
				bestCost = cost;
				bestVFC = vfc;
				bestOption = option;
				
				energyVersionsValues = "";
				for (int energyVersion : energyVersions) {
					double totEnergyVersion = option.getEnergyConsumption() + AdaptationsCollector.getAdaptationsEnergyConsumption(energyVersion);
					energyVersionsValues += String.format("%24s, %24s, ", AdaptationsCollector.getAdaptationsEnergyConsumption(energyVersion), totEnergyVersion); 
					double energyValues [] = {AdaptationsCollector.getAdaptationsEnergyConsumption(energyVersion), totEnergyVersion};
					energyVersionValues.put(energyVersion, energyValues);
				}
				nbLinksAdapted = AdaptationsCollector.getAdaptations().size();
				nbUniqueLinksAdapted = AdaptationsCollector.getUniqueLinks().size();
			}
			AdaptationsCollector.feedBackLoopCompleted(); // TODO: check if this does not result in race conditions
		}
		System.out.println("Benefit: " + bestBenefit + "  Cost: " + bestCost + "  VFC: " + bestVFC);
		bestAdaptationOption = bestOption;
		
		//writeCBAMToFile(bestOption, bestBenefit, bestCost, bestVFC, energyVersionValues, energyVersionsValues, nbLinksAdapted, nbUniqueLinksAdapted);
		bestOptionInUse(); // TODO: DELETE THIS AND PLACE THIS SOMEWHERE ELSE
	}
	
	private static void bestOptionInUse() {
		currentConfiguration = bestAdaptationOption;
	}
	
	public static AdaptationOption getBestAdaptationOption() {
		return bestAdaptationOption;
	}
	
	private static double calculateBenefit(AdaptationOption option) {
		double weightEnergyConsump = RunStatisticResults.getWeights().get(0) / 100.0;
		double weightPacketLoss = RunStatisticResults.getWeights().get(1) / 100.0;
		double energyConsumpUtilityOption = Functions.getEnergyConsumpUtility(option.getEnergyConsumption()); 
		double packetLossUtilityOption = Functions.getPacketLossUtility(option.getPacketLoss());
		double energyConsumpUtilityCurrent, packetLossUtilityCurrent;
		if (currentConfiguration != null) { // In run 1 the current values will be 0
			energyConsumpUtilityCurrent = Functions.getEnergyConsumpUtility(currentConfiguration.getEnergyConsumption());
			packetLossUtilityCurrent = Functions.getPacketLossUtility(currentConfiguration.getPacketLoss());
		} 
		else {
			System.out.println("Current Config is NULL");
			energyConsumpUtilityCurrent = Functions.getEnergyConsumpUtility(16.5);
			packetLossUtilityCurrent = Functions.getPacketLossUtility(0.0);
		}

		double netUtilityEnergyConsump = energyConsumpUtilityOption - energyConsumpUtilityCurrent; // U_expected - U_current
		double netUtilityPacketLoss = packetLossUtilityOption - packetLossUtilityCurrent;
		
		double benefit = (weightEnergyConsump * netUtilityEnergyConsump) + (weightPacketLoss * netUtilityPacketLoss);
		
		return benefit;
	}
	
	private static double calculateCost(AdaptationOption option) throws IllegalArgumentException {
		double cost;
		HashMap<Integer, Mote> adaptationMotes = fromModelStringToDeltaIot(option.getAdaptationOption()).getMotes();
		if (currentConfiguration.getAdaptationOption() != null) {
			// TODO: uitvogelen hoe weten waar welke aanpassing doorvoeren
			HashMap<Integer, Mote> currentMotes = fromModelStringToDeltaIot(currentConfiguration.getAdaptationOption()).getMotes();
		
			for(Integer key : currentMotes.keySet()) {
				if (currentMotes.get(key).getMoteId() != adaptationMotes.get(key).getMoteId()) {
					throw new IllegalArgumentException("Current configuration and adaptation options are differently hashed");
				}
	
				List<Link> adaptLinks = adaptationMotes.get(key).getLinks();
				List<Link> currentLinks = currentMotes.get(key).getLinks();
				
				for(int i = 0; i < currentMotes.get(key).getParents(); i++) {
					Link adaptLink = adaptLinks.get(i);
					Link currentLink = currentLinks.get(i);
					
					if (adaptLink.getSource() == 0) {
						throw new IllegalArgumentException("Parents argument does not match nb of links");
					}
					if (currentLink.getSource() != adaptLink.getSource() || currentLink.getDestination() != adaptLink.getDestination()) {
						throw new IllegalArgumentException("Ordening on links is different");
					}
					
					if (currentLink.getPowerSetting() != adaptLink.getPowerSetting()) {
						Adaptation adaptation = new Adaptation(adaptLink, adaptLink.getPowerSetting(), Adaptation.AdaptationOptionType.POWERCHANGE);
						AdaptationsCollector.addAdaptation(adaptation);
					}
					if (currentLink.getDistribution() != adaptLink.getDistribution())
					{
						Adaptation adaptation = new Adaptation(adaptLink, adaptLink.getDistribution(), Adaptation.AdaptationOptionType.DISTRIBUTIONCHANGE);
						AdaptationsCollector.addAdaptation(adaptation);
					}
				}				
			}
			
			cost = AdaptationsCollector.getAdaptationsEnergyConsumption(RunStatisticResults.getEnergyVersions().get(0));
		}
		else {
			for(Integer key : adaptationMotes.keySet()) {
				List<Link> adaptLinks = adaptationMotes.get(key).getLinks();
				
				for(int i = 0; i < adaptationMotes.get(key).getParents(); i++) {
					Link adaptLink = adaptLinks.get(i);
					if (adaptLink.getSource() == 0) {
						throw new IllegalArgumentException("Parents argument does not match nb of links");
					}
					
					if (adaptLink.getPowerSetting() != 15) {
						Adaptation adaptation = new Adaptation(adaptLink, adaptLink.getPowerSetting(), Adaptation.AdaptationOptionType.POWERCHANGE);
						AdaptationsCollector.addAdaptation(adaptation);
					}
					if (adaptLink.getDistribution() != (100 / adaptationMotes.get(key).getParents()))
					{
						Adaptation adaptation = new Adaptation(adaptLink, adaptLink.getDistribution(), Adaptation.AdaptationOptionType.DISTRIBUTIONCHANGE);
						AdaptationsCollector.addAdaptation(adaptation);
					}
				}				
			}
			cost = AdaptationsCollector.getAdaptationsEnergyConsumption(RunStatisticResults.getEnergyVersions().get(0));
		}
		
		return cost;
	}
	
	public static DeltaIoT fromModelStringToDeltaIot(String adaptationStr) {
		String [] adaptationSplitted = adaptationStr.split("\n");
		int moteid, load, battery, parents, queueSize;
		int source, destination, energyLevel, distribution;
		Link link;
		Map<Integer, Mote> motes = new HashMap<Integer, Mote>();
		for(int i = 2; i < adaptationSplitted.length - 1; i ++) {
			List<Link> links = new LinkedList<Link>();
			String moteStr = adaptationSplitted[i].replaceAll(" ", "");
			String [] moteSplitted = moteStr.split(",");
			moteid = Integer.parseInt(moteSplitted[0].replaceAll("\\{", ""));
			load = Integer.parseInt(moteSplitted[1]);
			battery = Integer.parseInt(moteSplitted[2]);
			parents = Integer.parseInt(moteSplitted[3]);
			queueSize = Integer.parseInt(moteSplitted[4]);
			
			source = Integer.parseInt(moteSplitted[5].replaceAll("\\{", ""));
			destination = Integer.parseInt(moteSplitted[6]);
			energyLevel = Integer.parseInt(moteSplitted[7]);
			distribution = Integer.parseInt(moteSplitted[8].replaceAll("\\}", ""));
			link = new Link(source, destination, energyLevel, distribution);		
			links.add(link);
				
			source = Integer.parseInt(moteSplitted[9].replaceAll("\\{", ""));
			destination = Integer.parseInt(moteSplitted[10]);
			energyLevel = Integer.parseInt(moteSplitted[11]);
			distribution = Integer.parseInt(moteSplitted[12].replaceAll("\\}", ""));
			link = new Link(source, destination, energyLevel, distribution);
			links.add(link);
			
			Mote mote = new Mote(moteid, load, battery, parents, queueSize, links);
			motes.put(moteid, mote);
		}		
		
		DeltaIoT deltaIoT = new DeltaIoT(motes);
		return deltaIoT;
	}
	
	public static void initializeCBAMFile() {
		List<Integer> utilityVersions = RunStatisticResults.getUtilityVersions();
		List<Integer> weights = RunStatisticResults.getWeights();
		List<Integer> energyVersions = RunStatisticResults.getEnergyVersions();
		int timeSlots = RunStatisticResults.getTimeSlots();
		String path = RunStatisticResults.getPath();
		
		try {
			File file = new File(path);
			file.createNewFile(); // will do nothing if file already exists
			System.out.println("Writing to file at: " + path);
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			Files.write(Paths.get(path), String.format("Creation date and time: %12s %10s", dateFormat.format(date), timeFormat.format(date)).getBytes(), StandardOpenOption.APPEND);
			Files.write(Paths.get(path), String.format("\r\nTIMESLOTS: %s", timeSlots).getBytes(), StandardOpenOption.APPEND);
			Files.write(Paths.get(path), String.format("\r\nEnergy versions: %s   (%s is used for calculating cost in RCBAM)", energyVersions, energyVersions.get(0)).getBytes(), StandardOpenOption.APPEND);
			Files.write(Paths.get(path), String.format("\r\nUtility versions: %s (Index: 0 = energy consumption, 1 = packet loss)", utilityVersions).getBytes(), StandardOpenOption.APPEND);
			Files.write(Paths.get(path), String.format("\r\nWeights: %s (Index: 0 = energy consumption, 1 = packet loss", weights).getBytes(), StandardOpenOption.APPEND);
			
			if (RunStatisticResults.getExtraArguments() != null && 
					RunStatisticResults.getExtraArguments().length > 0) {
				for(String extraArg : RunStatisticResults.getExtraArguments()) {
					Files.write(Paths.get(path), String.format("Extra argument: %s", extraArg).getBytes(), StandardOpenOption.APPEND);
				}
			}
			
			String energyVersionsExtension = "";
			for (int energyVersion : energyVersions) { 
				energyVersionsExtension += String.format("%24s, %24s, ", "AdaptEnergyConsumpV" + energyVersion, "TotEnergyConsumpV" + energyVersion); 
			}		
			
			Files.write(Paths.get(path), String.format("\r\n%24s,%s%22s, %10s, %16s, %22s, %22s, %22s, %12s, %10s,", "EnergyConsumption", energyVersionsExtension, "PacketLoss", "nbAdapts", "nbUniqueAdapts", "Benefit", "Cost", "VFC", "Date", "Time").getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			System.out.println (e.toString());
			System.out.println("Could not find file " + path);
		}
	}
	

	private static void writeCBAMToFile(AdaptationOption option, double benefit, double cost, double vfc, Map<Integer, double []> energyVersionValues, String energyVersionsValues, int nbLinksAdapted, int nbUniqueLinksAdapted) {
		String path = RunStatisticResults.getPath();
		
		System.out.println("Energy Consumption: " + option.getEnergyConsumption());
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			Files.write(Paths.get(path), String.format("\r\n%24s,%s%22s, %10s, %16s, %22s, %22s, %22s, %12s, %10s,", option.getEnergyConsumption(), energyVersionsValues, option.getPacketLoss(), nbLinksAdapted, nbUniqueLinksAdapted, benefit, cost, vfc, dateFormat.format(date), timeFormat.format(date)).getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			System.out.println (e.toString());
			System.out.println("Could not find file " + path);
		}
		System.out.println("Packet loss: " + option.getPacketLoss());
		System.out.println("Number of links adapted: " + nbLinksAdapted);
		System.out.println("Number of unique links adapted: " + nbUniqueLinksAdapted);
		for (int energyVersion : energyVersionValues.keySet()) {
			System.out.println("Energy V" + energyVersion + "   Adaptation Energy Consumption = " + energyVersionValues.get(energyVersion)[0] + "    Total Energy Consumption = " + energyVersionValues.get(energyVersion)[1]);
		}
		System.out.println();
	}
	
	public static void initializeCurrentConfiguration(double energyConsumption, double packetLoss) {

		AdaptationOption currentOption = new AdaptationOption(null);
		currentOption.setEnergyConsumption(energyConsumption);
		currentOption.setPacketLoss(packetLoss);
		
		currentConfiguration = currentOption;
	}
	
	public static AdaptationOption getCurrentConfiguration() {
		return currentConfiguration;
	}
	*/
}
