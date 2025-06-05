# interfaccia: esame dir start N
# dir = directory esistente
# start = stringa di caratteri
# N = intero positivo
# vado in dir e tutti i sottodirettori, deve contare il numero di file che inizia per start, e se sono più di N analizzo anche quel sottodirettorio, altrimenti mi fermo. Inoltre devo stampare su esito.out, file creato nel direttorio in cui viene lanciato esame.sh, tutti i direttori che hanno il numero di file che inizia per start > N nella forma "<nomeAssolutoDirectory> X"

if [[ $# != 3 ]]; then
	echo "Errore: numero di argomenti non corretto" 
	echo  "Usage: $0 S reportFile dir" 
	exit 1
fi

case "$1" in
/*) 
	#la directory inizia correttamente per /. Ora controllo anche che sia davvero una directory esistente  
	if ! [[ -d "$1" ]]; then
	echo "Errore terzo parametro: $1 non esiste o non è una directory" 
	exit 1
	fi
	;;
*)
	#il nome della directory non inizia per /, quindi non è un path assoluto
	echo "Errore terzo parametro: $1 non è una directory assoluta" 
	exit 1
	;;
esac

if [[ $3 = *[!0-9]* ]] ; then
	echo "Errore parametro 3: $3 non è un intero positivo" 1>&2
	exit 1
fi

working_dir=$(pwd)
> $working_dir"/esito.out"
recfile=recursion.sh
#definizione della stringa da invocare per l'esecuzione del file comandi ricorsivo:
case "$0" in
# il file comandi è stato invocato con un / Path assoluto.
/*) 
	dir_name=`dirname $0`
	recursive_command="$dir_name"/$recfile
	;;
*/*)
	# il file comandi è stato invocato con un path relativo.
	dir_name=`dirname $0`
	recursive_command="`pwd`/$dir_name/$recfile"
	;;
*)
	#Path né assoluto né relativo, il comando è nel $PATH
	recursive_command=$recfile
	;;
esac
#innesco la ricorsione
"$recursive_command" "$1" "$2" "$3" "$working_dir/esito.out"
