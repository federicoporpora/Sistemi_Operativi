package labJava_26_05_25;

import java.time.Duration;

public class Guardia extends Thread {

	private StradaPiazza stradaPiazza;
	private Duration tempoStrada;
	private Duration tempoMercato;
	private int giri = 0, giriMax;
	
	public Guardia(StradaPiazza stradaPiazza, Duration tempoStrada, Duration tempoMercato, int giriMax) {
		this.stradaPiazza = stradaPiazza;
		this.tempoStrada = tempoStrada;
		this.tempoMercato = tempoMercato;
		this.giriMax = giriMax;
	}

	public void run() {
		while(giri < giriMax) {
			try {
				stradaPiazza.entraGuardia(Direzione.IN);
				Thread.sleep(tempoStrada);
				stradaPiazza.esceGuardia(Direzione.IN);
				Thread.sleep(tempoMercato);
				stradaPiazza.entraGuardia(Direzione.OUT);
				Thread.sleep(tempoStrada);
				stradaPiazza.esceGuardia(Direzione.OUT);
				giri++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}