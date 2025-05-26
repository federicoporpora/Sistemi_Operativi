package esameJava_11_07_22;

public class Barca extends Thread {

	private int tempoCanalePiccola, tempoCanaleGrande;
	private int tempoStazionamentoPiccola, tempoStazionamentoGrande;
	private Direzione direzione;
	private TipoImbarcazione tipo;
	
	public Barca(int tempoCanalePiccola, int tempoCanaleGrande, int tempoStazionamentoPiccola, int tempoStazionamentoGrande, TipoImbarcazione tipo) {
		this.tempoCanalePiccola = tempoCanalePiccola;
		this.tempoCanaleGrande = tempoCanaleGrande;
		this.tempoStazionamentoPiccola = tempoStazionamentoPiccola;
		this.tempoStazionamentoGrande = tempoStazionamentoGrande;
		direzione = Direzione.IN;
		this.tipo = tipo;
	}
	
	public void run() {
		
	}
	
}