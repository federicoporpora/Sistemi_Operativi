package esercitazioneJava_03_06_25;

import java.time.Duration;
import java.util.Random;

public class Cliente extends Thread {

	private Random random = new Random();
	private Monitor monitor;
	private boolean singolo;
	
	public Cliente(Monitor monitor, boolean singolo) {
		this.monitor = monitor;
		this.singolo = singolo;
	}

	public void run() {
		int parcheggio;
		monitor.noleggiaBici((parcheggio = random.nextInt(3)), singolo);
		System.out.println(monitor.toString());
		if (singolo) System.out.println("Noleggiata una bici da singolo dal parcheggio " + parcheggio + ", ora pedalo bene");
		else System.out.println("Noleggiate due bici da coppia dal parcheggio " + parcheggio + ", ora pedaliamo bene");
		try {
			Thread.sleep(Duration.ofSeconds(10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		monitor.consegnaBici(parcheggio, singolo);
		System.out.println(monitor.toString());
		if (singolo) System.out.println("Consegnata una bici da singolo dal parcheggio " + parcheggio + ", ora ritorno");
		else System.out.println("Consegnate due bici da coppia dal parcheggio " + parcheggio + ", ora ritorniamo");
	}
}