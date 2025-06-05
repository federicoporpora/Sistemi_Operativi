package esercitazioneJava_03_06_25;

import java.util.concurrent.locks.*;

public class Monitor {
	
	private Lock lock = new ReentrantLock();
	private Condition[] codaSingoloSoldi = new Condition[3];
	private Condition[] codaSingoloBici = new Condition[3];
	private Condition[] codaCoppiaSoldi = new Condition[3];
	private Condition[] codaCoppiaBici = new Condition[3];
	private int[] sospSingoloSoldi = {0, 0, 0};
	private int[] sospSingoloBici = {0, 0, 0};
	private int[] sospCoppiaSoldi = {0, 0, 0};
	private int[] sospCoppiaBici = {0, 0, 0};
	private int[] cassa = {40, 40, 40};
	private int[] biciParcheggiate = new int[3];
	private int[] maxBici = new int[3];
	private int prezzoCauzione;
	
	public Monitor(int prezzoCauzione, int maxA, int maxB, int maxC, int biciA, int biciB, int biciC) {
		this.prezzoCauzione = prezzoCauzione;
		biciParcheggiate[0] = biciA;
		biciParcheggiate[1] = biciB;
		biciParcheggiate[2] = biciC;
		maxBici[0] = maxA;
		maxBici[1] = maxB;
		maxBici[2] = maxC;
		for (int i = 0; i < 3; i++) {
			codaSingoloSoldi[i] = lock.newCondition();
			codaSingoloBici[i] = lock.newCondition();
			codaCoppiaSoldi[i] = lock.newCondition();
			codaCoppiaBici[i] = lock.newCondition();
		}
	}
	
	public void noleggiaBici(int parcheggio, boolean singolo) {
		lock.lock();
		try  {
			if (singolo) {
				while (biciParcheggiate[parcheggio] < 1) {
					if (biciParcheggiate[parcheggio] < 1) System.out.println("non posso noleggiare la bici, biciParcheggiate in " + parcheggio +" < 1");
					sospSingoloBici[parcheggio]++;
					codaSingoloBici[parcheggio].await();
					sospSingoloBici[parcheggio]--;
				}
				biciParcheggiate[parcheggio]--;
				cassa[parcheggio] += 10;
				if (sospCoppiaSoldi[parcheggio] != 0) codaCoppiaSoldi[parcheggio].signal(); // secondo me questa condizione prende la maggior parte dei casi, non ritengo necessario un controllo sulla cassa perché verrà fatto nel while comunque
				if (sospSingoloSoldi[parcheggio] != 0) codaSingoloSoldi[parcheggio].signal();
			} else {
				while (biciParcheggiate[parcheggio] < 2) {
					if (biciParcheggiate[parcheggio] < 2) System.out.println("non posso noleggiare la bici, biciParcheggiate in " + parcheggio + " < 2");
					sospCoppiaBici[parcheggio]++;
					codaCoppiaBici[parcheggio].await();
					sospCoppiaBici[parcheggio]--;
				}
				biciParcheggiate[parcheggio] -= 2;
				cassa[parcheggio] += 20;
				if (sospCoppiaSoldi[parcheggio] != 0) codaCoppiaSoldi[parcheggio].signal();
				else if (sospSingoloSoldi[parcheggio] != 0) {
					codaSingoloSoldi[parcheggio].signal();
					codaSingoloSoldi[parcheggio].signal();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public void consegnaBici(int parcheggio, boolean singolo) {
		lock.lock();
		try  {
			if (singolo) {
				while (biciParcheggiate[parcheggio] >= maxBici[parcheggio]-1 || cassa[parcheggio] < prezzoCauzione) {
					if (biciParcheggiate[parcheggio] >= maxBici[parcheggio]-1) System.out.println("non posso consegnare la bici, biciParcheggiate in " + parcheggio + " >= maxBici in " + parcheggio +" -1");
					if (cassa[parcheggio] < prezzoCauzione) System.out.println("non posso consegnare la bici, cassa in " + parcheggio + " < prezzoCauzione");
					sospSingoloSoldi[parcheggio]++;
					codaSingoloSoldi[parcheggio].await();
					sospSingoloSoldi[parcheggio]--;
				}
				biciParcheggiate[parcheggio]++;
				cassa[parcheggio] -= 10;
				if (sospSingoloBici[parcheggio] != 0) codaSingoloBici[parcheggio].signal();
				else if (sospCoppiaBici[parcheggio] != 0) codaCoppiaBici[parcheggio].signal();
			} else {
				while (biciParcheggiate[parcheggio] >= (maxBici[parcheggio]-2) || cassa[parcheggio] < 2*prezzoCauzione) {
					if (biciParcheggiate[parcheggio] >= maxBici[parcheggio]-2) System.out.println("non posso consegnare la bici, biciParcheggiate in " + parcheggio + " >= maxBici in " + parcheggio + " -2");
					if (cassa[parcheggio] < prezzoCauzione) System.out.println("non posso consegnare la bici, cassa in " + parcheggio + "  < prezzoCauzione");
					sospCoppiaSoldi[parcheggio]++;
					codaCoppiaSoldi[parcheggio].await();
					sospCoppiaSoldi[parcheggio]--;
				}
				biciParcheggiate[parcheggio] += 2;
				cassa[parcheggio] -= 20;
				if (sospSingoloBici[parcheggio] != 0) {
					codaSingoloBici[parcheggio].signal();
					codaSingoloBici[parcheggio].signal();
				} else if (sospCoppiaBici[parcheggio] != 0) codaCoppiaBici[parcheggio].signal();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { lock.unlock(); }
	}
	
	public String toString() {
		return "--- Condizione attuale ---\nA:\tbici parcheggiate = " + biciParcheggiate[0] + ", cassa = " + cassa[0] +
				"\nB:\tbici parcheggiate = " + biciParcheggiate[1] + ", cassa = " + cassa[1] +
				"\nC:\tbici parcheggiate = " + biciParcheggiate[2] + ", cassa = " + cassa[2];
	}
}