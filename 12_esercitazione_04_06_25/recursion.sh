#!/bin/bash

# esame dirin N suffix dirout

cd $1

X=0
Y=0

for file in *
do
	if [[ -f $file ]]
	then
		if [[ `stat --format%U $file` == `whoami` ]]
		then
			X=`expr $X + 1`
		fi
		if [[ $file = *$3 ]]
		then
			Y=`expr $Y + 1`
		fi
	fi
	if [[ -d $file ]]
	then
		$0 $1 $2 $3 $4
	fi
done

if [[ `expr $X + $Y` > $2 ]]
then
	echo "`pwd`/$1" $X $Y >> $4"/report.txt"
fi