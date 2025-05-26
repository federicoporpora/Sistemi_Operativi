package es1;

import java.time.Duration;

public class Cameriere extends Thread {
	
	Tavolo t;
	int tipo; //0 per piatti 1 per bicchieri
	
	public Cameriere(Tavolo t, int tipo) {
		this.t = t;
		this.tipo = tipo;
	}
	
	public void run() {
		while(true) {
			if (tipo==0) {
				try {
					sleep(Duration.ofSeconds(1));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				t.depositaPiatto();
			}
			else {
				try {
					sleep(Duration.ofSeconds(2));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				t.depositaBicchiere();
			}
		}
	}
}