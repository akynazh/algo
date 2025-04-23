import sys

"""
3 10 10 10 # 建筑数、材料 A 数、材料 B 数、材料 C 数
2 5 8 5 # 需要材料 A 数、需要材料 B 数、需要材料 C 数、价值
4 5 6 8 # 需要材料 A 数、需要材料 B 数、需要材料 C 数、价值
6 2 3 3 # 需要材料 A 数、需要材料 B 数、需要材料 C 数、价值
求最大价值
"""

mat = []
mat = [[3, 10, 10, 10], [2, 5, 8, 5], [4, 5, 6, 8], [6, 2, 3, 3]]
# for line in sys.stdin:
#     a = line.split()
#     mat.append(a)
for i in range(len(mat)):
    for j in range(len(mat[i])):
        mat[i][j] = int(mat[i][j])
n = mat[0][0]
v1 = mat[0][1]
v2 = mat[0][2]
v3 = mat[0][3]
profit = [arc[3] for arc in mat[1:]]
pay_mat = [arc[:3] for arc in mat[1:]]
max_profit = -1
profit_mat3 = [
    [[0 for _ in range(v3 + 1)] for _ in range(v2 + 1)] for _ in range(v1 + 1)
]
for i in range(v1 + 1):
    for j in range(v2 + 1):
        for k in range(v3 + 1):
            for u in range(len(pay_mat)):
                p = pay_mat[u]
                if i >= p[0] and j >= p[1] and k >= p[2]:
                    profit_mat3[i][j][k] = max(
                        profit_mat3[i][j][k],
                        profit[u] + profit_mat3[i - p[0]][j - p[1]][k - p[2]],
                    )
                    max_profit = max(max_profit, profit_mat3[i][j][k])
print(max_profit)
