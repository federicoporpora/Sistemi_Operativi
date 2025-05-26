package esameJava_11_07_22;

public class Motovedetta extends Thread {
	
	private int tempoCanaleMotovedetta;
	private int tempoRicognizioneMotovedetta;
	private Direzione direzione;
	private TipoImbarcazione tipo;
	
	public Motovedetta(int tempoCanaleMotovedetta, int tempoRicognizioneMotovedetta) {
		this.tempoCanaleMotovedetta = tempoCanaleMotovedetta;
		this.tempoRicognizioneMotovedetta = tempoRicognizioneMotovedetta;
		direzione = Direzione.OUT;
		tipo = TipoImbarcazione.MOTOVEDETTA;
	}
	
	public void run() {
		
	}
	
}