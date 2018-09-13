package mapek;

public class SNR {
	int source;
	int destination;
	double SNR;
	
	public SNR(int source, int destination, double snr) {
		this.source = source;
		this.destination = destination;
		this.SNR = snr;
	}

	public SNR getCopy() {
		return new SNR(source, destination, SNR);
	}
}
