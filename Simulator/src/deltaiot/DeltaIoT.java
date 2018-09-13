package deltaiot;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import deltaiot.services.Mote;

public class DeltaIoT {
	@Expose double PacketLoss, PowerConsumption;
	@Expose
	public HashMap<Integer, Mote> Motes;
	
	public DeltaIoT() {
		
	}
	
	public DeltaIoT(Map<Integer, Mote> motes, double packetLoss) {
		this.PacketLoss = packetLoss;
		this.Motes = (HashMap<Integer, Mote>) motes;
	}
	
	@Override
	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
	
	public String toJson(){
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeSpecialFloatingPointValues().create();
		return gson.toJson(this);
	}
	
	@Override
	public Object clone() {
		HashMap<Integer, Mote> moteCopy = new LinkedHashMap<>();
		for(Entry<Integer, Mote> entry: Motes.entrySet()){
			moteCopy.put(entry.getKey(), (Mote)entry.getValue().clone());
		}
		
		return new DeltaIoT(moteCopy, PacketLoss);
	}
	
	public void setPacketLoss(double packetLoss) {
		this.PacketLoss = packetLoss;
	}
	
	public double getPacketLoss() {
		return PacketLoss;
	}
	
	public void setPower(double power) {
		this.PowerConsumption = power;
	}
	
	public double getPower() {
		return PowerConsumption;
	}
	
	public HashMap<Integer, Mote> getMotes() {
		return Motes;
	}
	
	public void initializeMots(){
		Motes = new LinkedHashMap<>();
	}
}
