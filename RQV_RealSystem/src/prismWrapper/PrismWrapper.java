package prismWrapper;

//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//
//	Modified by: M. Usman Iftikhar <Linnaues University>									//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- PRISMWrapper Class that invokes PRISM API and performs quantitative model checking	//
//	  for a parameterised model and a requested property.                                   //
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//	- PRISM.modelCheck(Model model, Property property, Expression expression) --> bug???    //
//																							//
//==========================================================================================//

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import parser.Values;
import parser.ast.*;
import prism.*;
import prismWrapper.PrismFileLog;

public class PrismWrapper {

	// private PrismLog mainLog;
	private Prism prismR1;
	private Prism prismR2;
	// private Prism prismR3;
	private ModulesFile modulesFile;
	private PropertiesFile propertiesFile;

	public static Path packetLossModelPath;// =
											// Paths.get(System.getProperty("user.dir")
											// +
											// "/models/EnergyConsumption.prism");

	public static Path energyConsumptionModelPath;// =
													// Paths.get(System.getProperty("user.dir")
													// +
													// "/models/TAS-Cost.prism");

	public static Path packetLossPropsPath = Paths.get(System.getProperty("user.dir") + "/models/PacketLoss.props");
	public static Path energyConsumptionPropsPath = Paths
			.get(System.getProperty("user.dir") + "/models/EnergyConsumption.props");

	private static final String PRISMOUTPUTFILENAME = "logfile/output_Prism.txt";
	private final String ERRORFILENAME = "logfile/output_error.txt";
	private String originalModelPacketLoss, updatedModelPacketLoss;
	private String originalModelEnergyConsumption, updatedModelEnergyConsumption;

	private BufferedWriter errorFileWriter;
	private String prismResult;

	private static boolean scalabilityTests = true;
	private static int noOfMotes = 5;
	PrismFileLog fileLog;

	/**
	 * Class constructor
	 * 
	 * @param modelFile
	 *            - the Markov model file to be provided as input to PRISM
	 * @param propertiesFile
	 *            - the temporal logic file to be provided as input to PRISM
	 */
	public PrismWrapper() {
		new File("logfile").mkdir();
		readModel();
		initPrism();
	}

	/**
	 * Initialises PRISM object and log file
	 */
	private void initPrism() {
		try {
			fileLog = new PrismFileLog(Paths.get(PRISMOUTPUTFILENAME));

			prismR1 = new Prism(fileLog);
			prismR1.initialise();

			prismR2 = new Prism(fileLog);
			prismR2.initialise();

		} catch (PrismException e) {
			writeToFileError("Error: " + e.getMessage());
		}
	}

	private void setModelPaths() {
		String userDir = System.getProperty("user.dir");

		if (scalabilityTests) {
			String modelPath = userDir + "/scalability/";

			PrismWrapper.packetLossModelPath = Paths.get(modelPath + "packetloss/Packetloss" + noOfMotes + ".prism");
			PrismWrapper.energyConsumptionModelPath = Paths.get(modelPath + "prism/TAS-Cost-" + noOfMotes + ".prism");
		} else {
			throw new RuntimeException("No implemented yet");
		}
	}

