#!bin/bash
# esame xUser yUser est dir
if [[ $# != 4 ]]
then
	echo "Errore: numero di argomenti non corretto" 
	echo  "Usage: $0 S reportFile dir" 
	exit 1
fi

if [[ $3 != ".*" ]]
then
	echo "Errore: argomento est non inizia per ."
	echo  "Usage: $0 S reportFile dir" 
	exit 1
fi

case "$4" in
/*) 
	#la directory inizia correttamente per /. Ora controllo anche che sia davvero una directory esistente  
	if ! [[ -d "$3" ]]; then
		echo "Errore terzo parametro: $3 non esiste o non è una directory" 
		exit 1
	fi
	;;
*)
	#il nome della directory non inizia per /, quindi non è un path assoluto
	echo "Errore terzo parametro: $3 non è una directory assoluta" 
	exit 1
	;;
esac

working_dir="$HOME"
> $working_dir"/report"

recfile=recursion.sh
#definizione della stringa da invocare per l'esecuzione del file comandi ricorsivo:
case "$0" in
/*) # il file comandi è stato invocato con un / Path assoluto.
	dir_name=`dirname $0`
	recursive_command="$dir_name"/$recfile
	;;
*/*) # il file comandi è stato invocato con un path relativo.
	dir_name=`dirname $0`
	recursive_command="`pwd`/$dir_name/$recfile"
;;
*) #Path né assoluto né relativo, il comando è nel $PATH
	recursive_command=$recfile
	;;
esac
#innesco la ricorsione
"$recursive_command" "$1" "$2" "$3" "$4" "$working_dir/report"
