package esameJava_15_07_24;

import java.time.Duration;
import java.util.concurrent.locks.*;

public class Erogatore {

	int maxCl, livelloCl = maxCl;
	boolean occupato = false, inSostituzione = false;
	Duration tempoSpillaturaPiccolo, tempoSpillaturaGrande, tempoSostituzioneFusto;
	Lock lock = new ReentrantLock();
	Condition codaPiccoli = lock.newCondition();
	Condition codaGrandi = lock.newCondition();
	Condition codaAddetti = lock.newCondition();
	int sospPiccoli = 0, sospGrandi = 0;
	
	public Erogatore(int maxCl, Duration tempoSpillaturaPiccolo, Duration tempoSpillaturaGrande, Duration tempoSostituzioneFusto) {
		this.maxCl = maxCl;
		this.tempoSpillaturaPiccolo = tempoSpillaturaPiccolo;
		this.tempoSpillaturaGrande = tempoSpillaturaGrande;
		this.tempoSostituzioneFusto = tempoSostituzioneFusto;
	}
	
	public void InizioPrelievo(int formato) {
		lock.lock();
		try {
			while (livelloCl < formato || occupato || inSostituzione) {
				codaAddetti.signal();
				codaPiccoli.await();
			}
			occupato = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public void FinePrelievo(int formato) {
		lock.lock();
		try {
			livelloCl -= formato;
			occupato = false;
			if (sospPiccoli != 0) codaPiccoli.signal();
			else if (sospGrandi != 0) codaGrandi.signal();
		} finally { lock.unlock(); }
	}
	
	public void InizioSostituzione() {
		lock.lock();
		try {
			while (occupato || livelloCl >= 33) {
				codaAddetti.await();
			}
			inSostituzione = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public void FineSostituzione() {
		lock.lock();
		try {
			livelloCl = maxCl;
			inSostituzione = false;
			if (sospPiccoli != 0) codaPiccoli.signal();
			else if (sospGrandi != 0) codaGrandi.signal();
		} finally { lock.unlock(); }
	}
}