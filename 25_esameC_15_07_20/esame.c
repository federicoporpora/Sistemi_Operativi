#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <fcntl.h>
#include <signal.h>
#define N 2

int pid[N];

void usage(char* prog_name)
{
    fprintf(stderr,"Usage:\n\t%s FileName c1 [c2 ... cN]\nFileName è il nome assoluto di una file di testo.\nc1 [c2 ... cN] sono caratteri. Ciascuno identifica una specie. \n", prog_name);
}
void figlio(int i, int *fdPipe, int fdFin, char C, char S, char *word);

/*
	PER COMPILARE PRIMA VADO NELLA CARTELLA, POI
		gcc nomeFile.c -o nomeFileEseguibile
		chmod u+x nomeFileEseguibile
		./nomeFileEseguibile arg1 arg2 arg3 ...
*/

/*	ERRORE GENERICO SENZA ERRORE VERO E PROPRIO DA SO
		fprintf(stderr, "MESSAGGIO DI ERRORE\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
	SENNO' UTILIZZO
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

int main(int argc, char *argv[]) {
    
	int i, status, fdFin, fdPipe[2];

    // esame Fin C S word
    // Fin = nome assoluto file di testo in ingresso
    // C ed S sono due caratteri
    // word è una stringa

    if (argc != 5) {
        fprintf(stderr, "NUMERO DI ARGOMENTI ERRATO\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
    }

    if (argv[1][0] != '/' || ((fdFin = open(argv[1], O_RDONLY)) < 0)) {
        fprintf(stderr, "FIN NON È UN PATH ASSOLUTO O NON ESISTE\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
    }

    if (strlen(argv[2]) != 1 || strlen(argv[3]) != 1) {
        fprintf(stderr, "LA LUNGHEZZA DI C O S NON È 1\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
    }
	
    if (pipe(fdPipe) != 0) {
        perror("pipe creation error");
		exit(EXIT_FAILURE);
    }

	for (i = 0; i < N; i++) {
        pid[i] = fork();
        if (pid[i] == 0) {
			figlio(i, fdPipe, fdFin, argv[2][0], argv[3][0], argv[4]);
        } else if (pid[i] > 0) {
            printf("Padre (PID = %d): ho creato il %d° figlio (PID %d)\n", getpid(), i+1, pid[i]);
        }
        else {
            perror("Fork error:");
            exit(1);
        }
    }

    close(fdPipe[0]);
    close(fdPipe[1]);
    close(fdFin);

    for (i = 0; i < N; i++ ) { // attesa figli
        int p = wait(&status);
        if (WIFEXITED(status)) {
            printf("Padre (PID = %d): il figlio (PID = %d) è uscito con codice %d.\n", getpid(), p, WEXITSTATUS(status));
        } else 
            printf("Padre (PID = %d): term. involontaria del figlio (PID = %d), segnale %d.\n", getpid(), p, WTERMSIG(status));
    }
	
}

// P2 controlla se il suo PID è pari. Se è pari deve leggere il contenuto del file Fin e mandarlo
// a P1 sostituendo ogni occorrenza di C con S
// Se il PID è dispari glielo manda così com'è

// P1 deve filtrare il file ricevuto da P2 con grep stampando a video le sole righe che contengono
// almeno un'occorrenza di word

void figlio(int i, int *fdPipe, int fdFin, char C, char S, char *word) {
	printf("%d° figlio creato, sono dentro alla funzione\n", i + 1);
    if (i == 0) { // P1
        close(fdPipe[1]);
        close(0);
        dup(fdPipe[0]);
        close(fdPipe[0]);
        close(fdFin);
        execlp("grep", "grep", word, NULL);
        perror("execlp");
        exit(1);
    } else { // P2
        char letto;
        close(fdPipe[0]);
        if (getpid() % 2 == 0) { // pid pari
            while (read(fdFin, &letto, sizeof(char)) != 0) {
                if (letto == C) letto = S;
                write(fdPipe[1], &letto, sizeof(char));
            }
        } else { // pid dispari
            while (read(fdFin, &letto, sizeof(char)) != 0) {
                write(fdPipe[1], &letto, sizeof(char));
            }
        }
        close(fdPipe[1]);
        close(fdFin);
    }
    printf("%d° figlio (PID = %d), ritorno con codice 0\n", i+1, getpid());
	exit(0);
}