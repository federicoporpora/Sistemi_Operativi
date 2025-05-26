#!bin/bash
# esame xUser yUser est dir dir_report
cd $4
countX=0
countY=0
for file in *
do
	if [[ -f "$file" && "$file" = "*.$3" ]]
	then
		if [[ $(stat -c %U "$(pwd)/$file") = "$xUser" ]]
		then
			countX=$(expr $countX + 1)
		elif [[ $(stat -c %U "$(pwd)/$file") = "$yUser" ]]
		then
			countY=$(expr $countY + 1)
		fi
	fi
	if [[ -d "$file" ]]
	then
		"$0" "$1" "$2" "$3" "$file" "$5"
	fi
done
if [[ $countX > $countY ]]
then
	echo "$(pwd)/$4" "$(expr $countX - $countY)" >> "$5"
fi
