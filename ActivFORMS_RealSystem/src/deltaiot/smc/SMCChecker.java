package deltaiot.smc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import activforms.types.Int;
import deltaiot.activforms.ExecuteCommand;
import deltaiot.cbam.ProcessError;
import deltaiot.types.*;

public class SMCChecker {

	// static String modelPower, modelPacketLoss;
	//
	String configPath;
	public static String command = Paths.get(System.getProperty("user.dir"))
			+ "/uppaal-verifyta/verifyta -a %f -E %f -u %s";
	// public static int relativeSEM = 10;
	// public static int simulationsSEM5 = 100;
	// public static int simulationsSEM10 = 25;
	// public static double alpha = 0.01;
	// public static double epsilon = 0.01;
	//
	// public static Path smcModelPathPower =
	// Paths.get(System.getProperty("user.dir")
	// + "/models/QueueLoss.xml");
	// public static Path smcModelPathPacketLoss =
	// Paths.get(System.getProperty("user.dir")
	// + "/models/PacketLoss-SMC.xml");

	SMCModelLoader modelLoader;
	List<SMCModel> models;

	public SMCChecker(String configPath) {
		this.configPath = configPath;
		modelLoader = new SMCModelLoader(configPath);
	}

	ExecutorService cachedPool = Executors.newFixedThreadPool(2);

	public List<SMCModel> getModels() {
		return models;
	}

	/*
	 * Reference:
	 * http://www.mkyong.com/java/how-to-execute-shell-command-from-java/
	 */
	private static String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {

			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			if (output.length() == 0) {
				while ((line = errorReader.readLine()) != null) {
					output.append(line + "\n");
				}
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
			e.printStackTrace(ProcessError.getErrorPrintStream());
		}

		return output.toString();
	}

	public static String getCommand(String modelPath, double alpha, double epsilon) {
		String cmd = String.format(command, alpha, epsilon, modelPath);
		// System.out.println(cmd);
		return cmd;
	}

	static int getRuns(String string) {
		if (string.contains(" runs)")) {
			int end = string.indexOf(" runs)");
			int start = string.lastIndexOf("(", end) + 1;
			String runs = string.substring(start, end);
			return Integer.parseInt(runs);
		} else {
			throw new RuntimeException("Couldn't parse probability");
		}
	}

	/*
	 * This method parse the following line -- States explored : 147698 states
	 */
	final static String STATES_EXPLORED = "-- States explored : ";

	static long getStatesExplored(String string) {

		int start = string.indexOf(STATES_EXPLORED);
		if (start != -1) {
			start += STATES_EXPLORED.length();
			int end = string.indexOf(" states", start);
			String runs = string.substring(start, end);
			return Long.parseLong(runs);
		} else {
			throw new RuntimeException("Couldn't parse states explored");
		}
	}

	/*
	 * This method parse the following line -- CPU user time used : 571 ms
	 */
	final static String CPU_TIME = "-- CPU user time used : ";

	static long getCPUtime(String string) {

		int start = string.indexOf(CPU_TIME);
		if (start != -1) {
			start += CPU_TIME.length();
			int end = string.indexOf(" ms", start);
			String runs = string.substring(start, end);
			return Long.parseLong(runs);
		} else {
			throw new RuntimeException("Couldn't parse CPU time");
		}
	}

	/*
	 * This method parse the following line -- Resident memory used : 6164 KiB
	 */
	final static String RESIDENT_MEM = "-- Resident memory used : ";

	static long getResidentMem(String string) {

		int start = string.indexOf(RESIDENT_MEM);
		if (start != -1) {
			start += RESIDENT_MEM.length();
			int end = string.indexOf(" KiB", start);
			String runs = string.substring(start, end);
			return Long.parseLong(runs);
		} else {
			throw new RuntimeException("Couldn't parse resident memory");
		}
	}

	static double getSimulatedValue(String string) {
		// find the last pair
		int startingIndex = string.lastIndexOf("(") + 1;
		int endIndex = string.lastIndexOf(")");
		String pair = string.substring(startingIndex, endIndex);
		String[] splitPair = pair.split(",");
		double value = Double.parseDouble(splitPair[1]);
		return value;
	}

