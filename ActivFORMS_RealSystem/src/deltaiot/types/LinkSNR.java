package deltaiot.types;

import java.util.HashMap;

import activforms.types.Int;

public class LinkSNR {
	private int source;
	private int destination;
	private int SNR;
	
	public LinkSNR(){}
	
	public LinkSNR(int source, int destination, int SNR) {
		this.source = source;
		this.destination = destination;
		this.SNR = SNR;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getSNR() {
		return SNR;
	}

	public void setSNR(int sNR) {
		SNR = sNR;
	}

	public String toModelString() {
		return String.format("{%d, %d, %d}", source, destination, SNR);
	}

	public static LinkSNR parseLinkSNR(HashMap<String, Int> model) {
		LinkSNR snr = new LinkSNR();
		
		snr.source = model.get("source").getValue();
		snr.destination = model.get("destination").getValue();
		snr.SNR = model.get("SNR").getValue();
		
		return snr;
	}
}
