package labJava_26_05_25;

import java.util.concurrent.locks.*;

public class StradaPiazza {
	
	// ho usato le signal in ogni caso perché non avevo tempo, so perfettamente che dovrei mettere degli if prima delle signal
	
	private int maxStrada, maxPiazza;
	private int[] fattoriniInStrada = {0, 0}; // in, out
	private int totStrada = 0;
	private int personeInPiazza = 0;
	private int guardieInPiazza = 0;
	private Lock lock;
	private Condition[] codaPersoneStrada = new Condition[2];
	private Condition[] codaFattoriniStrada = new Condition[2];
	private Condition[] codaGuardieStrada = new Condition[2];
	
	public StradaPiazza(int maxStrada, int maxPiazza) {
		this.maxStrada = maxStrada;
		this.maxPiazza = maxPiazza;
		lock = new ReentrantLock();
		codaPersoneStrada[0] = lock.newCondition();
		codaPersoneStrada[1] = lock.newCondition();
		codaFattoriniStrada[0] = lock.newCondition();
		codaFattoriniStrada[1] = lock.newCondition();
		codaGuardieStrada[0] = lock.newCondition();
		codaGuardieStrada[1] = lock.newCondition();
	}
	
	public void entraCliente(Direzione dir) {
		lock.lock();
		try {
			if (dir.equals(Direzione.IN)) {
				while (totStrada == maxStrada || guardieInPiazza == 0 || personeInPiazza == maxPiazza) {
					codaPersoneStrada[0].await();
				}
				personeInPiazza++;
				totStrada++;
			} else {
				while (totStrada < maxStrada) {
					codaPersoneStrada[0].await();
				}
				personeInPiazza--;
				totStrada++;
			}
			System.out.println("Cliente entrato in strada in direzione " + dir.toString());
			System.out.println(this.toString());
			this.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public void esceCliente(Direzione dir) {
		lock.lock();
		try {
			if (dir.equals(Direzione.IN)) {
				totStrada--;
			} else {
				totStrada--;
			}
			System.out.println("Cliente uscito dalla strada in direzione " + dir.toString());
			System.out.println(this.toString());
			this.signal();
		} finally { lock.unlock(); }
	}
	
	public void entraFattorino(Direzione dir) {
		lock.lock();
		try {
			if (dir.equals(Direzione.IN)) {
				while (totStrada > maxStrada-2 || guardieInPiazza == 0 || personeInPiazza > maxPiazza-2 || fattoriniInStrada[1] != 0) {
					codaFattoriniStrada[0].await();
				}
				personeInPiazza += 2;
				fattoriniInStrada[0]++;
				totStrada += 2;
			} else {
				while (totStrada < maxStrada-2 || fattoriniInStrada[0] != 0) {
					codaFattoriniStrada[0].await();
				}
				personeInPiazza -= 2;
				fattoriniInStrada[1]++;
				totStrada += 2;
			}
			System.out.println("Fattorino entrato in strada in direzione " + dir.toString());
			System.out.println(this.toString());
			this.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public void esceFattorino(Direzione dir) {
		lock.lock();
		try {
			if (dir.equals(Direzione.IN)) {
				fattoriniInStrada[0]--;
				totStrada -= 2;
			} else {
				fattoriniInStrada[1]--;
				totStrada -= 2;
			}
			System.out.println("Fattorino uscito dalla strada in direzione " + dir.toString());
			System.out.println(this.toString());
			this.signal();
		} finally { lock.unlock(); }
	}
	
	public void entraGuardia(Direzione dir) {
		lock.lock();
		try {
			if (dir.equals(Direzione.IN)) {
				while (totStrada == maxStrada || personeInPiazza == maxPiazza) {
					codaGuardieStrada[0].await();
				}
				personeInPiazza++;
				totStrada++;
			} else {
				while (totStrada == maxStrada || (guardieInPiazza == 1 && personeInPiazza != 1)) {
					codaGuardieStrada[0].await();
				}
				personeInPiazza--;
				guardieInPiazza--;
				totStrada++;
			}
			System.out.println("Guardia entrata in strada in direzione " + dir.toString());
			System.out.println(this.toString());
			this.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public void esceGuardia(Direzione dir) {
		lock.lock();
		try {
			if (dir.equals(Direzione.IN)) {
				guardieInPiazza++;
				totStrada--;
			} else {
				totStrada--;
			}
			System.out.println("Guardia uscita dalla strada in direzione " + dir.toString());
			System.out.println(this.toString());
			this.signal();
		} finally { lock.unlock(); }
	}
	
	private void signal() {
		codaFattoriniStrada[1].signalAll();
		codaPersoneStrada[1].signalAll();
		codaGuardieStrada[1].signalAll();
		codaGuardieStrada[0].signalAll();
		codaPersoneStrada[0].signalAll();
		codaFattoriniStrada[0].signalAll();
	}
	
	public String toString() {
		return " - - - Stato attuale - - - \nPersone in strada : " + totStrada + "\nPersone in piazza: " + personeInPiazza + "\nGuardie in piazza: " + guardieInPiazza;
	}
}