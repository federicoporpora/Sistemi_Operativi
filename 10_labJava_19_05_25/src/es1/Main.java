package es1;

import java.time.Duration;

public class Main {

	public static void main(String[] args) {
		
		int piattiLavastoviglie = 20, bicchieriLavastoviglie = 10;
		int maxPiattiTavolo = 40, maxBicchieriTavolo = 20;
		int nAddettiLavastoviglie = 2, nCamerieriPiatti = 10, nCamerieriBicchieri = 10;
		Tavolo tavolo = new Tavolo(piattiLavastoviglie, bicchieriLavastoviglie, maxPiattiTavolo, maxBicchieriTavolo);
		
		Thread[] addettiLavastoviglie = new Thread[nAddettiLavastoviglie];
		Thread[] camerieriP = new Thread[nCamerieriPiatti];
		Thread[] camerieriB = new Thread[nCamerieriBicchieri];
		
		for (int i = 0; i < nAddettiLavastoviglie; i++) {
			addettiLavastoviglie[i] = new AddettoLavastoviglie(tavolo);
			addettiLavastoviglie[i].start();
		}
		
		for (int i = 0; i < nCamerieriPiatti; i++) {
			camerieriP[i] = new Cameriere(tavolo, 0);
			camerieriP[i].start();
		}
		
		for (int i = 0; i < nCamerieriBicchieri; i++) {
			camerieriB[i] = new Cameriere(tavolo, 1);
			camerieriB[i].start();
		}
		
		while(true) {
			try {
				Thread.sleep(Duration.ofSeconds(2));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tavolo.stampa();
		}
	}
}