#!/bin/bash

# esame dirin N suffix dirout

# dirin è il nome assoluto di una directory esistente nel file system
# N è un intero positivo
# suffix è una stringa di caratteri
# dirout è il nome assoluto di una directory NON esistente nel file system

# Dopo aver effettuato gli opportuni controlli dei parametri in ingresso, il file comandi dovrà scandire ricorsivamente il sottoalbero
# individuato da dirin.
# Per ogni sottocartella trovata (inclusa la cartella dirin stessa) lo script deve contare:
	# il numero X di regular file di proprietà dell’utente che esegue lo script
	# il numero Y di regular file il cui nome termina per suffix
# Inoltre, qualora X+Y>N, lo script dovrà aggiungere al file report.txt nella
# cartella dirout una riga con il seguente formato:
	# <nomeAssDirectory> <X> <Y>
#Dove:
	# < nomeAssDirectory > è il nome assoluto della directory il cui contenuto rispetta la specifica precedente (X+Y>N)
	# <X> e <Y> sono i risultati dei conteggi sopracitati.

if [[ $# != 4 ]]; then
	echo "Errore: numero di argomenti non corretto" 
	echo  "Usage: $0 S reportFile dir" 
	exit 1
fi

case "$1" in
/*) #la directory inizia correttamente per /. Ora controllo anche che sia davvero una directory esistente  
	if ! [[ -d "$1" ]]; then
		echo "Errore primo parametro: $1 non esiste o non è una directory" 
		exit 1
	fi
;;
*) #il nome della directory non inizia per /, quindi non è un path assoluto
	echo "Errore primo parametro: $1 non è una directory assoluta" 
	exit 1
	;;
esac

if [[ $2 = *[!0-9]* ]] ; then
	echo "Errore secondo parametro: $2 non è un intero positivo" 1>&2
	exit 1
fi

case "$4" in
/*) #la directory inizia correttamente per /. Ora controllo anche che sia davvero una directory esistente  
	if [[ -d "$4" ]]; then
		echo "Errore quarto parametro: $4 è una directory esistente" 
		exit 1
	fi
;;
*) #il nome della directory non inizia per /, quindi non è un path assoluto
	echo "Errore quarto parametro: $4 non è una directory assoluta" 
	exit 1
	;;
esac

> $4"/report.txt"

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
"$recursive_command" "$1" "$2" "$3" "$4"