#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <fcntl.h>
#include <signal.h>

int pid[2];
int M;

void usage(char* prog_name)
{
    fprintf(stderr,"Usage:\n\t%s FileName c1 [c2 ... cN]\nFileName è il nome assoluto di una file di testo.\nc1 [c2 ... cN] sono caratteri. Ciascuno identifica una specie. \n", prog_name);
}

void handler(int signum) {
	if (signum == SIGUSR1) {
		printf("Sono P2. il mio PID è %d. Il numero di occorrenze è divisibile per %d\n", getpid(), M);
	} else {
		printf("Sono P2. il mio PID è %d. Il numero di occorrenze NON è divisibile per %d\n", getpid(), M);
	}
}

/*
	PER COMPILARE PRIMA VADO NELLA CARTELLA, POI
		gcc nomeFile.c -o nomeFileEseguibile
		chmod u+x nomeFileEseguibile
		./nomeFileEseguibile arg1 arg2 arg3 ...
*/

// Fin path assoluto file di testo già esistente
// parola stringa di caratteri
// M numero intero positivo

int main(int argc, char *argv[]) {
    
	int i, status, fdFin;

	if(argc != 4) {
		printf("Usage: %s Fin Fout C T\n", argv[0]);
		exit(1);
	}

	if (argv[1][0] != '/') {
		fprintf(stderr, "Fin non è un path assoluto\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
	}

    if ((fdFin = open(argv[1], O_RDONLY)) == -1) {
		perror("Fin non è un file esistente");
		exit(1);
	}
	close(fdFin);

	M = atoi(argv[3]);

	int fdPipe[2];
    if (pipe(fdPipe) != 0) {
        perror("pipe creation error");
		exit(EXIT_FAILURE);
    }

	for (i = 0; i < 2; i++) {
        pid[i] = fork();
        if (pid[i] == 0 && i == 0) { // Eseguito da P1
			printf("P1 (PID = %d): sono stato creato\n", getpid());
			close(fdPipe[0]);
			close(1);
			dup(fdPipe[1]);
			close(fdPipe[1]);
			execlp("grep", "grep", "-c", argv[2], argv[1], NULL);
			perror("Errore in grep -c");
            exit(1);
        }
        else if (pid[i] == 0 && i == 1) { // Eseguito da P2
			printf("P2 (PID = %d): sono stato creato\n", getpid());
			signal(SIGUSR1, handler);
			signal(SIGUSR2, handler);
			close(fdPipe[0]);
			close(fdPipe[1]);
			pause();
            exit(0);
        }
        else if (pid[i] > 0) { // Eseguito dal padre
            printf("Padre (PID = %d): ho creato P%d (PID %d)\n", getpid(), i+1, pid[i]);
        }
        else {
            perror("Fork error:");
            exit(1);
        }
    }
	
	close(fdPipe[1]);	
	int X;
	read(fdPipe[0], &X, sizeof(int));
	if (X % M == 0) kill(pid[1], SIGUSR1);
	else kill(pid[1], SIGUSR2);

    for ( i=0; i < 2; i++ ) { // attesa figli
        int p = wait(&status);
        if (WIFEXITED(status)) {
            printf("Padre (PID = %d): il figlio (PID = %d) è uscito con codice %d.\n", getpid(), p, WEXITSTATUS(status));
        } else 
            printf("Padre (PID = %d): term. involontaria del figlio (PID = %d), segnale %d.\n", getpid(), p, WTERMSIG(status));
    }
	
}