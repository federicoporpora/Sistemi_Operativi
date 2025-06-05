# recursion.sh dir start N "$working_dir/esito.out"
cd "$1"
X=0
for file in *
do
	if [[ -f "$file" && "$file" = $2* ]]
	then
		X=$(expr $X + 1)
	fi
done
if [[ $X<=$3 ]]
then
	echo per il direttorio $(pwd) ci sono $X file che iniziano per $2, non abbastanza, quindi mi fermo
	exit 0
fi
echo $(pwd): $X >> $4
for dir in *
do
	if [[ -d "$dir" ]]
	then
		"$0" "$(pwd)/$dir" "$2" "$3" "$4"
	fi
done
