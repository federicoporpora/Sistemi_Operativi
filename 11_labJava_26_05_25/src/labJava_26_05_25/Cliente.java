package labJava_26_05_25;

import java.time.Duration;

public class Cliente extends Thread {
	
	private StradaPiazza stradaPiazza;
	private Duration tempoStrada;
	private Duration tempoMercato;
	
	public Cliente(StradaPiazza stradaPiazza, Duration tempoStrada, Duration tempoMercato) {
		this.stradaPiazza = stradaPiazza;
		this.tempoStrada = tempoStrada;
		this.tempoMercato = tempoMercato;
	}

	public void run() {
		try {
			//System.out.println("Cliente: ora entro in strada direzione IN");
			stradaPiazza.entraCliente(Direzione.IN);
			//System.out.println("Cliente: entrato in strada, ora passa del tempo");
			Thread.sleep(tempoStrada);
			//System.out.println("Cliente: tempo passato, esco dalla strada in direzione IN");
			stradaPiazza.esceCliente(Direzione.IN);
			//System.out.println("Cliente: uscito dalla strada, ora giro per il mercato");
			Thread.sleep(tempoMercato);
			//System.out.println("Cliente: tempo nel mercato passato, ora entro in strada direzione OUT");
			stradaPiazza.entraCliente(Direzione.OUT);
			//System.out.println("Cliente: entrato in strada, ora passa del tempo");
			Thread.sleep(tempoStrada);
			//System.out.println("Cliente: tempo passato, esco dalla strada in direzione OUT");
			stradaPiazza.esceCliente(Direzione.OUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}