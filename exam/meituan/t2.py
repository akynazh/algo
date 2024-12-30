def mex(arr):
    for i in range(max(arr) + 2):
        if i not in arr:
            return i


def solve(n, k, x, arr):
    cur_mex = mex(arr)
    mx = k * cur_mex
    for i in range(len(arr)):
        v = arr[i]
        if v < cur_mex:
            cur_mex = v
        t = (i + 1) * x + k * cur_mex
        if t < mx:
            mx = t
    return mx


T = int(input())
for _ in range(T):
    t = input().split(" ")
    n, k, x = int(t[0]), int(t[1]), int(t[2])
    t = input().split(" ")
    arr = [int(s) for s in t]
    print(solve(n, k, x, arr))
