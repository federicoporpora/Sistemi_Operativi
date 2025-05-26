#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <fcntl.h>
#include <signal.h>

int pid1, pid2;
char* fout;

void usage(char* prog_name)
{
    fprintf(stderr,"Usage:\n\t%s FileName c1 [c2 ... cN]\nFileName è il nome assoluto di una file di testo.\nc1 [c2 ... cN] sono caratteri. Ciascuno identifica una specie. \n", prog_name);
}

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

// esame Fin Fout C T

void handler(int signum) {
    if(signum == SIGALRM) {
		printf("P0: timout scaduto\n");
		kill(pid1, SIGKILL);
		//exit(6);
	}
	if(signum == SIGUSR1 || signum == SIGUSR2) {
		char outStr[500];
		int out = creat(fout, 00640);
		printf("P0: ricevuto segnale SIGUSR1/2\n");
		if(out < 0) {
			printf("P0: impossibile aprire file di output\n");
			exit(7);
		}
		
		sprintf(outStr, "%s contiene un numero %spari di righe\n", fout, signum == SIGUSR1 ? "" : "dis");
		write(out, outStr, sizeof(char) * strlen(outStr));
		close(out);
		//exit(0);
	}
}

int main(int argc, char *argv[]) {
    
    if(argc != 5) {
		printf("Usage: %s Fin Fout C T\n", argv[0]);
		exit(1);
	}

    if (open(argv[1], O_RDONLY) < 0) {
        fprintf(stderr, "IL FILE NON ESISTE\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
    }

    if (open(argv[2], O_RDONLY) > 0) {
        fprintf(stderr, "IL FILE ESISTE\n");
		usage(argv[0]);
		exit(EXIT_FAILURE);
    }

    if(argv[0][0] != '/') {
        printf("%s non e' un percorso assoluto!\n", argv[3]);
        exit(-2);
    }
	if(argv[0][0] != '/') {
        printf("%s non e' un percorso assoluto!\n", argv[3]);
        exit(-2);
    }

	char c = argv[3][0];
	if(c != 'P' && c != 'D') {
		printf("C può valere solo P o D\n");
		exit(2);
	}
	
	char t = atoi(argv[4]);
	if(t <= 0) {
		printf("T deve essere un intero positivo\n");
		exit(2);
	}

    fout = argv[2];

	int fdPipe[2];
    if (pipe(fdPipe) != 0) {
        perror("pipe creation error");
		exit(EXIT_FAILURE);
    }

    if ((pid1 = fork()) == 0) {
        // codice figlio 1
        printf("Figlio 1 (PID = %d): sono stato creato", getpid());
        close(fdPipe[1]);
        int righe;
        read(fdPipe[0], &righe, sizeof(int));
        close(fdPipe[0]);
        if (righe % 2 == 0) {
            kill(getppid(), SIGUSR1);
        } else {
            kill(getppid(), SIGUSR2);
        }
    } else if (pid1 > 0) {
        printf("Padre (PID = %d): ho creato il 1° figlio (PID %d)", getpid(), pid1);
        if (pid2 = fork() == 0) {
            // codice figlio 2
            printf("Figlio 2 (PID = %d): sono stato creato", getpid());
            close(fdPipe[0]);
            int fdIn = open(argv[1], O_RDONLY);
            int righe = 0;
            char charLetto;
            while (read(fdIn, &charLetto, sizeof(char)) > 0) {
                if (charLetto == '\n') righe++;
            }
            if (charLetto != '\n') righe++;
            close(fdIn);
            write(fdPipe[1], &righe, sizeof(int));
            close(fdPipe[1]);
            exit(-1);
        } else if (pid2 > 0) {
            // codice padre
            signal(SIGUSR1, handler);
            signal(SIGUSR2, handler);
            printf("Padre (PID = %d): ho creato il 1° figlio (PID %d)", getpid(), pid1);
            close(fdPipe[0]);
            close(fdPipe[1]);
            int pidTerm1, pidTerm2, status1, status2;
            pidTerm1=wait(&status1);
            pidTerm2=wait(&status2);
            if (pidTerm1 < 0) {
                fprintf(stderr, "P0: errore in wait. \n");
                exit(EXIT_FAILURE);
            }
            if (pidTerm2 < 0) {
                fprintf(stderr, "P0: errore in wait. \n");
                exit(EXIT_FAILURE);
            }
            if (WIFEXITED(status1)) {
                printf("P0: terminazione volontaria del figlio %d con stato %d\n", pidTerm1, WEXITSTATUS(status1));
                if (WEXITSTATUS(status1) == EXIT_FAILURE){
                    fprintf(stderr, "P0: errore nella terminazione del figlio pid_terminated\n");
                    exit(EXIT_FAILURE);
                }
            } else if (WIFSIGNALED(status1)) {
                fprintf(stderr, "P0: terminazione involontaria del figlio %d a causa del segnale %d\n", pidTerm1 ,WTERMSIG(status1));
                exit(EXIT_FAILURE);
            }
            if (WIFEXITED(status2)) {
                printf("P0: terminazione volontaria del figlio %d con stato %d\n", pidTerm2, WEXITSTATUS(status2));
                if (WEXITSTATUS(status2) == EXIT_FAILURE){
                    fprintf(stderr, "P0: errore nella terminazione del figlio pid_terminated\n");
                    exit(EXIT_FAILURE);
                }
            } else if (WIFSIGNALED(status2)) {
                fprintf(stderr, "P0: terminazione involontaria del figlio %d a causa del segnale %d\n", pidTerm2,WTERMSIG(status2));
                exit(EXIT_FAILURE);
            }
    } else {
        perror("Fork error primo figlio:");
        exit(1);
    }
}
}