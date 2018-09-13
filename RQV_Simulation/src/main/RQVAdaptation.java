package main;

import java.util.List;

import deltaiot.client.Effector;
import deltaiot.client.Probe;
import deltaiot.client.SimulationClient;
//import deltaiot.client.SimulationClient;
import deltaiot.services.QoS;
import mapek.FeedbackLoop;
import simulator.Simulator;

public class RQVAdaptation {
	
	Probe probe;
	Effector effector;
	Simulator simulator;
	public void start(){

		// get probe and effectors
		//probe = new Probe();
		//effector = new Effector();
		//SimulationClient client = new SimulationClient();
		//probe = client.getProbe();
		//effector = client.getEffector();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				FeedbackLoop feedbackLoop = new FeedbackLoop();
				feedbackLoop.setProbe(probe);
				feedbackLoop.setEffector(effector);

				// StartFeedback loop
				feedbackLoop.start();

				// See results
				printResults();
		
			}
		}).start();
		// Connect probe and effectors with feedback loop
			}
	
	void printResults(){
		// Get QoS data of previous runs
		List<QoS> qosList =  probe.getNetworkQoS(96);
		for(QoS qos: qosList){
			System.out.println(String.format("%s, %f, %f", qos.getPeriod(), 
					qos.getPacketLoss(), qos.getEnergyConsumption()));
		}
	}

	public static void main(String[] args) {
		RQVAdaptation simpleAdaptation = new RQVAdaptation();
		simpleAdaptation.initializeSimulator();
		simpleAdaptation.start();
 	}

	public void initializeSimulator() {
		SimulationClient client = new SimulationClient();
		probe = client.getProbe();
		effector = client.getEffector();
		simulator = client.getSimulator();
		//probe = new Probe();
		//effector = new Effector();
	}
	
	public Simulator getSimulator() {
		return simulator;
	}
}
