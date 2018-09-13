package domain;

public class SNREquation{
	
	double multiplier;
	double constant;
	
	public SNREquation(double multiplier, double constant) {
		this.multiplier = multiplier;
		this.constant = constant;
	}
	
	public double getSNR(int power){
		return multiplier * power + constant;
	}
}
