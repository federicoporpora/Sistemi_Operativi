package lab_12_05_25;

public class BambinoB extends Thread {
	
	private Fattoria fattoria;

	public BambinoB(Fattoria fattoria) {
		this.fattoria = fattoria;
	}

	public void run() {
		System.out.println("Bambino botanico con codice " + getName() + ", entro");
		fattoria.entraBot();
		System.out.println("Bambino botanico con codice " + getName() + ", esco");
		fattoria.esceBot();
		System.out.println("Bambino botanico con codice " + getName() + ", finito");
	}
	
}