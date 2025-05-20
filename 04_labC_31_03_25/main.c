#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <signal.h>
#include <fcntl.h>
#define N 2

typedef struct {
	int pos;
	char cA;
	char cB;
} elemento;

int fdtemp, pid[N];

void usage(char* prog_name)
{
    fprintf(stderr,"Usage:\n\t%s FileName c1 [c2 ... cN]\nFileName è il nome assoluto di una file di testo.\nc1 [c2 ... cN] sono caratteri. Ciascuno identifica una specie. \n", prog_name);
}

int isAnInt(char* string) {
	for (int i = 0; i < strlen(string); i++) {
		if (string[i] < '0' || string[i] > '9') return 0;
	}
	return 1;
}

void handler(int signum) {
	if ((fdtemp = open("Fdiff", O_RDONLY)) == -1) {
		perror("ERRORE IN APERTURA DEL FILE TEMPORANEO");
		exit(1);
	}
	elemento tempP1;
	while (read(fdtemp, &tempP1, sizeof(elemento)) > 0) {
		printf("\nDifferenza alla posizione %d tra i caratteri %c e %c", tempP1.pos, tempP1.cA, tempP1.cB);
	}
}

int main(int argc, char *argv[]) {
    
    int i, status, multiplier, fd1, fd2, pos = 0;
    char temp1, temp2;
    elemento temp;
    
    //check vari
    if (argc != 4) {
    	fprintf(stderr, "IL NUMERO DI ARGOMENI PASSATI È %d INVECE DI 3\n", argc - 1);
		usage(argv[0]);
		exit(EXIT_FAILURE);
    }
	if (!isAnInt(argv[3])) {
		fprintf(stderr, "L'ARGOMENTO PASSATO %s NON È UN INTERO\n", argv[3]);
		usage(argv[0]);
		exit(EXIT_FAILURE);
	}
	if ((fd1 = open(argv[1], O_RDONLY)) == -1) {
		perror("ERRORE IN APERTURA DEL PRIMO FILE");
		exit(1);
	}
	if ((fd2 = open(argv[2], O_RDONLY)) == -1) {
		perror("ERRORE IN APERTURA DEL SECONDO FILE");
		exit(1);
	}
	multiplier = atoi(argv[3]);
	
	//algoritmo padre e figli
	for (i = 0; i < N; i++) {
        pid[i] = fork();
        if (pid[i] == 0 && i == 0) { // eseguito da P1 (appena arriva il segnale da P2 apre l'handler)
			printf("\nsono il figlio P1 (PID = %d)\n", getpid());
			signal(SIGUSR1, handler);
			pause();
			exit(0);
        }
        else if (pid[i] == 0 && i == 1) { // eseguito da P2 (controlla differenze in TESTa e TESTb e le scrive su un file temporaneo)
        	printf("\nsono il figlio P2 (PID = %d)", getpid());
        	if ((fdtemp = creat("Fdiff", 0777)) == -1) {
        		perror("\nERRORE IN CREAZIONE DEL FILE TEMPORANEO");
        		exit(1);
        	}
        	while (read(fd1, &temp1, 1) > 0 && read(fd2, &temp2, 1) > 0) {
        		pos++;
        		if (pos % multiplier == 0) {
        			temp.pos = pos;
        			temp.cA = temp1;
        			temp.cB = temp2;
        			if (temp1 != temp2) {
        				if (write(fdtemp, &temp, sizeof(elemento)) <= 0) {
        					perror("\nERRORE NELLA SCRITTURA DELL'ELEMENTO");
        					exit(1);
        				}
        			}
        		}
        	}
        	kill(pid[0], SIGUSR1);
        	exit(0);
        }
        else if (pid[i] > 0) { // eseguito dal padre
            printf("\nPadre (PID = %d): ho creato il %d° figlio (PID %d)", getpid(), i+1, pid[i]);
        }
        else {
            perror("Fork error:");
            exit(1);
        }
    }
    
    // chiusura dei file
	if (close(fd1) < 0) {
		perror("\nERRORE IN CHIUSURA DEL PRIMO FILE");
		exit(1);
	}
	if (close(fd2) < 0) {
		perror("\nERRORE IN CHIUSURA DEL SECONDO FILE");
		exit(1);
	}
	
	// attesa figli
    for (i = 0; i < N; i++) {
        int p = wait(&status);
        if (WIFSIGNALED(status))
            printf("\nPadre (PID = %d): term. involontaria del figlio (PID = %d), segnale %d.", getpid(), p, WTERMSIG(status));
    }
    
    // cancellazione file temporaneo
	if (unlink("Fdiff") != 0) {
		perror("\nERRORE IN CANCELLAZIONE DEL FILE TEMPORANEO");
		exit(1);
	}
}