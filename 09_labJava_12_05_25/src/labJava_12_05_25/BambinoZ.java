package labJava_12_05_25;

public class BambinoZ extends Thread {
	
	private Fattoria fattoria;

	public BambinoZ(Fattoria fattoria) {
		this.fattoria = fattoria;
	}

	public void run() {
		System.out.println("Bambino zoologo con codice " + getName() + ", entro");
		fattoria.entraZoo();
		System.out.println("Bambino zoologo con codice " + getName() + ", esco");
		fattoria.esceZoo();
		System.out.println("Bambino zoologo con codice " + getName() + ", finito");
	}
	
}