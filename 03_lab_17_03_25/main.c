#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <signal.h>

#define MAX_WAIT 3

void usage(char* prog_name)
{
    fprintf(stderr,"Usage:\n\t%s FileName S R\nFileName è il nome assoluto di una file di testo.\nS ed R sono interi, il primo un intero qualsiasi che indica l'attesa del figlio, il secondo è 0 o 1 ed indica il comportamento del programma stesso\n", prog_name);
}

/*
	PER COMPILARE PRIMA VADO NELLA CARTELLA, POI
		gcc nomeFile.c -o nomeFileEseguibile
		chmod u+x nomeFileEseguibile
		./nomeFileEseguibile arg1 arg2 arg3 ...
*/

/*	ERRORE GENERICO SENZA ERRORE VERO E PROPRIO DA SISTEMA OPERATIVO
		fprintf(stderr, "MESSAGGIO DI ERRORE\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
	SENNO' NEL CASO IN CUI IL SISTEMA OPERATIVO MANDI ERRORE, UTILIZZO
		perror("messaggio di errore");
		exit(1);
*/

/*
	PER RANDOM PRIMA SCRIVO
		srand(time(NULL));
	POI IL CODICE
		int r = rand() % N
	GENERA UN INTERO COMPRESO TRA 0 E N-1
*/

/*
	LA FUNZIONE int p = wait(&status) METTE IN p IL PID DEL FIGLIO E METTE NELLO STATUS LO STATO DI TERMINAZIONE CODIFICATO. PER LEGGERLO:
		if (WIFEXITED(status)) printf("%d", WEXITSTATUS(status));
		else if (WIFSIGNALED(status)) printf("%d", WTERMSIG(status))
*/

void figlio();

void handlerPadre(int signum) {
	if (signum == SIGALRM) {
		printf("Timeout scaduto!\n");
		kill(-getpid(), SIGKILL);
		exit(0);
	}
	if (signum == SIGUSR1) {
		printf("Ricevuto il segnale da P1!\n");
		exit(0);
	}
}

void main(int argc, char *argv[]) {
    
    signal(SIGALRM, handlerPadre);
    alarm(MAX_WAIT);
    
	int i, pid, wait_time, R, status;
	
	//gestione errori
	if (argv[2][0] != '0' && argv[2][0] != '1' || strlen(argv[2]) > 1) {
		fprintf(stderr, "Il secondo intero non è nel range desiderato\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
	}
	
	wait_time = atoi(argv[1]);
	R = atoi(argv[2]);
	
    pid = fork();
    if (pid == 0) { // Eseguito dai figli
		figlio(R, wait_time);
    }
    else if (pid > 0) { // Eseguito dal padre (se arriva segnale da figlio stampare "Ricevuto il segnale da P1!" e terminare, sennò se non finisce in tempo stampare "Timeout scaduto!", uccidere il figlio e terminare a sua volta)
        signal(SIGUSR1, handlerPadre);
        printf("Padre (PID = %d): ho creato il figlio (PID %d)\n", getpid(), pid);
        wait(&status);
    }
    else {
        perror("Fork error:");
        exit(1);
    }
}

void figlio(int R, int wait_time) {
	signal(SIGALRM, SIG_IGN);
	sleep(wait_time);
	if (R == 1) { //se R == 1 (lancio whoami, stampo "Comando whoami eseguito con successo" e termino)
		int temp_pid = fork();
		if (temp_pid == 0) {//eseguito da figlio del figlio, eseguo whoami
			execlp("/bin/whoami", "/bin/whoami", (char*)0);
			perror("Errore in execl\n");
			exit(0);
		}
		if (temp_pid > 0) {
			int status;
			wait(&status);
			if (WIFEXITED(status)) printf("comando whoami eseguito con successo: codice %d\n", WEXITSTATUS(status));
			else if (WIFSIGNALED(status)) printf("comando whoami non eseguito con successo: codice %d\n", WTERMSIG(status));
			exit(0);
		}
		else {
			perror("Fork error:");
        	exit(1);
		}
	}
	if (R == 0) { //se R == 0 (invio segnale a P0 e termino)
		kill(getppid(), SIGUSR1);
		exit(0);
	}
}