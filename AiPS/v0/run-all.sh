#!/bin/bash

######### generate algorithms #############################

######### big const O(n) algorithm, ~n
function gen_biglin {
cat <<\EOF >maxsum_linear.sh
#!/bin/bash

(( maxsofar = 0 ))
(( maxendinghere = 0 ))
while read x; do
	(( maxendinghere += x )) 
	( (( maxendinghere < 0 )) ) && maxendinghere=0
	(( maxendinghere > maxsofar )) && maxsofar=$maxendinghere
done
echo $maxsofar
EOF
chmod a+x maxsum_linear.sh
}


######### small const O(n^3) algorithm, 1/6n^3+1/2n^2+1/3n=~1/6n^3
function gen_smallcub {
cat <<EOF >maxsum_cubic.c
#include <stdlib.h>
#include <stdio.h>

#define MAXLEN	100000

int main(int argc, char **argv) {
	int *a = malloc(MAXLEN * sizeof(int));
	int n = -1;
	while (scanf("%d", &a[++n]) == 1);
	//for (int i = 0; i < n; i++) printf("%d ", a[i]);

	int maxsofar = 0;
	for (int i = 0; i < n; i++)
		for (int j = i; j < n; j++) {
			int sum = 0;
			for (int k = i; k <= j; k++)
				sum += a[k];
			if (sum > maxsofar) maxsofar = sum;
		}

	printf("%d\n", maxsofar);
	free(a);
}
EOF
gcc -O3 -o maxsum_cubic maxsum_cubic.c
}


######### small const O(n) algorithm, ~n
function gen_smalllin {
cat <<\EOF >maxsum_linear.c
#include <stdio.h>

int main(int argc, char **argv) {
	int maxsofar = 0;
	int maxendinghere = 0;
	int x;
	while (scanf("%d", &x) == 1) {
		maxendinghere += x;
		if (maxendinghere < 0) maxendinghere = 0;
		if (maxendinghere > maxsofar) maxsofar = maxendinghere;
	}
	printf("%d\n", maxsofar);
}
EOF
gcc -O3 -o maxsum_linear maxsum_linear.c
}


######### random stuff ####################################

# using this generator many sums are the same
function rndseq1 {
	n=$1
	while ((n > 0)); do
		echo $(( RANDOM % 100 - 50 ))
		(( n-- ))
	done
}

function rndseq2 {
# need sed: od formats negative 3-digit improperly (without leading space)
	n=$1
	od -A n -t dC -N $n /dev/random | sed -E 's/([[:digit:]])-/\1 -/g'
}


######### comparing correctness ###########################

# check if all three algorithms return the same result
function check {
	for ((i = 0; i < 100; i++)); do
		l=$(rndseq 100)
		a=$(echo $l | tr " " "\n" | ./maxsum_linear.sh)
		b=$(echo $l | ./maxsum_cubic)
		c=$(echo $l | ./maxsum_linear)
		[[ $a == $b && $b == $c ]] && continue
		echo fail
		echo $a $b
		return
	done
	echo success
}


######### parsing time ####################################

colreset='\e[0m'
colgreen='\e[0;32m'
colboldgreen='\e[1;32m'
colboldblue='\e[1;34m'

function parseout_posix {
	read sum _ r _ u _ s
	cpu=$(echo $u+$s | bc)
	printf "%7d %5.2f $colboldgreen%5.2f$colreset " $sum $r $cpu
}

function parseout_custom {
	read sum r u s
	cpu=$(echo $u+$s | bc)
	printf "%7d %6.3f $colboldgreen%6.3f$colreset " $sum $r $cpu
}


######### executing demos #################################

function show_asymp {
	gen_biglin
	gen_smallcub

	start=${1:-1000}
	stop=${2:-10000}
	step=${3:-1000}
	resultsfile=results-$start-$stop-$step.txt

	if ! test -f $resultsfile; then
		printf "%5s " n
		printf "%7s %6s %6s " lsum lreal lcpu
		printf "%7s %6s %6s " csum creal ccpu
		printf "%7s \n" speedup
		for ((n = $start; n <= $stop; n += $step)); do
			l=$(rndseq1 $n)
			printf "$colboldgreen%5d$colreset " $n
			$parseout <<< $(echo $l | tr " " "\n" | { time ./maxsum_linear.sh; } 2>&1)
			last=$cpu
			$parseout <<< $(echo $l| { time ./maxsum_cubic; } 2>&1)
			printf "$colboldblue%7.2f$colreset" $(echo "scale=2; $last/$cpu" | bc -l)
			echo
		done | tee $resultsfile
	else
		cat $resultsfile
	fi

	######### draw comparison chart

	csvfile=$(basename $resultsfile .txt).csv
	cat -t $resultsfile | sed -E -e "s/\^[^m]+m//g" | awk '{ print $1,$4,$7; }' >$csvfile

python3 <<EOF
import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv("$csvfile", sep=" ", names=["n", "linear", "cubic"])
#print(data)

plt.plot(data["n"], data["linear"], "o-", label="linear")
plt.plot(data["n"], data["cubic"], "x-", label="cubic")
plt.xlabel("Size")
plt.ylabel("Time [s]")
plt.legend(loc="upper left")
plt.show()
EOF
#	rm $csvfile
}


function show_const {
	gen_smalllin

	start=${1:-1000}
	stop=${2:-10000}
	step=${3:-1000}
	resultsfile=results2-$start-$stop-$step.txt

	if ! test -f $resultsfile; then
		printf "%5s " n
		printf "%7s %6s %6s\n" lsum lreal lcpu
		for ((n = $start; n <= $stop; n += $step)); do
			printf "$colboldgreen%5d$colreset " $n
			l=$($rndseq $n)
			$parseout <<< $(echo $l | { time ./maxsum_linear; } 2>&1)
			echo
		done | tee $resultsfile
	else
		cat $resultsfile
	fi
}


function show_help {
	echo "$(basename $0) asymp|const|clean|help"
	[[ -n $1 ]] && return
	echo -e "  asymp\t\tRun demo showing the power of asymptotics."
	echo -e "  const\t\tRun demo showing the power of constant."
	echo -e "  clean\t\tRemove temporary files, restart demos."
	echo -e "  help\t\tShow this help."
}


######### benchmark

# parsing
export LC_NUMERIC="en_US.UTF-8"
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export TIMEFORMAT='%R %U %S'
parseout=parseout_custom

# random numbers
RANDOM=$$
rndseq=rndseq2

case $1 in
	asymp|const|help)
		show_$1
	;;
	clean)
		rm -f maxsum*
		rm -f results*
	;;
	*)
		show_help short
	;;
esac

exit 0
