package es1;

public class AddettoLavastoviglie extends Thread {
	
	Tavolo t;
	
	public AddettoLavastoviglie(Tavolo t) {
		this.t = t;
	}
	
	public void run() {
		while(true) {
			t.prelevaPerLavastoviglie();
		}
	}
}