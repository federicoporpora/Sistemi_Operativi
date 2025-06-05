# esame ORIGINE LOG ERR LISTA1 LISTA2 … LISTAN

ORIGINE="$1"
if ! [[ -f "$1" && "$1" = /*.txt && -r "$1" ]]
then
	echo "Errore primo parametro: $1 non è un file, non inizia per /, non finisce per .txt o non ho i permessi in lettura" 
	exit 1
fi

LOG="$2"
if ! [[ -w "$2" && -f "$2" ]]
then
	echo "Errore secondo parametro: $2 non è un file o non ho accesso in scrittura"
	exit 1
fi
> "$2"

ERR="$3"
if [[ -f "$3" && ! -w "$3" ]]
then
	echo "Errore terzo parametro: $3 esiste ma non ho i permessi di scrittura"
	exit 1
fi
> "$3"

shift 3
for lista
do
	if ! [[ -f "$lista" && -r "$lista" ]]
	then
		echo "Errore parametro $lista: non esiste, non è un file o non ho i permessi di lettura"
	fi
done

for lista
do
	for cartella in `cat "$lista"`
	do
		if [[ -d "$cartella" && -w "$cartella" ]]
		then
			cp "$ORIGINE" "$cartella"
			echo `ls $cartella` >> "$LOG"
		elif ! [[ -d "$cartella" ]]
		then
			echo la directory $cartella non è una directory esistente >> $ERR
		elif ! [[ -w "$cartella" ]]
		then
			echo non abbiamo i diritti di scrittura per la cartella $cartella dalla lista $lista >> $ERR
		fi
	done
done
