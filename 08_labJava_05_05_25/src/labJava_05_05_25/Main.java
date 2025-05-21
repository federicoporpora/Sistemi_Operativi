package labJava_05_05_25;
import java.util.Random;



public class Main{
	public static final int maxNormaleTempo = 3;
	public static final int lavaggioTempo = 1;
	public static void main(String[] args) throws InterruptedException {
		Random random = new Random();
		int normaleTempo = random.nextInt(1, maxNormaleTempo);
		int maxCapi = 20;
		int paccoCamicie = 5, paccoNormali = 2;
		// creazione Bancone b
		Bancone b = new Bancone();
		// <creazione AddettoLavaggio>
		AddettoLavaggio addettoLavaggio = new AddettoLavaggio(b, maxCapi, paccoCamicie, paccoNormali);
		// <creazione AddettoStiratura(Normale o Camicia)>
		AddettoStiratura addettoCamicie = new AddettoStiratura(b, 1, normaleTempo);
		AddettoStiratura addettoNormali = new AddettoStiratura(b, 0, normaleTempo);
		// <attivazione di tutti i thread>
		Thread threadAddettoLavaggio = new Thread(addettoLavaggio);
		Thread threadAddettoCamicie = new Thread(addettoCamicie);
		Thread threadAddettoNormali = new Thread(addettoNormali);
		threadAddettoLavaggio.start();
		threadAddettoCamicie.start();
		threadAddettoNormali.start();
		// <attesa della terminazione di tutti i thread>
		threadAddettoLavaggio.join();
		threadAddettoCamicie.join();
		threadAddettoNormali.join();
		b.stampa(); //stampa dei valori finali
	}
}