	static String changeCAO(String file, String cao, String env) {

		String startText = "//&lt;Configuration&gt;";
		String endText = "//&lt;/Configuration&gt;";
		String newText = String.format("%s\n%s ", cao, env);
		//System.out.println(newText);
		file = changeText(file, startText, endText, newText);
		return file;
	}

	static String changeCAO(String file, HashMap cao) {

		String startText = "//&lt;Configuration&amp;gt;\n";
		String endText = "//&amp;lt;/Configuration&amp;gt;";

		String newText = String.format("\nconst ManagedSystem deltaIoT =%s;\n", cao);
		file = changeText(file, startText, endText, newText);
		return file;
	}

	static String changeText(String file, String startString, String endString, String newText) {

		if (file.contains(startString)) {
			int startIndex = file.indexOf(startString) + startString.length();
			int endIndex = file.indexOf(endString);
			String oldText = file.substring(startIndex, endIndex);
			file = file.replace(oldText, newText);
			return file;
		} else {
			throw new RuntimeException("StartString:" + startString + " not found!");
		}
	}

	public void checkCAO(String adaptationOption, String environment, HashMap<String, Int> verificationResults) {
		try {

			setInitialData(adaptationOption, environment, verificationResults);

			LinkedList<ExecuteCommand> commands = new LinkedList<ExecuteCommand>();

			for (SMCModel model : models) {
				String command = getCommand(model.getPath(), model.alpha, model.epsilon);
				commands.add(new ExecuteCommand(command, model));
			}
			String[] values;
			double value = 0;

			List<Future<String>> results = cachedPool.invokeAll(commands);

			// collecting results

			for (ExecuteCommand command : commands) {
 
				values = command.getResult().split("Verifying formula ");
				value = 0;
				if (command.getModel().getType() == ModelType.SIMULATION) {
					value = getSimulatedValue(values[1]);
				} else if (command.getModel().getType() == ModelType.PROBABILITY) {
					value = getProbability(values[1]);
				}
				System.out.print(value + ",");
				verificationResults.get(command.getModel().getKey()).setValue((int)(value*100));
				//verificationResults.put(command.getModel().getKey(), (int)(value*100));
			}
			System.out.println();
		} catch (Exception ex) {
			ex.printStackTrace();
			ex.printStackTrace(ProcessError.getErrorPrintStream());
		}
	}

