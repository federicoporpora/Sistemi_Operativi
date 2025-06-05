package esercitazioneJava_03_06_25;

public class Main {

	public static void main(String[] args) {
		
		int prezzoCauzione = 10, maxA = 80, maxB = 80, maxC = 80, biciA = 2, biciB = 2, biciC = 2;
		int nSingoli = 2, nCoppie = 3;
		
		Monitor monitor = new Monitor(prezzoCauzione, maxA, maxB, maxC, biciA, biciB, biciC);
		System.out.println(monitor.toString());
		Thread[] singoli = new Thread[nSingoli];
		Thread[] coppie = new Thread[nCoppie];
		
		for (int i = 0; i < nSingoli; i++) {
			singoli[i] = new Cliente(monitor, true);
			singoli[i].start();
		}
		System.out.println("Creati i singoli e lanciati");
		
		for (int i = 0; i < nCoppie; i++) {
			coppie[i] = new Cliente(monitor, false);
			coppie[i].start();
		}
		System.out.println("Create le coppie e lanciate");
		
		for (int i = 0; i < nSingoli; i++) {
			try {
				singoli[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < nCoppie; i++) {
			try {
				coppie[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("pedalato tutti");
	}
}