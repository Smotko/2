from time import time
from random import randint


def rand_perm(n):
    """Nakljucna permutacija dolzine n"""
    perm = range(n)
    for i in range(n, 0):
        r = randint(0, i + 1)
        perm[i], perm[r] = perm[r], perm[i]
    return perm


def find(tab, key):
    """Iskanje elementa v tabeli (linearna casovna zahtevnost)"""
    for i in range(len(tab)):
        if tab[i] == key: return i
    return -1


def time_it(cnt, n):
    """Merjenje casa za cnt ponovitev algoritma"""
    starttime = time()
    for i in range(cnt):
        perm = rand_perm(n)
        key = randint(0, n - 1)
        find(perm, key)
    return time() - starttime


# Stevilo izvajanj algoritma
cnt = 1000

# Velikosti dveh nalog
n1 = 1000
n2 = 2000


print("Izvajanje pri dveh razlicnih velikostih")
t1 = time_it(cnt, n1)
print("t1 = T(%i) = %f s" % (n1, t1))
t2 = time_it(cnt, n2)
print("t2 = T(%i) = %f s" % (n2, t2))


# T(n) = a * n + b
print()
print("Napovedni model za izvajalni cas algoritma")
a = (t2 - t1) / (n2 - n1)
b = t2 - a * n2
print("T(n) = %f * n + %f" % (a, b))


print()
print("Preizkus napovednega modela")
n3 = 10000
t3calc = a * n3 + b
print("T(%i) = %f" % (n3, t3calc))
t3 = time_it(cnt, n3)
print("t3 = %f s" % t3)
