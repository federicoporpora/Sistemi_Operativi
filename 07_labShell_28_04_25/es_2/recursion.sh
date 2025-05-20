cd "$3"
count=0
fileMax=""
for file in *
do
	if [[ -f $file ]]
	then
		# $file è un file, algoritmo
		if [[ $(tail -n 20 "$file" | grep -c "$2") > $count ]]
		then
			count=$(tail -n 20 "$file" | grep -c "$2")
			fileMax=$file
		fi
	elif [[ -d $file ]]
	then
		# $file è una directory, lancio il comando ricorsivamente per analizzare la sotto-directory
		"$0" "$1" "$2" "$file"
	fi
done
echo $(pwd) $fileMax $count
