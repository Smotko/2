#!/bin/bash

(( maxsofar = 0 ))
(( maxendinghere = 0 ))
while read x; do
	(( maxendinghere += x )) 
	( (( maxendinghere < 0 )) ) && maxendinghere=0
	(( maxendinghere > maxsofar )) && maxsofar=$maxendinghere
done
echo $maxsofar
