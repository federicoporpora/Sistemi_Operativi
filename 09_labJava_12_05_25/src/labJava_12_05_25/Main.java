package labJava_12_05_25;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Fattoria fattoria = new Fattoria();
		int nBot = 2, nZoo = 3;
		System.out.println("L'incasso totale dovrebbe essere di " + (nBot*fattoria.getpBot() + nZoo*fattoria.getpZoo()) + " €");		
		
		Thread[] bambini = new Thread [nBot + nZoo];
		
		for (int i = 0; i < nBot; i++) {
			bambini[i] = new BambinoB(fattoria);
			bambini[i].start();
		}
		
		for (int i = 0; i < nZoo; i++) {
			bambini[i + nBot] = new BambinoZ(fattoria);
			bambini[i + nBot].start();
		}
		
		for (int i = 0; i < nBot + nZoo; i++) {
			bambini[i].join();
		}
		
		fattoria.stampa();
	}
}