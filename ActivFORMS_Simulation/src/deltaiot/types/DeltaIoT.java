package deltaiot.types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class DeltaIoT {
	
	public HashMap<Integer, Mote>  Motes;
	
	public DeltaIoT() {
		Motes = new LinkedHashMap<>();
	}
	
	public HashMap<Integer, Mote> getMotes() {
		return Motes;
	}
	
	public DeltaIoT(Map<Integer, Mote> motes) {
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
	
	public static DeltaIoT parseDeltaIoT(HashMap<String, HashMap> deltaIoTModel){
		DeltaIoT deltaIoT = new DeltaIoT();
		Mote m;
		HashMap<Integer, HashMap> motes = (HashMap<Integer, HashMap>) deltaIoTModel.get("motes");
		for(Entry<Integer, HashMap> mote: motes.entrySet()){
			m = Mote.parseMote(mote.getValue());
			deltaIoT.Motes.put(mote.getKey(), m);
		}
		
		return deltaIoT;
	}

	public String toModelString() {
		StringBuilder str = new StringBuilder();
		
		str.append("\nManagedSystem deltaIoT = {{\n");
		
		for(Mote m: Motes.values()){
			str.append(m.toModelString());
			str.append(",\n");
		}
		str.setLength(str.length()-2);
		str.append("}};\n");
		return str.toString();
	}
}
