package labJava_05_05_25;

import java.time.Duration;

public class AddettoStiratura implements Runnable {
	
	private Bancone b;
	private int tipo; //0=normale, 1=camicie
	private int normaleTempo;
	
	public AddettoStiratura(Bancone b, int tipo, int normaleTempo) {
		super();
		this.b = b;
		this.tipo = tipo;
		this.normaleTempo = normaleTempo;
		if (tipo != 0 && tipo != 1) {
			System.out.println("Errore, il tipo è diverso da 0 e 1");
			System.exit(-1);
		}
	}

	public Bancone getB() {
		return b;
	}

	public int getTipo() {
		return tipo;
	}

	public void run() {
		try {
			Thread.sleep(Duration.ofSeconds(Main.lavaggioTempo + 1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(b.getTotCamicie() + b.getTotNormali() > b.getCamicieStirate() + b.getNormaliStirati()) {
			boolean OK;
			if (tipo == 0)
				OK = b.stiraNormale();
			else 
				OK = b.stiraCamicia();
			if (OK) {
				if (tipo == 0)
					try {
						Thread.sleep(Duration.ofSeconds(normaleTempo));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				else
					try {
						Thread.sleep(Duration.ofSeconds(Main.maxNormaleTempo * 2));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			else {
				System.out.println("Nulla da fare");
				break;
			}
		}
	}
}
