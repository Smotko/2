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
