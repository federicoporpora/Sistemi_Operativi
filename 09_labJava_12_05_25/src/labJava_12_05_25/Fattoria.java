package lab_12_05_25;

import java.util.concurrent.Semaphore;

public class Fattoria {
	
	final int MAX = 2;
	final int C = 1;
	final int pBot = 10;
	final int pZoo = 25;
	private Semaphore sBot, sZoo, mutex;
	private int sospesiBot, sospesiZoo, bambiniBot, bambiniZoo, incasso;

	public Fattoria() {
		this.sBot = new Semaphore(0);
		this.sZoo = new Semaphore(0);
		this.mutex = new Semaphore(1);
		this.sospesiBot = 0;
		this.sospesiZoo = 0;
		this.bambiniBot = 0;
		this.bambiniZoo = 0;
		this.incasso = 0;
	}
	
	public int getpBot() {
		return pBot;
	}
	
	public int getpZoo() {
		return pZoo;
	}
	
	public void entraBot() {
		try {
			mutex.acquire();
			while (bambiniBot + bambiniZoo >= MAX) {
				sospesiBot++;
				mutex.release();
				sBot.acquire();
				mutex.acquire();
				sospesiBot--;
			}
			bambiniBot++;
			incasso += pBot;
			mutex.release();
		} catch (InterruptedException e) {}
	}
	
	public void esceBot() {
		try {
			mutex.acquire();
			bambiniBot--;
			while (sospesiBot > 0 || sospesiZoo > 0) {
				if (sospesiBot > 0) {
					sospesiBot--;
					sBot.release();
				} else {
					sospesiZoo--;
					sZoo.release();
				}
			}
			mutex.release();
		} catch (InterruptedException e) {}
	}
	
	public void entraZoo() {
		try {
			mutex.acquire();
			while (bambiniBot + bambiniZoo >= MAX || bambiniZoo >= C) {
				sospesiZoo++;
				mutex.release();
				sZoo.acquire();
				mutex.acquire();
				sospesiZoo--;
			}
			bambiniZoo++;
			incasso += pZoo;
			mutex.release();
		} catch (InterruptedException e) {}
	}
	
	public void esceZoo() {
		try {
			mutex.acquire();
			bambiniZoo--;
			while (sospesiBot > 0 || sospesiZoo > 0) {
				if (sospesiBot > 0) {
					sospesiBot--;
					sBot.release();
				} else {
					sospesiZoo--;
					sZoo.release();
				}
			}
			mutex.release();
		} catch (InterruptedException e) {}
	}
	
	public void stampa() {
		System.out.println("\nCi sono " + this.bambiniBot + " bambini in botanica, " + this.bambiniZoo + " bambini in zoologia");
		System.out.println("L'incasso attuale è di " + this.incasso + " €");
	}
}