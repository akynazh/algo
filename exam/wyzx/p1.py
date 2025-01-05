"""
二维动态规划
消耗+收益
"""

t = input().split(" ")
n, m = int(t[0]), int(t[1])
consume = [[0 for _ in range(m)] for _ in range(n)]
profit = [[0 for _ in range(m)] for _ in range(n)]

for i in range(n):
    t = input()
    if t.strip() == "":
        t = input()
    t = t.split(" ")
    for j in range(m):
        consume[i][j] = int(t[j])
for i in range(n):
    t = input()
    if t.strip() == "":
        t = input()
    t = t.split(" ")
    for j in range(m):
        profit[i][j] = int(t[j])

# def dfs(x, y, c, p):
#     global res_c, res_p
#     if x == n - 1 and y == m - 1:
#         if c <= res_c:
#             res_c = c
#             res_p = max(res_p, p)
#         return
#     if c > res_c:
#         return
#     if x + 1 < n:
#         dfs(x + 1, y, c + consume[x + 1][y], p + profit[x + 1][y])
#     if y + 1 < m:
#         dfs(x, y + 1, c + consume[x][y + 1], p + profit[x][y + 1])

res_c, res_p = 2**100, 0
# dfs(0, 0, consume[0][0], profit[0][0])
dp = [[[0, 0] for _ in range(m)] for _ in range(n)]
dp[0][0][0] = consume[0][0]
dp[0][0][1] = profit[0][0]
for i in range(1, m):
    dp[0][i][0] = consume[0][i] + dp[0][i - 1][0]
    dp[0][i][1] = profit[0][i] + dp[0][i - 1][1]
for i in range(1, n):
    dp[i][0][0] = consume[i][0] + dp[i - 1][0][0]
    dp[i][0][1] = profit[i][0] + dp[i - 1][0][1]
for i in range(1, n):
    for j in range(1, m):
        x, x1 = dp[i - 1][j][0], dp[i - 1][j][1]
        y, y1 = dp[i][j - 1][0], dp[i][j - 1][1]
        if x > y:
            dp[i][j][0] = y + consume[i][j]
            dp[i][j][1] = y1 + profit[i][j]
        elif x < y:
            dp[i][j][0] = x + consume[i][j]
            dp[i][j][1] = x1 + profit[i][j]
        else:
            dp[i][j][0] = x + consume[i][j]
            dp[i][j][1] = max(x1 + profit[i][j], y1 + profit[i][j])


print(dp[n - 1][m - 1][0], dp[n - 1][m - 1][1])
