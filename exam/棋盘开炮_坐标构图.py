xys = [(0, 0), (0, 1), (0, 2), (1, 0), (2, 0), (3, 0)]


def solve(xys):
    mx, my = {}, {}
    for x, y in xys:
        mx[x] = mx[x] + [y] if x in mx else [y]
        my[y] = my[y] + [x] if y in my else [x]
    for k in mx.keys():
        mx[k] = sorted(mx[k])
    for k in my.keys():
        my[k] = sorted(my[k])
    for x, y in xys:
        lmx, lmy = mx[x], my[y]
        nx, ny = len(lmx), len(lmy)
        ix, iy = lmx.index(y), lmy.index(x)
        ans = 0
        if ix > 1:
            ans += 1
        if ix < nx - 2:
            ans += 1
        if iy > 1:
            ans += 1
        if iy < ny - 2:
            ans += 1
        print(ans)
