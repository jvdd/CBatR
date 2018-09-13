package deltaiot.activforms;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import activforms.types.Int;
import deltaiot.services.Link;
import deltaiot.services.Mote;
import deltaiot.types.DeltaIoT;

public class Settings {
	public static final int PRECISION = 100;
	public static final int routerId = 1;
	public static final int MAX_DEVICES = 15;
	public static final int MAX_LINKS = 2;
	public static final boolean DEBUG = true;
	public static long startTime;
	public static AtomicBoolean adaptationDone = new AtomicBoolean(false);
	public static int toInt(double val) {
		return (int) Math.round(val * PRECISION);
	}

	public static double toDouble(int val) {
		return (double)val / PRECISION;
	}
/*
	public static DeltaIoT toLoRA(HashMap modelLoRA){
		DeltaIoT loRA = new DeltaIoT();
		//loRA.initializeMotes();
		//loRA.setPacketLoss(toDouble(((Int) modelLoRA.get("packetLoss")).getValue()));
		//loRA.setPower(toDouble((((Int) modelLoRA.get("energyConsumption")).getValue())));
		int id;
		HashMap<String, Integer> modelMote;
		Mote mote;
		HashMap<Integer, HashMap<String, Integer>> motes = (HashMap<Integer, HashMap<String, Integer>>) ((HashMap<String, HashMap>)modelLoRA.get("deltaIoT")).get("motes");
		for (Entry<Integer, HashMap<String, Integer>> entry : motes.entrySet()) {
			mote = new Mote();
			id = entry.getKey();
			mote.setMoteid(id);

			modelMote = entry.getValue();

			mote.setLoad(modelMote.get("load"));
			mote.setBattery(modelMote.get("energyLevel"));
			mote.setParents(modelMote.get("parents"));
			//mote.setDataProbability(modelMote.get("dataProbability"));
			//mote.setQueueSize(modelMote.get("queue"));
			HashMap<String, Integer> modelLink;
			Link link;
			for (Entry<Integer, HashMap<String, Integer>> linkEntry : ((HashMap<Integer, HashMap<String, Integer>>) (Object) modelMote
					.get("links")).entrySet()) {

				modelLink = linkEntry.getValue();
				// if not empty link
				if (modelLink.get("destination") != 0) {
					link = new Link();

					//link.setLatency(toDouble(modelLink.get("latency")));
					link.setPower(modelLink.get("powerSetting"));
					//link.setPacketLoss(modelLink.get("packetLoss"));
					link.setDest(modelLink.get("destination"));
					//link.setSNR((double)modelLink.get("SNR"));
					link.setDistribution(modelLink.get("distribution"));
					//link.setSF(modelLink.get("SF"));
					link.setSource(id);
					mote.getLinks().add(link);
				}
			}
			loRA.getMotes().put(id, mote);
		}
		return loRA;
	}

	public static void toLoRAModel(DeltaIoT actualLora, HashMap modelLoRA){
		int id;
		Mote mote;
		for(Entry<Integer, Mote> entry: actualLora.Motes.entrySet()){
			id = entry.getKey();
			if (id == 1) continue;
			mote = entry.getValue();
			HashMap<String, Int> moteModel = (HashMap<String, Int>) ((HashMap)modelLoRA.get("motes")).get(id);
			moteModel.get("moteId").setValue(id);
			moteModel.get("load").setValue(mote.getLoad());
			moteModel.get("battery").setValue(((Double)mote.getBattery()).intValue());
			moteModel.get("parents").setValue(mote.getParents());
			moteModel.get("dataProbability").setValue(mote.getDataProbability());
			if (mote.getQueueSize() > 0)
				moteModel.get("queue").setValue(mote.getQueueSize());
			int i = 0;
			for(Link link: mote.getLinks()){
				HashMap<String, Int> linkMote = (HashMap<String, Int>) ((HashMap)(Object)moteModel.get("links")).get(i++);

				linkMote.get("latency").setValue(toInt(link.getLatency()));
				linkMote.get("power").setValue(link.getPower());
				linkMote.get("packetLoss").setValue(link.getPacketLoss());
				linkMote.get("destination").setValue(link.getDest());
				linkMote.get("SNR").setValue(link.getSNR().intValue());
				linkMote.get("distribution").setValue(link.getDistribution());
				linkMote.get("source").setValue(link.getSource());
				linkMote.get("SF").setValue(link.getSF());
			}
		}
	}

	public static Mote toMote(HashMap modelMote) {
		Mote mote = new Mote();
		int id = ((Int)modelMote.get("moteId")).getValue();
		mote.setMoteid(id);
		mote.setLoad(((Int)modelMote.get("load")).getValue());
		mote.setBattery(((Int)modelMote.get("battery")).getValue());
		mote.setParents(((Int)modelMote.get("parents")).getValue());
		mote.setDataProbability(((Int)modelMote.get("dataProbability")).getValue());
		HashMap<String, Int> modelLink;
		Link link;
		for (Entry<Integer, HashMap<String, Int>> linkEntry : ((HashMap<Integer, HashMap<String, Int>>) (Object) modelMote
				.get("links")).entrySet()) {

			modelLink = linkEntry.getValue();
			// if not empty link
			if (modelLink.get("destination").getValue() != 0) {
				link = new Link();

				link.setLatency(toDouble(modelLink.get("latency").getValue()));
				link.setPower(modelLink.get("power").getValue());
				link.setPacketLoss(modelLink.get("packetLoss").getValue());
				link.setDest(modelLink.get("destination").getValue());
				link.setSNR((double)modelLink.get("SNR").getValue());
				link.setDistribution(modelLink.get("distribution").getValue());
				link.setSF(modelLink.get("SF").getValue());
				link.setSource(id);
				mote.getLinks().add(link);
			}
		}
		return mote;
	}

	public static String toModelString(DeltaIoT lora) {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = routerId + 1; i <= MAX_DEVICES; i++){
			stringBuilder.append(toModelString(lora.getMotes().get(i)));
			stringBuilder.append(",");
		}
		stringBuilder.setLength(stringBuilder.length()-1);
		return String.format("{{%s}}", stringBuilder.toString());
	}

	public static String toModelString(Link link) {
		return String.format("{%d, %d, %d, %d, %d, %d, %d}", link.getDest(), link.getDistribution(), link.getPower(), link.getPacketLoss(),
				toInt(link.getLatency()), ((Double)link.getSNR()).intValue(), link.getSF());
	}

	public static String toModelString(Mote mote) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("{%d, %d, %d, %d, %d,%d,{", mote.getMoteid(), mote.getLoad(), ((Double)mote.getBattery()).intValue(), mote.getParents(), mote.getDataProbability(), mote.getQueueSize()));

		for(Link link: mote.getLinks()){
			stringBuilder.append(toModelString(link));
			stringBuilder.append(",");
//			if (link == null){
//				stringBuilder.append("EMPTY_LINK");
//			}
//			else{
//				stringBuilder.append(link.toModelString());
//			}
//			stringBuilder.append(",");
		}
		for(int i = mote.getParents(); i < MAX_LINKS; i++){
			stringBuilder.append("EMPTY_LINK");
			stringBuilder.append(",");
		}
		stringBuilder.setLength(stringBuilder.length()-1);
		stringBuilder.append("}}");
		return stringBuilder.toString();
	}
	*/
}
