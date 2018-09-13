package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Gateway extends Node {

	private List<Mote> view;
	private HashMap<Integer, List<Packet>> packetStore = new HashMap<Integer, List<Packet>>();
	private long prevFramePackets;
	// private List<Packet> lostPackets = new ArrayList<>();
	// private List<Packet> queueLoss = new ArrayList<>();
	// private List<Packet> queuedPackets = new ArrayList<>();
	private HashMap<Integer, Integer> expectedPacketCount = new HashMap<>();
	private double powerConsumed = 0;
	private double packetLoss;

	public Gateway(int id) {
		this(id, null);
	}

	public Gateway(int id, Position position) {
		super(id, position);
	}

	public void setView(Mote... motes) {
		this.view = Arrays.asList(motes);
	}

	public List<Mote> getView() {
		return Collections.unmodifiableList(view);
	}

	@Override
	void receivePacket(Packet packet, RunInfo runInfo) {
		List<Packet> list;
		if (!packetStore.containsKey(packet.getStartingRun())) {
			list = new LinkedList<Packet>();
			packetStore.put(packet.getStartingRun(), list);
		} else {
			list = packetStore.get(packet.getStartingRun());
		}

		if (!list.contains(packet)) {
			list.add(packet);
			if (packet.getStartingRun() == runInfo.getRunNumber() - 1)
				prevFramePackets++;
		}
	}

	void addPacketToExpect(RunInfo runInfo) {
		int packets = 1;
		if (expectedPacketCount.containsKey(runInfo.getRunNumber())) {
			packets = expectedPacketCount.get(runInfo.getRunNumber());
			packets++;
		}
		expectedPacketCount.put(runInfo.getRunNumber(), packets);
	}

	public void resetPacketStore() {

		// lostPackets.clear();
	}

	public void setExpectedPackets(RunInfo runInfo, int packets) {
		for (int i = 0; i < packets; i++)
			// expectedPacketCount = packets;//countQueuedPackets();
			addPacketToExpect(runInfo);
	}

	public int getExpectedPackets(RunInfo runInfo) {
		return expectedPacketCount.get(runInfo.getRunNumber());
	}

	public void reportPacketLost(Packet packet) {
		// if (!lostPackets.contains(packet))
		// lostPackets.add(packet);
	}

	// public void resetQueueLoss(){
	// queueLoss.clear();
	// }
	//
	// public void resetQueuePackets(){
	// queuedPackets.clear();
	// }
	//
	// public double calculateQueueLoss(){
	// int queuedLoss = (int) queueLoss.stream().distinct().count();
	// return (double)queuedLoss / (double)expectedPacketCount;
	// }

	// public double calculateQueuedPackets(){
	// int queuedPackets = countQueuedPackets();
	// return (double)queuedPackets / (double)expectedPacketCount;
	// }

	public double calculateLatency(RunInfo runInfo) {
		// int totalTime = 0;
		// List<Packet> distinctPackets =
		// currentRunPackets.stream().distinct().collect(Collectors.toList());
		// for(Packet packet:distinctPackets){
		// totalTime += runInfo.getRunNumber() - packet.getStartingRun();
		// }
		//
		// return totalTime/(double)distinctPackets.size();
		double latency = 0;
		if (packetStore.containsKey(runInfo.getRunNumber() - 1)) {
			latency = ((double) prevFramePackets) / packetStore.get(runInfo.getRunNumber() - 1).size();
		}
		prevFramePackets = 0;
		return latency;
	}

	void reportPowerConsumed(double amount) {
		powerConsumed += amount;
	}

	// void reportQueueLoss(Packet packet){
	// queueLoss.add(packet);
	// }

	public void resetPowerConsumed() {
		powerConsumed = 0;
	}

	// public int countQueuedPackets(){
	// int queuedPacketsCount = (int) queuedPackets.stream().distinct().count();
	// return queuedPacketsCount;
	// }
	//
	// public void reportQueuedPacket(Packet packet){
	// queuedPackets.add(packet);
	// }

	public double calculatePacketLoss(RunInfo runInfo) {
		if (packetStore.containsKey(runInfo.getRunNumber() - 1)) {
			packetLoss = 1-((double) packetStore.get(runInfo.getRunNumber() - 1).stream().distinct().count())
					/ expectedPacketCount.get(runInfo.getRunNumber() - 1);
		}
		return packetLoss;
	}

	public double getPowerConsumed() {
		return powerConsumed;
	}

	// Debugging helpers
	public void printInfoPacketLoss(int queuedPackets) {
		// long packetStoreSizeWithoutDuplicates =
		// currentRunPackets.stream().distinct().count();

		// System.out.println("GW" + getId() + " packetloss: 1 - "
		// + packetStoreSizeWithoutDuplicates + "/" + expectedPacketCount + " =
		// " + packetLoss);
	}

	public void printInfoPacketStore() {
		// System.out.println("GW" + getId() + " PacketStore: ");
		// for (Packet packet: packetStore) {
		// System.out.println("\tnumber " + packet.getStartingRun() + " from " +
		// packet.getSource().getId() + " to " +
		// packet.getDestination().getId());
		// }
	}

	@Override
	public String toString() {
		// double packetloss = calculatePacketLoss();
		return "Gateway " + String.format("%2d", getId()) + " [storedPackets=" + packetStore.size()
				+ ", expectedPackets=" + expectedPacketCount + ", packetloss="
				+ String.format("%2d", Math.round(packetLoss * 100)) + ", powerConsumed="
				+ String.format("%.2f", powerConsumed) + "]";
	}

	@Override
	void calcualtePacketReceiveBatteryConsumption(int timeSlots) {
		// We are not calculating packet receiving battery consumption for
		// gateways
	}

}
