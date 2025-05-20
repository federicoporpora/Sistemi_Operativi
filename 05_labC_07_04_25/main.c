#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <time.h>
#include <signal.h>
#include <ctype.h>
#include <sys/wait.h>
#include <sys/types.h>

int pid1,pid2;
	
typedef struct{
	int pos; 
	char cA; 
	char cB;  
} element;

void wait_child(){
	int status, pid_terminated=wait(&status);
	if ((char)status==0) 
		printf("P0(pid=%d): figlio con PID=%d terminato volontariamente con stato %d.\n",getpid(),pid_terminated, status>>8);
	else 
		printf("P0(pid=%d): figlio con PID=%d terminato involontariamente per segnale %d\n",getpid(),pid_terminated,(char)status);
}

void print_usage(char* prog_name){
	fprintf(stderr, "Usage:\n\t%s X Fin\nX deve essere un intero positivo\nFin deve essere il nome assoluto di un file esistente nel file system\n", prog_name);
}

void check_is_number(char* str, char* prog_name){
	for(int i=0; i<strlen(str);i++){
		if (!isdigit(str[i])){
			fprintf(stderr, "Il parametro passato '%s' non è un numero intero positivo.\n",str);
			print_usage(prog_name);
			exit(EXIT_FAILURE);
		}
	}
}

int main(int argc, char* argv[]) {
	/*controllo parametri in ingresso*/
	if ( argc != 4 ) {
		perror("Numero di parametri non valido.");
		print_usage(argv[0]);
		exit(EXIT_FAILURE);
	}
	/*if (argv[1][0]!='/'){ 
		//se il parametro passato non inizia per '/' allora non è un nome assoluto
		perror("il primo parametro non è un nome assoluto.\n");
		print_usage(argv[0]);
		exit(EXIT_FAILURE);
	}*/
	if (access(argv[1], F_OK) != 0) {
		perror("il primo parametro non è un file esistente.\n");
		print_usage(argv[0]);
		exit(EXIT_FAILURE);
	}
	/*if (argv[2][0]!='/'){ 
		//se il parametro passato non inizia per '/' allora non è un nome assoluto
		perror("il secondo parametro non è un nome assoluto.\n");
		print_usage(argv[0]);
		exit(EXIT_FAILURE);
	}*/
	if (access(argv[2], F_OK) != 0) {
		perror("il secondo parametro non è un file esistente.\n");
		print_usage(argv[0]);
		exit(EXIT_FAILURE);
	}
	check_is_number(argv[3], argv[0]);
	int X=atoi(argv[3]);

	/*esecuzione*/
	int fdArr[2];
	if (pipe(fdArr) != 0) {
		perror("pipe creation error");
		print_usage(argv[0]);
		exit(EXIT_FAILURE);
	}
	pid1 = fork();
	if (pid1 == 0) {
		//P1
		printf("P1(pid=%d): sono stato creato.\n", getpid());
		close(fdArr[1]);
		element el;
		while (read(fdArr[0], &el, sizeof(element)) > 0) {
			printf("P1: trovata una differenza in posizione %d: cA=%c cB=%c\n", el.pos, el.cA, el.cB);
		}
		close(fdArr[0]);
		exit(EXIT_SUCCESS);
	} else if (pid1>0) {
		pid2=fork();
		if (pid2 == 0) { 
			//P2
			printf("P2(pid=%d): sono stato creato.\n", getpid());
			//apertura del fileA di input
			int fdA = open(argv[1], O_RDONLY);
			if (fdA < 0){
				fprintf(stderr,"P2: Errore nell'apertura di %s.\n", argv[1]);
				exit(EXIT_FAILURE);
			}
			//apertura del fileB di input
			int fdB = open(argv[2], O_RDONLY);
			if (fdB < 0) {
				fprintf(stderr,"P2: Errore nell'apertura di %s.\n", argv[2]);
				exit(EXIT_FAILURE);
			}

			//lettura dei due file e scrittura nel buffer
			int pos=0;
			char cA,cB;
			element el;
			
			while(read(fdA,&cA,sizeof(char))>0){
				if (read(fdB,&cB,sizeof(char))>0){
					if(cA!=cB){
						el.pos=pos;
						el.cA=cA;
						el.cB=cB;
						write(fdArr[1],&el,sizeof(element));
					}
				}
				pos+=X;
				lseek(fdA,X-1,SEEK_CUR); //oppure: lseek(fdA,pos,SEEK_SET);
				//NB: nell'espressione precedente uso X-1 poichè la read ha già spostato il file descriptor avanti 
				//di una unità, ora devo muovermi di altre X-1 per raggiungere il prossimo char
				lseek(fdB,X-1,SEEK_CUR);
			}
			close(fdA);
			close(fdA);
			close(fdArr[0]);
			close(fdArr[1]);
			printf("P1(pid=%d): ho chiuso tutto, ora ritorno.\n", getpid());
			exit(EXIT_SUCCESS);
		} else if (pid2>0) { 
			//P0
			printf("P0(pid=%d): ho creato i figli P1(pid=%d) e P2(pid=%d).\n", getpid(), pid1, pid2);
			close(fdArr[0]);
			printf("P0(pid=%d): ho chiuso la pipe per me, ora aspetto la terminazione dei figli.\n", getpid());
			//attendo la terminazione dei figli 
			wait_child();
			close(fdArr[1]);
			wait_child();
		} else {
			fprintf(stderr, "Errore nella seconda fork() eseguita da P0.\n");
			exit(EXIT_FAILURE);
		}
	} else {
		fprintf(stderr, "Errore nella prima fork() eseguita da P0.\n");
		exit(EXIT_FAILURE);
	}
}