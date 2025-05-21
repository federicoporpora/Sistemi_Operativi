package labJava_05_05_25;

import java.time.Duration;

public class AddettoLavaggio implements Runnable {
	
	private Bancone b;
	private int maxCapi, paccoCamicie, paccoNormali;

	public AddettoLavaggio(Bancone b, int maxCapi, int paccoCamicie, int paccoNormali) {
		super();
		this.b = b;
		this.maxCapi = maxCapi;
		this.paccoCamicie = paccoCamicie;
		this.paccoNormali = paccoNormali;
	}

	public Bancone getB() {
		return b;
	}
	
	public void run() {
		while (b.getTotCamicie() + b.getTotNormali() < this.maxCapi) {
			try {
				Thread.sleep(Duration.ofSeconds(Main.lavaggioTempo));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			b.depositaCapiPuliti(paccoCamicie, paccoNormali);
		}
	}
}