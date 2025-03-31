#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#define MAX 26
#define N 20

void main(int argc, char *argv[]) {
	
	int i, j, sum = 0, status, pid[argc - 2];
	char V[N];
	
	srand(time(NULL));
	
	//vari check
	if (argc < 3) {
		perror("non sono stati passati abbastanza argomenti");
		exit(1);
	}	
	for (i = 2; i < argc; i++) {
		if (strlen(argv[i]) > 1 || argv[i][0] < 'a' || argv[i][0] > 'z') {
			perror("uno degli argomenti non è un singolo carattere");
			exit(1);
		}
	}
	
	//esercizio vero e proprio
	//generazione array casuale
	printf("ARRAY:\n");
	for (i = 0; i < N; i++) {
		V[i] = (char) (rand() % MAX) + 97;
		printf("%c\t", V[i]);
	}
	printf("\n\n");
	
	//generazione figli
	for (i = 0; i < argc - 2; i++) {
		pid[i] = fork();
		if (pid[i] == 0) {
			for (j = 0; j < N; j++) {
			if (V[j] == argv[i + 2][0]) sum++;
			}
			exit(sum);
		}
		else if (pid[i] > 0) {
			printf("Padre (pid %d), ho creato il %d° figlio (pid %d), si occuperà dei cetacei di codice %c", getpid(), i+1, pid[i], argv[i + 2][0]);
			pid[i] = wait(&status);
			if (WIFEXITED(status)) printf("specie %c trovate: %d\n", argv[i + 2][0], WEXITSTATUS(status));
			if (WIFSIGNALED(status)) {
				perror("errore in uscita da un figlio");
			}
		}
		else {
			perror("fork error");
			exit(1);
		}
	}	
}