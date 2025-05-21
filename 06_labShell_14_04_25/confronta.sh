echo inserisci i due nomi dei file da confrontare
read f1 f2
gruppo_file1=$(stat -c %G $HOME/$f1)
gruppo_file2=$(stat -c %G $HOME/$f2)
if [[ -f $HOME/$f1 && -w $HOME/$f1 && -f $HOME/$f2 && -w $HOME/$f1 ]]
then
	echo $f1 ed $f2 sono entrambi scrivibili ed esistono;
else
	echo errore per i file $f1 ed $f2
	exit;
fi
if [[ $gruppo_file1 == $1 && $gruppo_file2 == $1 ]]
then
	echo $f1 ed $f2 hanno entrambi come gruppo proprietario $1;
else
	echo errore per i file $f1 ed $f2
	exit;
fi
dimF1=$(stat -c %s $HOME/$f1)
dimF2=$(stat -c %s $HOME/$f2)
somma=$(expr $dimF1 + $dimF2)
echo La somma delle dimensioni di $HOME/$f1 e $HOME/$f2 è $somma Byte