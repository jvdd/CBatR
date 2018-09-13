package mapek;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.Prism;
import prism.PrismException;
import prism.Result;
import prismWrapper.PrismFileLog;

public class PrismChecker {

	private static final String PRISMOUTPUTFILENAME = "prismlog/output_Prism.txt";
	private static final int PRECISION = 100;
	private PrismFileLog fileLog;
	private Prism prism;

	String modelPacketLoss, modelEnergyConsumption, updatedModelPacketLoss, updatedModelEnergyConsumption;

	// Models
	public static final Path packetLossModelPath = Paths
			.get(System.getProperty("user.dir") + "/models/PacketLoss.prism");
	public static final Path energyConsumptionModelPath = Paths
			.get(System.getProperty("user.dir") + "/models/EnergyConsumption.prism");

	// Properties
	public static final Path packetLossPropsPath = Paths
			.get(System.getProperty("user.dir") + "/models/PacketLoss.props");
	public static final Path energyConsumptionPropsPath = Paths
			.get(System.getProperty("user.dir") + "/models/EnergyConsumption.props");

	public PrismChecker() {
		new File("prismlog").mkdir();
		readModels();
		initPrism();
	}

	public void checkPacketLoss(AdaptationOption configuration, Environment environment) {
		updatePacketLossModel(configuration, environment);

		try {
			try (PrintStream out = new PrintStream(new FileOutputStream("updatedPacketlossModel.txt"))) {
				out.print(updatedModelPacketLoss);
			}
			ModulesFile modulesFile = prism.parseModelString(updatedModelPacketLoss);

			PropertiesFile propertiesFile = prism.parsePropertiesFile(modulesFile, packetLossPropsPath.toFile());
			prism.buildModel(modulesFile);
			Result result = prism.modelCheck(propertiesFile, propertiesFile.getProperty(0));

			double packetLoss = (double) result.getResult();
			configuration.verificResults.packetLoss = packetLoss;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkEnergyConsumption(AdaptationOption configuration, Environment environment) {
		updateEnergyConsumptionModel(configuration, environment);

		try {
			// try (PrintStream out = new PrintStream(new
			// FileOutputStream("updateEnergyModel.txt"))) {
			// out.print(updatedModelEnergyConsumption);
			// }
			ModulesFile modulesFile = prism.parseModelString(updatedModelEnergyConsumption);

			PropertiesFile propertiesFile = prism.parsePropertiesFile(modulesFile, energyConsumptionPropsPath.toFile());
			prism.buildModel(modulesFile);
			Result result = prism.modelCheck(propertiesFile, propertiesFile.getProperty(0));

			double energyConsumption = (double) result.getResult();
			configuration.verificResults.energyConsumption = energyConsumption;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updatePacketLossModel(AdaptationOption configuration, Environment environment) {

		double[] moteLoad = new double[configuration.system.motes.size()];
		double[] linksSNR = new double[environment.linksSNR.size()];
		List<Double> linksDistribution = new LinkedList<>();

		for (int i = 0; i < environment.motesLoad.size(); i++) {
			moteLoad[i] = environment.motesLoad.get(i).load / 100;
		}

		for (int i = 0; i < environment.linksSNR.size(); i++) {
			linksSNR[i] = environment.linksSNR.get(i).SNR;
		}

		for (Mote mote : configuration.system.motes.values()) {
			if (mote.getLinks().size() == 2) {
				for (Link link : mote.links) {
					linksDistribution.add((double) link.distribution / 100);
				}
			}
		}

		Object[] modelParameters = new Object[moteLoad.length + linksSNR.length + linksDistribution.size()];
		int i = 0;
		for (double pMoteData : moteLoad)
			modelParameters[i++] = pMoteData;
		for (double snr : linksSNR)
			modelParameters[i++] = snr;
		for (double distribution : linksDistribution)
			modelParameters[i++] = distribution;

		updatedModelPacketLoss = String.format(modelPacketLoss, modelParameters);// moteLoad);//,
																					// linksSNR,
																					// linksDistribution);
	}

	private void updateEnergyConsumptionModel(AdaptationOption configuration, Environment environment) {

		double[] moteLoad = new double[configuration.system.motes.size()];
		List<Integer> linksPower = new LinkedList<>();
		List<Integer> linksDistribution = new LinkedList<>();

		for (int i = 0; i < environment.motesLoad.size(); i++) {
			moteLoad[i] = environment.motesLoad.get(i).load / 100;
		}

		for (Mote mote : configuration.system.motes.values()) {
			for (Link link : mote.links) {
				linksPower.add(link.power);
				if (mote.getLinks().size() == 2) {
					linksDistribution.add(link.distribution);
				}
			}
		}

		Object[] modelParameters = new Object[moteLoad.length + linksPower.size() + linksDistribution.size()];
		int i = 0;
		for (double pMoteData : moteLoad)
			modelParameters[i++] = pMoteData;
		for (int power : linksPower)
			modelParameters[i++] = power;
		for (int distribution : linksDistribution)
			modelParameters[i++] = distribution;

		updatedModelEnergyConsumption = String.format(modelEnergyConsumption, modelParameters);// moteLoad);//,
																								// linksPower,
																								// linksDistribution);
	}

	private void initPrism() {
		try {
			fileLog = new PrismFileLog(Paths.get(PRISMOUTPUTFILENAME));

			prism = new Prism(fileLog);
			prism.initialise();

		} catch (PrismException e) {
			e.printStackTrace(System.err);
		}
	}

	private boolean readModels() {
		try {
			modelPacketLoss = new String(Files.readAllBytes(packetLossModelPath), Charset.defaultCharset());
			modelEnergyConsumption = new String(Files.readAllBytes(energyConsumptionModelPath),
					Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static int toInt(double val) {
		return (int) Math.round(val * PRECISION);
	}

	public static double toDouble(int val) {
		return (double) val / PRECISION;
	}
}
