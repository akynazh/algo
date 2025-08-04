# 原始矩阵
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]

m, n = len(matrix), len(matrix[0])

# 构建二维差分数组
diff = [[0] * (n + 1) for _ in range(m + 1)]

# 初始化差分数组
for i in range(m):
    for j in range(n):
        diff[i][j] = matrix[i][j]
        if i > 0:
            diff[i][j] -= matrix[i - 1][j]
        if j > 0:
            diff[i][j] -= matrix[i][j - 1]
        if i > 0 and j > 0:
            diff[i][j] += matrix[i - 1][j - 1]


# 区域加法
def range_add_2d(x1, y1, x2, y2, val):
    diff[x1][y1] += val
    diff[x1][y2 + 1] -= val
    diff[x2 + 1][y1] -= val
    diff[x2 + 1][y2 + 1] += val


# 应用：给右下 2x2 矩阵加 10
range_add_2d(1, 1, 2, 2, 10)

# 还原矩阵（二维前缀和）
res = [[0] * n for _ in range(m)]
for i in range(m):
    for j in range(n):
        res[i][j] = diff[i][j]
        if i > 0:
            res[i][j] += res[i - 1][j]
        if j > 0:
            res[i][j] += res[i][j - 1]
        if i > 0 and j > 0:
            res[i][j] -= res[i - 1][j - 1]

for row in res:
    print(row)
# 输出:
# [1, 2, 3]
# [4, 15, 16]
# [7, 18, 19]
