n = 100
f = [[float("inf") for _ in range(n + 1)] for _ in range(n + 1)]

for k in range(1, n + 1):
    for x in range(1, n + 1):
        for y in range(1, n + 1):
            f[x][y] = min(f[x][y], f[x][k] + f[k][y])