	public void setInitialData(String cao, String env, HashMap verificationResults) {
		models = new LinkedList<>();
		try {
			for (SMCModel model : modelLoader.loadModels()) {
				if (verificationResults.containsKey(model.getKey())) {
					String updatedModel = changeCAO(model.getModel(), cao, env);
					Files.write(Paths.get(model.getPath()), updatedModel.getBytes(Charset.defaultCharset()));
					models.add(model);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			e.printStackTrace(ProcessError.getErrorPrintStream());
		}
	}

	public static String setSimulations(String model, int simulations) {
		String str = "simulate 1[";
		if (model.contains(str)) {
			int start = model.lastIndexOf(str) + str.length();
			int end = model.indexOf("]", start);
			String s = model.substring(start, end);
			// int simulations = relativeSEM == 5 ? simulationsSEM5 :
			// simulationsSEM10;
			model = model.replace(s, "&lt;=" + simulations);
		}
		return model;
	}

	static double getProbability(String string) {
		String proability = getProbabilityBounds(string);
		String[] strBounds = proability.split(",");
		Double[] bounds = new Double[] { Double.parseDouble(strBounds[0]), Double.parseDouble(strBounds[1]) };

		double avg = ((bounds[0] + bounds[1]) / 2);
		return avg;
	}

	static String getProbabilityBounds(String string) {
		if (string.contains("Pr(<> ...) in [")) {
			int index = string.indexOf("Pr(<> ...) in [") + "Pr(<> ...) in [".length();
			String proability = string.substring(index, string.indexOf("]", index));

			return proability;
		} else {
			throw new RuntimeException("Couldn't parse probability");
		}
	}

	static void scalabilityTestPropertyPacketLoss() {
		try {
			for (int count = 5; count <= 9; count++) {
				Path path = Paths.get(System.getProperty("user.dir")
						+ "/scalability/properties/packetloss/PacketLoss-SMC" + count + ".xml");
				String cmd = getCommand(path.toString(), 0.10, 0.01);
				ExecuteCommand command = new ExecuteCommand(cmd);

				// collecting results
				double value = 0;
				long time;

				for (int i = 0; i < 100; i++) {
					String[] values = command.call().split("Verifying formula ");
					value = getProbability(values[1]);
					time = getCPUtime(values[1]);
					System.out.format("Packetloss: %d %d %d %f \n", i, count, time, value);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ex.printStackTrace(ProcessError.getErrorPrintStream());
		}
	}
	
	static void scalabilityTestPropertyEnergyConsumption() {
		try {
			for (int count = 5; count <= 9; count++) {
				Path path = Paths.get(System.getProperty("user.dir")
						+ "/scalability/properties/energyconsumption/EnergyConsumption" + count + ".xml");
				String cmd = getCommand(path.toString(), 0.10, 0.01);
				ExecuteCommand command = new ExecuteCommand(cmd);

				// collecting results
				double value = 0;
				long time;

				for (int i = 0; i < 100; i++) {
					String[] values = command.call().split("Verifying formula ");
					value = getSimulatedValue(values[1]);
					time = getCPUtime(values[1]);
					System.out.format("EnergyConsumption: %d %d %d %f \n", i, count, time, value);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ex.printStackTrace(ProcessError.getErrorPrintStream());
		}
	}

	public static void main(String[] args) {
		//scalabilityTestPropertyPacketLoss();
		scalabilityTestPropertyEnergyConsumption();
//		SMCChecker smc = new SMCChecker(System.getProperty("user.dir") + "/SMCConfig.properties");
//		SMCModelLoader modelLoader = new SMCModelLoader(smc.configPath);
//		List<SMCModel> models = modelLoader.loadModels();
//
//		try {
//
//			// LinkedList<ExecuteCommand> commands = new
//			// LinkedList<ExecuteCommand>();
//
//			// for (SMCModel model : models) {
//			// String command = getCommand(model.getPath(),
//			// model.alpha,model.epsilon);
//			// commands.add(new ExecuteCommand(command, model));
//			// }
//
//			// List<Future<String>> results = cachedPool.invokeAll(commands);
//			SMCModel model = models.get(0);
//			String cmd = getCommand(model.getPath(), model.alpha, model.epsilon);
//			ExecuteCommand command = new ExecuteCommand(cmd, model);
//
//			// collecting results
//			double value = 0;
//			long time;
//			// for (ExecuteCommand command : commands) {
//			for (int i = 0; i < 1000; i++) {
//				String[] values = command.call().split("Verifying formula ");
//
//				// if (command.getModel().getType() == ModelType.SIMULATION) {
//
//				value = getSimulatedValue(values[1]);
//				time = getCPUtime(values[1]);
//				System.out.println(value + ", " + time);
//			}
//			// } else if (command.getModel().getType() == ModelType.PROBABILITY)
//			// {
//			// value = getProbability(values[1]);
//			// }
//			// ((Int)
//			// cao.get(command.getModel().getKey())).setValue(toInt(value));
//			// // Parsing values and times
//			// // R1.addAndGet(getCPUtime(formulasFR[1]));
//			// // R2.addAndGet(getCPUtime(formulasCost[1]));
//			// double power, packetLoss;
//			// power = getSimulatedValue(formulasPower[1]);
//			// packetLoss = getProbability(formulasPacketLoss[1]);
//			// ((Int) cao.get("power")).setValue(toInt(powerUsage));
//			// ((Int) cao.get("packetLoss")).setValue(toInt(packetLoss));
//
//		} catch (Exception ex) {
//			// System.err.println(Arrays.toString(formulasPower));
//			ex.printStackTrace();
//		}
	}
}
