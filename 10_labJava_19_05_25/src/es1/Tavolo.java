package es1;

import java.time.Duration;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tavolo {
	
	private int bicchieriLavastoviglie, piattiLavastoviglie, bicchieriTavolo, piattiTavolo, maxBicchieriTavolo, maxPiattiTavolo;
	private Lock lock;
	private Condition codaBicchieri;
	private Condition codaPiatti;
	private Condition codaLavastoviglie;
	
	public Tavolo(int piattiLavastoviglie, int bicchieriLavastoviglie, int maxPiattiTavolo, int maxBicchieriTavolo) {
		this.piattiLavastoviglie = piattiLavastoviglie;
		this.bicchieriLavastoviglie = bicchieriLavastoviglie;
		this.maxPiattiTavolo = maxPiattiTavolo;
		this.maxBicchieriTavolo = maxBicchieriTavolo;
		piattiTavolo = 0;
		bicchieriTavolo = 0;
		lock = new ReentrantLock();
		codaPiatti = lock.newCondition();
		codaBicchieri = lock.newCondition();
		codaLavastoviglie = lock.newCondition();
	}

	public void prelevaPerLavastoviglie() {
		lock.lock();
		try {
			while (bicchieriTavolo < bicchieriLavastoviglie || piattiTavolo < piattiLavastoviglie) {
				codaLavastoviglie.await();
			}
			System.out.println("Lavastoviglie partita!");
			piattiTavolo -= piattiLavastoviglie;
			bicchieriTavolo -= bicchieriLavastoviglie;
			codaBicchieri.signalAll();
			codaPiatti.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
		try {
			Thread.sleep(Duration.ofSeconds(5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void depositaPiatto() {
		lock.lock();
		try {
			while(piattiTavolo == maxPiattiTavolo) {
				codaPiatti.await();
			}
			piattiTavolo++;
			codaLavastoviglie.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}

	public void depositaBicchiere() {
		lock.lock();
		try {
			while(bicchieriTavolo == maxBicchieriTavolo) {
				codaBicchieri.await();
			}
			bicchieriTavolo++;
			codaLavastoviglie.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public void stampa() {
		System.out.println("\n- - - STAMPA - - -\nAl momento ci sono:");
		System.out.println(piattiTavolo + " piatti sul tavolo");
		System.out.println(bicchieriTavolo + " bicchieri sul tavolo\n");
	}
}