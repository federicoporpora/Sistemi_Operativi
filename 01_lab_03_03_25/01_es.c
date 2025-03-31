#include <stdio.h>
#include <string.h>

int isDigit(char digit);

int main(int argc, char *argv[]) {

	if (argc <= 1) {
		printf("Non è stata passata nessuna attività\n");
		return -1;
	}

	int numM = 0, numF = 0, numU = 0;

	for (int i = 1; i < argc; i++) {
		int lenght = strlen(argv[i]);
		if (lenght != 7) {
			printf("L'attività %s non ha 7 caratteri\n", argv[i]);
			return -1;
		}
		for (int j = 0; j < 7; j++) {
			if (j == 3) {
				if (argv[i][j] != 'M' && argv[i][j] != 'F' && argv[i][j] != 'U') {
					printf("Il %d° carattere (%c) nella stringa %s non è M, F o U\n", j, argv[i][j], argv[i]);
					return -1;
				}
			}
			if (j >= 4 && j < 7) {
				if (!isDigit(argv[i][j])) {
					printf("Il %d carattere (%c) nella stringa %s non è un numero\n", j, argv[i][j], argv[i]);
					return -1;
				}
			}
		}
		if (argv[i][3] == 'M') numM += (((int) argv[i][4] - '0')*100 + ((int) argv[i][5] - '0')*10 + ((int)argv[i][6] - '0'));
		if (argv[i][3] == 'F') numF += (((int) argv[i][4] - '0')*100 + ((int) argv[i][5] - '0')*10 + ((int)argv[i][6] - '0'));
		if (argv[i][3] == 'U') numU += (((int) argv[i][4] - '0')*100 + ((int) argv[i][5] - '0')*10 + ((int)argv[i][6] - '0'));
	}
	printf("Att. maschili (partecipanti %d)\n", numM);
	for (int i = 1; i < argc; i++) {
		if (argv[i][3] == 'M') printf("%c%c%c\n", argv[i][0], argv[i][1], argv[i][2]);
	}
	printf("Att femminili (partecipanti %d)\n", numF);
	for (int i = 1; i < argc; i++) {
		if (argv[i][3] == 'F') printf("%c%c%c\n", argv[i][0], argv[i][1], argv[i][2]);
	}
	printf("Att miste (partecipanti %d)\n", numU);
	for (int i = 1; i < argc; i++) {
		if (argv[i][3] == 'U') printf("%c%c%c\n", argv[i][0], argv[i][1], argv[i][2]);
	}
	return 0;
}

int isDigit(char digit) {
	return (digit >= '0' && digit <= '9') ? 1 : 0;
}