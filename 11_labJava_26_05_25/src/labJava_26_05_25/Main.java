package labJava_26_05_25;

import java.time.Duration;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		int maxStrada = 2, maxPiazza = 5;
		int nClienti = 1, nFattorini = 1, nGuardie = 2;
		int giriGuardie = 5;
		StradaPiazza stradaPiazza = new StradaPiazza(maxStrada, maxPiazza);
		Duration tempoStradaCliente = Duration.ofSeconds(5);
		Duration tempoStradaFattorino = Duration.ofSeconds(10);
		Duration tempoStradaGuardia= Duration.ofSeconds(3);
		Duration tempoMercatoCliente = Duration.ofSeconds(10);
		Duration tempoMercatoFattorino = Duration.ofSeconds(20);
		Duration tempoMercatoGuardia= Duration.ofSeconds(7);
		
		Thread[] clienti = new Thread[nClienti];
		Thread[] fattorini = new Thread[nFattorini];
		Thread[] guardie = new Thread[nGuardie];
		
		for (int i = 0; i < nClienti; i++) {
			clienti[i] = new Cliente(stradaPiazza, tempoStradaCliente, tempoMercatoCliente);
			clienti[i].start();
		}
		System.out.println("Creati i clienti e lanciati");
		
		for (int i = 0; i < nFattorini; i++) {
			fattorini[i] = new Fattorino(stradaPiazza, tempoStradaFattorino, tempoMercatoFattorino);
			fattorini[i].start();
		}
		System.out.println("Creati i fattorini e lanciati");
		
		for (int i = 0; i < nGuardie; i++) {
			guardie[i] = new Guardia(stradaPiazza, tempoStradaGuardia, tempoMercatoGuardia, giriGuardie);
			guardie[i].start();
		}
		System.out.println("Create le guardie e lanciate");
		
		for (int i = 0; i < nClienti; i++) {
			clienti[i].join();
		}
		
		for (int i = 0; i < nFattorini; i++) {
			fattorini[i].join();;
		}
		
		for (int i = 0; i < nGuardie; i++) {
			guardie[i].join();;
		}
	}
}