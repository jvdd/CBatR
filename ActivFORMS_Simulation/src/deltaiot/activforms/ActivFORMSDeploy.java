package deltaiot.activforms;

import activforms.engine.ActivFORMSEngine;
import activforms.goalmanagement.goalmanager.GoalManager;
import deltaiot.client.Effector;
import deltaiot.client.Probe;
import deltaiot.client.SimulationClient;
import deltaiot.smc.SMCChecker;
import simulator.Simulator;

public class ActivFORMSDeploy {

	ActivFORMSEngine engine;
	Simulator simulator;
	Probe probe;
	Effector effector;
	
	public void start() {
		try {
			
			// Step 1: Load the feedback loop model in ActivFORMS runtime environment
			engine = new ActivFORMSEngine(System.getProperty("user.dir") + 
					"/models/DeltaIoT-MAPE.xml",9000);
			engine.setRealTimeUnit(60000);
			//DebugActivFORMS debug = new DebugActivFORMS();
			//engine.getActivFORMSProbe().register(debug);
			//engine.setCommittedLocationTime(60000);
			//GoalManager goalManager = engine.getGoalManager();
			//goalManager.addModelProperty("A[] !Analyzer.UseFaileSafeStrategy");
			//goalManager.addModelProperty("A[] K.currentConfig.qualities.packetLoss < 10");
			//goalManager.addModelProperty("Monitor.AnalysisRequired --> Analyzer.AnalysisDone");
			//goalManager.addModelProperty("Analyzer.RuntimeVerification -->(8) Analyzer.VerificationCompleted");
			
			//goalManager.addModelProperty("Analyzer.QualityEstimatesReady --> Planner.ComposeAdaptationPlan || Planner.BestOptionInUse");
			//goalManager.addModelProperty("A[] Planner.PlanCreated imply Planner.bestAdaptationOption.verificaitonResults.packetLoss < 10");
			
			//goalManager.addModelProperty("Analyzer.VerificationTimeExceeded --> Analyzer.UseFailSafeStrategy");
			//goalManager.addModelProperty("E<> Planner.UseFailSafeStrategy");
			
			// Step 2: Connecting models with managed system
			
			//Probe probe = new Probe();
			//Effector effector = new Effector();
			
			//Probe probe = client.getProbe();
			//Effector effector = client.getEffector();
			
			ProbeConnector probeConnector = new ProbeConnector();
			probeConnector.setEngine(engine);
			probeConnector.setProbe(probe);
			
			EffectorConnector effectorConnector = new EffectorConnector();
			effectorConnector.setEngine(engine);
			effectorConnector.setEffector(effector);
			
			SMCChecker smcChecker = new SMCChecker(System.getProperty("user.dir") + "/SMCConfig.properties");
			SMCConnector smcConnector = new SMCConnector(smcChecker, engine);
			smcConnector.setUseThreadForReceiving(true);
			
			// Step 3: Start the ActivFORMS VM
			engine.start();
			probeConnector.start();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void stop(){
		engine.stop();
	}

	public static void main(String[] args) {
		System.out.println("\nStarted");
		ActivFORMSDeploy start = new ActivFORMSDeploy();
		start.initializeSimulator();
		start.start();
		// TODO: Hieronder toegevoegd
		//RunStatisticResults.waitForPreviousToComplete();
		//cleanUp();
		//System.gc();
		// TODO: Hierboven toegevoegd
	}

	public void initializeSimulator() {
		SimulationClient client = new SimulationClient();
		probe = client.getProbe();
		effector = client.getEffector();
		simulator = client.getSimulator();
	}

	public Simulator getSimulator() {
		return simulator;
	}
	
	// TODO: hieronder toegevoegd
	//private static void cleanUp() {
		//engine = null;
		//simulator = null;
		//probe = null;
		//effector = null;
	//}
	// TODO: hierboven toegevoegd
}
