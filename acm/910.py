# greedy !!
def solve(n, d, s):
    next = [i for i in range(n) if s[i] == "1"]
    mx = len(next) - 1
    for i in range(mx):
        if next[i + 1] - next[i] > d:
            return -1
    c = 0
    s = 0
    while s < mx:
        sx = s + 1
        while sx < mx + 1:
            if next[sx] - next[s] <= d:
                sx += 1
            else:
                sx -= 1
                break
        c += 1
        s = sx
    return c


t = input().split()
n, d = int(t[0]), int(t[1])
s = input()
print(solve(n, d, s))
