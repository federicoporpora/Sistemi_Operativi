package esameJava_11_07_22;

import java.util.concurrent.locks.*;

public class Porto {

	private int postiPiccoliTot, postiGrandiTot;
	private int postiOccupatiN, postiOccupatiM;
	private int sospEntrataPiccole, sospEntrataGrande, sospEntrataMotovedetta;
	private int sospUscitaPiccole, sospUscitaGrande, sospUscitaMotovedetta;
	
	private Lock lock;
	private Condition codaEntrataPiccole;
	private Condition codaEntrataGrande;
	private Condition codaEntrataMotovedetta;
	private Condition codaUscitaPiccole;
	private Condition codaUscitaGrande;
	private Condition codaUscitaMotovedetta;
	
	public Porto(int postiPiccoliTot, int postiGrandiTot) {
		this.postiPiccoliTot = postiPiccoliTot;
		this.postiGrandiTot = postiGrandiTot;
		this.postiOccupatiN = 0;
		this.postiOccupatiM = 0;
		this.sospEntrataPiccole = 0;
		this.sospEntrataGrande = 0;
		this.sospEntrataMotovedetta = 0;
		this.sospUscitaPiccole = 0;
		this.sospUscitaGrande = 0;
		this.sospUscitaMotovedetta = 0;
		lock = new ReentrantLock();
		codaEntrataPiccole = lock.newCondition();
		codaEntrataGrande = lock.newCondition();
		codaEntrataMotovedetta = lock.newCondition();
		codaUscitaPiccole = lock.newCondition();
		codaUscitaGrande = lock.newCondition();
		codaUscitaMotovedetta = lock.newCondition();
	}
	
	public void entraPorto() {
		
	}
	
	
}