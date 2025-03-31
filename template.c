#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <signal.h>
#define N 100

int pid[N];

void usage(char* prog_name)
{
    fprintf(stderr,"Usage:\n\t%s FileName c1 [c2 ... cN]\nFileName è il nome assoluto di una file di testo.\nc1 [c2 ... cN] sono caratteri. Ciascuno identifica una specie. \n", prog_name);
}
void figlio(int i);

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

int main(int argc, char *argv[]) {
    
	int i, status;
	
	printf("\n-------------------------------------------");
	printf("\nIl codice per ogni stringa di argomenti crea un figlio, ed ognuno ritorna con valori incrementali di multipli di 10.");
	printf("\n-------------------------------------------\n\n");
	
	for (i = 0; i < argc - 1; i++) { // creazione figli, uno per argomento in argv
        pid[i] = fork();
        if (pid[i] == 0) { // Eseguito dai figli
			figlio(i);
        }
        else if (pid[i] > 0) { // Eseguito dal padre
            printf("Padre (PID = %d): ho creato il %d° figlio (PID %d)\n", getpid(), i+1, pid[i]);
        }
        else {
            perror("Fork error:");
            exit(1);
        }
    }
	
    for ( i=0; i < argc - 1; i++ ) { // attesa figli
        int p = wait(&status);
        if (WIFEXITED(status)) {
            printf("Padre (PID = %d): il figlio (PID = %d) è uscito con codice %d.\n", getpid(), p, WEXITSTATUS(status));
        } else 
            printf("Padre (PID = %d): term. involontaria del figlio (PID = %d), segnale %d.\n", getpid(), p, WTERMSIG(status));
    }
	printf("\nfinito!!!\n\n\n");
}

void figlio(int i) {
	//algoritmo del figlio
	int code = i*10;
	printf("%d° figlio (PID = %d), ritorno con codice %d\n", i+1, getpid(), code);
	exit(code);
}