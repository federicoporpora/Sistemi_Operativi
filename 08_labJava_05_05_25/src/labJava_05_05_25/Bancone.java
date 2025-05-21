package labJava_05_05_25;

public class Bancone {
	
	private int totCamicie, totNormali, camicieLavate, normaliLavati, camicieStirate, normaliStirati;
	
	public Bancone() {
		this.totCamicie = 0;
		this.totNormali = 0;
		this.camicieLavate = 0;
		this.normaliLavati = 0;
		this.camicieStirate = 0;
		this.normaliStirati = 0;
	}
	public int getTotCamicie() {
		return totCamicie;
	}
	public int getTotNormali() {
		return totNormali;
	}
	public synchronized void depositaCapiPuliti(int camicieLavate, int normaliLavati) {
		this.camicieLavate += camicieLavate;
		this.totCamicie += camicieLavate;
		this.normaliLavati += normaliLavati;
		this.totNormali += normaliLavati;
	}
	public synchronized boolean stiraCamicia() {
		if (camicieLavate > 0) {
			camicieLavate--;
			return true;
		}
		return false;
	}
	public synchronized boolean stiraNormale() {
		if (normaliLavati > 0) {
			normaliLavati--;
			return true;
		}
		return false;
	}
	public synchronized void stampa() {
		System.out.println("Camicie = " + totCamicie + "\tNormali = " + totNormali);
		System.out.println("Camicie lavate = " + camicieLavate + "\tNormali lavati = " + normaliLavati);
		System.out.println("Camicie stirate = " + camicieStirate + "\tNormali stirati = " + normaliStirati);
	}
	public int getCamicieStirate() {
		return camicieStirate;
	}
	public void setCamicieStirate(int camicieStirate) {
		this.camicieStirate = camicieStirate;
	}
	public int getNormaliStirati() {
		return normaliStirati;
	}
	public void setNormaliStirati(int normaliStirati) {
		this.normaliStirati = normaliStirati;
	}
}