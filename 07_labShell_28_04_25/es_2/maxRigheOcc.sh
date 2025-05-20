# invoker esercizio 7.2

# test correttezza argomenti
intero=$1
stringa=$2

shift 2
if ! [[ "$intero" == [0-9]* && "$intero" != *[!0-9]* ]]
then
	echo errore parametro $intero: non è un intero, contiene un carattere non numerico
	exit 1
fi

for directory
do
	if ! [[ -d "$directory" || "$directory" == /* ]]
	then
       		echo errore parametro $directory: non esiste o non è una directory 
        	exit 1
	fi
done

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
for directory
do
	"$recursive_command" "$intero" "$stringa" "$directory"
done