	/**
	 * Reads the model and save it as a string to <b>modelString</b> variable
	 * 
	 * @return <b>true</b> if the operation completes successfully, <b>false</b>
	 *         otherwise
	 */
	private boolean readModel() {
		setModelPaths();
		try {
			originalModelPacketLoss = new String(Files.readAllBytes(packetLossModelPath), Charset.defaultCharset());
			originalModelEnergyConsumption = new String(Files.readAllBytes(energyConsumptionModelPath),
					Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Write data to error file
	 * 
	 * @param errorMessage
	 * @return <b>true</b> if the operation completes successfully, <b>false</b>
	 *         otherwise
	 */
	private boolean writeToFileError(String errorMessage) {
		try {
			errorFileWriter = new BufferedWriter(new FileWriter(ERRORFILENAME, true));
			errorFileWriter.write(errorMessage + "\n");
			errorFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Returns the result for the last quantified property as String
	 * 
	 * @return the result as String
	 */
	protected String getPrismResult() {
		return prismResult;
	}

	double emergencyP;

	public void setInitialData() {
		// this.emergencyP = emergencyP;
		// int parameterCount = 1 + currentFailureRates.size();
		// Object[] parameters = new Double[parameterCount];
		// parameters[0] = emergencyP;
		// int count = 1;
		//
		// for (Integer integer : currentFailureRates) {
		// parameters[count++] = ((double) integer / 1000);
		// }
		//
		// updatedModelR1 = String.format(originalModelR1, parameters);
		// updatedModelR2 = String.format(originalModelR2, emergencyP);
	}

	// int service4, int service5, int service6 can be provided based on how
	// many service types are
	// private SMCConfiguration checkConfiguration(int alarmService, int
	// medicalService, int drugService) {
	// SMCConfiguration configuration = new SMCConfiguration();
	// configuration.setpEmergency(this.emergencyP);
	// configuration.setServices(new int[] { alarmService, medicalService,
	// drugService });
	// try {
	// modulesFile = prismR1.parseModelString(updatedModelR1);
	//
	// Values values = new Values();
	// values.addValue("AS", alarmService);
	// values.addValue("MAS", medicalService);
	// values.addValue("DS", drugService);
	// modulesFile.setUndefinedConstants(values);
	// propertiesFile = prismR1.parsePropertiesFile(modulesFile,
	// rqvPropertyFrate.toFile());
	// propertiesFile.setUndefinedConstants(null);
	// prismR1.buildModel(modulesFile);
	//
	// Result resultR1 = prismR1.modelCheck(propertiesFile,
	// propertiesFile.getProperty(0));
	// double failureRate = (double) resultR1.getResult();
	//
	// modulesFile = prismR2.parseModelString(updatedModelR2);
	// modulesFile.setUndefinedConstants(values);
	// propertiesFile = prismR2.parsePropertiesFile(modulesFile,
	// rqvPropertyCost.toFile());
	// propertiesFile.setUndefinedConstants(null);
	// prismR2.buildModel(modulesFile);
	//
	// Result resultR2 = prismR2.modelCheck(propertiesFile,
	// propertiesFile.getProperty(0));
	// double cost = (double) resultR2.getResult();
	//
	// if (useServiceTime) {
	// modulesFile = prismR3.parseModelString(updatedModelR3);
	// modulesFile.setUndefinedConstants(values);
	// propertiesFile = prismR3.parsePropertiesFile(modulesFile,
	// rqvPropertyQueuing.toFile());
	// propertiesFile.setUndefinedConstants(null);
	// prismR3.buildModel(modulesFile);
	//
	// Result resultR3 = prismR3.modelCheck(propertiesFile,
	// propertiesFile.getProperty(0));
	// double serviceTime = (double) resultR3.getResult();
	// configuration.setServiceTime(serviceTime);
	// }
	//
	// configuration.setFailureRate(failureRate);
	// configuration.setCost(cost);
	//
	// } catch (Exception ex) {
	// ex.printStackTrace(System.out);
	// }
	// return configuration;
	// }

	public PrismFileLog getFileLog() {
		return fileLog;
	}

	private static void scalabilityTestPropertyPacketLoss(String pid) {
		try {
			ProcessMemoryLog log = new ProcessMemoryLog(pid);
			Thread thread = new Thread(log);
			thread.start();
			//long time;
			for (int count = 5; count <= 25; count+=5) {
				noOfMotes = count;
				PrismFileLog fileLog = new PrismFileLog(Paths.get(PRISMOUTPUTFILENAME));

				// Prism prism = new Prism(fileLog);
				// prism.initialise();
				Path modelPath = Paths
						.get(System.getProperty("user.dir") + "/scalability/packetloss/Packetloss" + count + ".prism");
				if (modelPath.toFile().exists()) {
					

					for (int i = 1; i <= 100; i++) {
						log.resetValues();
						String model = new String(Files.readAllBytes(modelPath), Charset.defaultCharset());
						Prism prism = new Prism(fileLog);
						prism.initialise();
						fileLog.clearLog();
						ModulesFile modulesFile = prism.parseModelString(model);

						PropertiesFile propertiesFile = prism.parsePropertiesFile(modulesFile,
								packetLossPropsPath.toFile());
						prism.buildModel(modulesFile);
						//
						Result resultR1 = prism.modelCheck(propertiesFile, propertiesFile.getProperty(0));
						double failureRate = (double) resultR1.getResult();
						
						if (log.getSamples() == 0) {
							Thread.currentThread().sleep(1000);
						}
						
						System.out.format("Packetloss: %d %d %f %f %d \n", count, i, fileLog.getTime(), failureRate, log.getMemory());
						fileLog.resetTime();
						prism.closeDown();
						// Run the garbage collector
						Runtime.getRuntime().gc();
						log.resetValues();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void scalabilityTestPropertyEnergyConsumption(String pid) {
		try {
			ProcessMemoryLog log = new ProcessMemoryLog(pid);
			Thread thread = new Thread(log);
			thread.start();
			for (int count = 25; count <= 25; count+=5) {
				noOfMotes = count;
				PrismFileLog fileLog = new PrismFileLog(Paths.get(PRISMOUTPUTFILENAME));
				Path modelPath = Paths.get(System.getProperty("user.dir")
						+ "/scalability/energyconsumption/EnergyConsumption" + count + ".prism");
				if (modelPath.toFile().exists()) {
					String model = new String(Files.readAllBytes(modelPath), Charset.defaultCharset());

					for (int i = 1; i <= 40; i++) {
						log.resetValues();
						Prism prism = new Prism(fileLog);
						prism.initialise();
						ModulesFile modulesFile = prism.parseModelString(model);
						PropertiesFile propertiesFile = prism.parsePropertiesFile(modulesFile,
								energyConsumptionPropsPath.toFile());

						prism.buildModel(modulesFile);
						//
						Result resultR1 = prism.modelCheck(propertiesFile, propertiesFile.getProperty(0));
						double failureRate = (double) resultR1.getResult();

						if (log.getSamples() == 0) {
							Thread.currentThread().sleep(1000);
						}

						System.out.format("EnergyConsumption: %d %d %f %f %d \n", count, i, fileLog.getTime(),
								failureRate, log.getMemory());
						fileLog.resetTime();
						// MemoryUsage heapMemoryUsage =
						// ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
						// System.out.println(heapMemoryUsage.getUsed()/1024);
						// System.out.println((initialMem-Runtime.getRuntime().freeMemory())/1024/1024);
						// prism.closeDown();
						// System.out.println((initialMem-Runtime.getRuntime().freeMemory())/1024/1024);
						// Get the Java runtime
						// Runtime runtime = Runtime.getRuntime();
						// // Run the garbage collector
						// runtime.gc();
						// // Calculate the used memory
						// long memory = runtime.totalMemory() -
						// runtime.freeMemory();
						// System.out.println("Used memory is bytes: " +
						// memory);
						prism.closeDown();
						
						// Run the garbage collector
						Runtime.getRuntime().gc();
						// Runtime runtime = Runtime.getRuntime();
						//
						// NumberFormat format = NumberFormat.getInstance();
						//
						// StringBuilder sb = new StringBuilder();
						// long maxMemory = runtime.maxMemory();
						// long allocatedMemory = runtime.totalMemory();
						// long freeMemory = runtime.freeMemory();
						//
						// sb.append("free memory: " + format.format(freeMemory
						// / 1024) + "<br/>");
						// sb.append("allocated memory: " +
						// format.format(allocatedMemory / 1024) + "<br/>");
						// sb.append("max memory: " + format.format(maxMemory /
						// 1024) + "<br/>");
						// sb.append("total free memory: " +
						// format.format((freeMemory + (maxMemory -
						// allocatedMemory)) / 1024) + "<br/>");
						// System.out.println(sb.toString());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// scalabilityTestPropertyPacketLoss();
		String name = ManagementFactory.getRuntimeMXBean().getName();
		// System.out.println(name);
		String pid = name.split("@")[0];
		//scalabilityTestPropertyPacketLoss(pid);
		scalabilityTestPropertyEnergyConsumption(pid);
		
	}

}
