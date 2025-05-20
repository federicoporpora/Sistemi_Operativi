#!/bin/bash
# file comandi ricorsivo dell'esercizio 7.1 (LZ) 
# invocato con "$recursive_command" S reportFile dir 

cd "$3"
count=0
for file in * ; do    #per ogni elemento contenuto nella directory corrente:
    if test -f "$file" ; then #$file è un file ordinario, non una directory
		#conto le occorrenze di S nel file
		X=`grep -o "$1" "$file"| wc -l`
		if [[ $X -gt 3 ]];then # se ci sono più di 3 occorrenze, incremento il conteggio
		    echo Il file `pwd`/$file contiene $X occorrenze di $3 #stampa di controllo
		    count=`expr $count + 1` 
		fi
    elif test -d "$file"; then  #file è una directory
        echo "Invocazione ricorsiva sulla directory `pwd`/$file"
        "$0" "$1" "$2" "$file" #invocazione ricorsiva
    fi
done

echo Directory `pwd`: ci sono $count file con almeno 3 occorrenze della stringa $1 >> "$2"
