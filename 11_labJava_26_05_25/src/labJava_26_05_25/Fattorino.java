package labJava_26_05_25;

import java.time.Duration;

public class Fattorino extends Thread {

	private StradaPiazza stradaPiazza;
	private Duration tempoStrada;
	private Duration tempoMercato;
	
	public Fattorino(StradaPiazza stradaPiazza, Duration tempoStrada, Duration tempoMercato) {
		this.stradaPiazza = stradaPiazza;
		this.tempoStrada = tempoStrada;
		this.tempoMercato = tempoMercato;
	}

	public void run() {
		try {
			//System.out.println("Fattorino: ora entro in strada direzione IN");
			stradaPiazza.entraFattorino(Direzione.IN);
			//System.out.println("Fattorino: entrato in strada, ora passa del tempo");
			Thread.sleep(tempoStrada);
			//System.out.println("Fattorino: tempo passato, esco dalla strada in direzione IN");
			stradaPiazza.esceFattorino(Direzione.IN);
			//System.out.println("Fattorino: uscito dalla strada, ora giro per il mercato");
			Thread.sleep(tempoMercato);
			//System.out.println("Fattorino: tempo nel mercato passato, ora entro in strada direzione OUT");
			stradaPiazza.entraFattorino(Direzione.OUT);
			//System.out.println("Fattorino: entrato in strada, ora passa del tempo");
			Thread.sleep(tempoStrada);
			//System.out.println("Fattorino: tempo passato, esco dalla strada in direzione OUT");
			stradaPiazza.esceFattorino(Direzione.OUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}