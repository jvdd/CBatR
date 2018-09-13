package deltaiot.cbam;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import deltaiot.activforms.ActivFORMSDeploy;
import deltaiot.activforms.ProbeConnector;
import deltaiot.services.Mote;
import deltaiot.services.QoS;

public class RunStatisticResults {
	
	private static final int TIMESLOTS = 1;
	private static List<Integer> energyVersions;
	//private static List<Integer> utilityVersions; // 0: energyConsump, 1: packetLoss
	//private static List<Integer> weights; // 0: energyConsump, 1: packetLoss     ex. [50, 50] (sum = 100, weights evenly distributed)
	private static String path;
	private static int projectVersion;
	private static int currentTestExecuted = 1;
	private static String [] extraArguments = null;

	public static void main(String[] args) {
		
		int projectVersionTest = 0;
		for (String folder : System.getProperty("user.dir").split("\\\\")) { if (folder.contains("Project_v")) {projectVersionTest = Integer.parseInt(folder.substring(folder.length() - 1));}}
		System.out.println("\n\n-------- Project version: " + projectVersionTest + " --------");
		
		//String partialPath = String.format("C:\\Users\\Jeroen\\Documents\\BachelorProef\\Resultaten\\Project_v%s\\", projectVersion);
		String partialPath = String.format("C:\\Users\\mama\\Desktop\\BachelorProef_Resultaten\\Project_v%s\\", projectVersionTest);
		 
		List<Integer> energyVersionsTest = stringToIntList(args[0]);
		//List<Integer> utilityVersionsTest = stringToIntList(args[1]);
		//List<Integer> weightsTest = stringToIntList(args[2]);
		//int timeSlots = Integer.parseInt(args[3]);
		String extraInfoName = args[1];
		String extraInfoFile = args[2];
		if (args.length > 3) {
			extraArguments = Arrays.copyOfRange(args, 3, args.length);
		}
		
		try {
			executeStatisticTest(partialPath, projectVersionTest, energyVersionsTest, extraInfoName, extraInfoFile, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		waitForPreviousToComplete();
		System.out.println("Completed");
		System.exit(0);
		
		/* 		
		executeStatisticTest(partialPath, 5, 0, 1, "EnergyConsumpSendingSF=8", "Energy Consumption Sending in Upstream: spreading factor = 8 = DomainConstants.getPacketDuplicationSfTime() which is equal to sfTimes.get(8);", args);
		waitForPreviousToComplete();
		System.out.println("\n\n1ste Completed");
		executeStatisticTest(partialPath, 5, 0, 2, "EnergyConsumpSendingSF=8", "Energy Consumption Sending in Upstream: spreading factor = 8 = DomainConstants.getPacketDuplicationSfTime() which is equal to sfTimes.get(8);", args);
		waitForPreviousToComplete();
		executeStatisticTest(partialPath, 5, 1, 1, "EnergyConsumpSendingSF=8", "Energy Consumption Sending in Upstream: spreading factor = 8 = DomainConstants.getPacketDuplicationSfTime() which is equal to sfTimes.get(8);", args);
		waitForPreviousToComplete();
		executeStatisticTest(partialPath, 5, 1, 2, "EnergyConsumpSendingSF=8", "Energy Consumption Sending in Upstream: spreading factor = 8 = DomainConstants.getPacketDuplicationSfTime() which is equal to sfTimes.get(8);", args);
		waitForPreviousToComplete();
		
		
		executeStatisticTest(partialPath, 5, 0, 1, "EnergyConsumpSendingSF=12", "Energy Consumption Sending in Upstream: spreading factor = 12 (maximum)", args);
		executeStatisticTest(partialPath, 5, 0, 1, "EnergyConsumpSendingSF=12", "Energy Consumption Sending in Upstream: spreading factor = 12 (maximum)", args);
		executeStatisticTest(partialPath, 5, 1, 2, "EnergyConsumpSendingSF=12", "Energy Consumption Sending in Upstream: spreading factor = 12 (maximum)", args);
		executeStatisticTest(partialPath, 5, 1, 2, "EnergyConsumpSendingSF=12", "Energy Consumption Sending in Upstream: spreading factor = 12 (maximum)", args);
		*/	
		
		// PLAY SONG WHEN FINISHED :)
		/*
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.browse(new URI("https://www.youtube.com/watch?v=IhchfhxvPKI"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	private static List<Integer> stringToIntList(String listStr) {
		String[] strList = listStr.replaceFirst("\\[", "").replaceFirst("\\]", "").replaceAll(" ", "").split(",");
		List<Integer> intList = Arrays.asList(strList).stream().map(Integer::parseInt).collect(Collectors.toList());
		
		return intList;
	}

	
	public static void waitForPreviousToComplete() {
		while (ProbeConnector.FINISHED != currentTestExecuted) {
			Thread thread = Thread.currentThread();
			try {
				thread.sleep(60000); // wait for 1 min is relative short to the 55 min time to generate 96 simulated cycles
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void executeStatisticTest(String partialPath, int projectVersionTest, List<Integer> energyVersionsTest, String extraInfoName, String extraInfoFile, String[] args) {
		currentTestExecuted += 1;

		energyVersions = energyVersionsTest;
		projectVersion = projectVersionTest;
		if (extraInfoName.length() > 0) { extraInfoName = " - " + extraInfoName; }
		//String fileEnd = String.format("Project_v%s - Energy_v%s - ActivFormsWITHUpstrEnergyConsump_TIMESLOTS=%s%s.txt", projectVersionTest, energyVersions, timeSlots, extraInfoName);
		String fileEnd = String.format("Project_v%s CBAM - Energy_v%s - TIMESLOTS=%s%s.txt", projectVersion, energyVersions.get(0), TIMESLOTS, extraInfoName);
		path = partialPath + fileEnd;

		System.out.println("TIMESLOTS: " + TIMESLOTS);
		System.out.println("Energy Versions: " + energyVersions + "   (" + energyVersions.get(0) + " is used for calculating cost in RCBAM)");
		System.out.println("Extra Info: " + extraInfoFile);
				
		if (extraArguments != null && extraArguments.length > 0) {
			for (String argument : extraArguments) {
				System.out.println("Extra argumen: " + argument);
			}
		}
				
		try {
			File file = new File(path);
			file.createNewFile(); // will do nothing if file already exists
			System.out.println("Writing to file at: " + path);
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			Files.write(Paths.get(path), String.format("Creation date and time: %12s %10s", dateFormat.format(date), timeFormat.format(date)).getBytes(), StandardOpenOption.APPEND);
			Files.write(Paths.get(path), String.format("\r\nTIMESLOTS: %s", TIMESLOTS).getBytes(), StandardOpenOption.APPEND);
			Files.write(Paths.get(path), String.format("\r\nEnergy Consumption versions: %s   (%s is used for calculating cost in RCBAM)", energyVersions, energyVersions.get(0)).getBytes(), StandardOpenOption.APPEND);
			if (extraInfoFile.length() > 0) {
				Files.write(Paths.get(path), String.format("\r\n" + extraInfoFile).getBytes(), StandardOpenOption.APPEND);
			}
			if (extraArguments != null && extraArguments.length > 0) {
				for (String argument : extraArguments) {
					Files.write(Paths.get(path), String.format("\r\nExtra argument: " + argument).getBytes(), StandardOpenOption.APPEND);
				}
			}
			String energyVersionsExtension = "";
			for (int energyVersion : energyVersions) { 
				energyVersionsExtension += String.format("%24s, %24s, ", "AdaptEnergyConsumpV" + energyVersion, "TotEnergyConsumpV" + energyVersion); 
			}
			Files.write(Paths.get(path), String.format("\r\n%24s, %s%22s, %22s, %22s, %8s, %10s, %16s, %12s, %10s,", "EnergyConsumption", energyVersionsExtension, "PacketLoss", "Latency", "QueueLoss", "Sent", "nbAdapts", "nbUniqueAdapts", "Date", "Time").getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			System.out.println (e.toString());
			System.out.println("Could not find file " + path);
		}
		
		//AdaptationOptionsCollector.initializeCBAMFile();
		ActivFORMSDeploy.main(args);
	}
	
	
	public static void writeDataToFile(QoS qos) { // TODO: Skipped in checking code (since not used for RCBAM)
		Map<Integer, double []> energyVersionValues = new HashMap<>(); 
		String energyVersionsValues = "";
		for (int energyVersion : energyVersions) {
			double totEnergyVersion = qos.getEnergyConsumption() + AdaptationsCollector.getAdaptationsEnergyConsumption(energyVersion);
			energyVersionsValues += String.format("%24s, %24s, ", AdaptationsCollector.getAdaptationsEnergyConsumption(energyVersion), totEnergyVersion); 
			double energyValues [] = {AdaptationsCollector.getAdaptationsEnergyConsumption(energyVersion), totEnergyVersion};
			energyVersionValues.put(energyVersion, energyValues);
		}
		int nbOfLinksAdapted = AdaptationsCollector.getAdaptations().size();
		int uniqueNbOfLinksAdapted = AdaptationsCollector.getUniqueLinks().size();
		
		String path = RunStatisticResults.getPath();
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			//Files.write(Paths.get(path), "".getBytes(), StandardOpenOption.APPEND); // Niks wegschrijven
			Files.write(Paths.get(path), String.format("\r\n%24s,%s%22s, %22s, %22s, %8s, %16s, %12s, %12s, %10s,", qos.getEnergyConsumption(), energyVersionsValues, qos.getPacketLoss(), qos.getLatency(), qos.getQueueLoss(), qos.getSent(), nbOfLinksAdapted, uniqueNbOfLinksAdapted, dateFormat.format(date), timeFormat.format(date)).getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			System.out.println (e.toString());
			System.out.println("Could not find file " + path);
		}
		System.out.println("Energy Consumption: " + qos.getEnergyConsumption());
		System.out.println("Number of adaptations: " + nbOfLinksAdapted);
		System.out.println("Number of unique links adapted: " + uniqueNbOfLinksAdapted);
		System.out.println("Packet loss: " + qos.getPacketLoss());
		for (int energyVersion : energyVersionValues.keySet()) {
			System.out.println("Energy V" + energyVersion + "   Adaptation Energy Consumption = " + energyVersionValues.get(energyVersion)[0] + "    Total Energy Consumption = " + energyVersionValues.get(energyVersion)[1]);
		}
		System.out.println();
	}
	


	public static int getTimeSlots() {
		return TIMESLOTS;
	}
	
	public static String getPath() {
		return path;
	}
	
	public static List<Integer> getEnergyVersions(){ // 0: energy consumption used for the cost calculation in RCBAM
		return energyVersions;
	}
	
	public static int getProjectVersion() {
		return projectVersion;
	}
	
	public static int getCurrentTestExecuted() {
		return currentTestExecuted;
	}
	
	public static String [] getExtraArguments() {
		return extraArguments;
	}
	
}
