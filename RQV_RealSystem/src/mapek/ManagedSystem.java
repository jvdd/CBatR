package mapek;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

public class ManagedSystem {
	
	HashMap<Integer, Mote> motes = new LinkedHashMap();
	
	public ManagedSystem(){
		
	}
	
	public ManagedSystem(List<Mote> motes) {
		for(Mote mote: motes){
			this.motes.put(mote.getMoteId(), mote);
		}
	}
	
	public void setMote(int moteId, Mote mote){
		motes.put(moteId, mote);
	}
	
	public Mote getMote(int moteId){
		return motes.get(moteId);
	}
	
	public ManagedSystem getCopy(){
		ManagedSystem managedSystem = new ManagedSystem();
		for(Mote mote: motes.values()){
			managedSystem.motes.put(mote.getMoteId(), mote.getCopy());
		}
		return managedSystem;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
