# 原始二维矩阵
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]

# 行数和列数
m, n = len(matrix), len(matrix[0])

# 构建二维前缀和矩阵（多一行一列，方便处理边界）
prefix = [[0] * (n + 1) for _ in range(m + 1)]

# 计算二维前缀和
for i in range(m):
    for j in range(n):
        prefix[i + 1][j + 1] = (
            prefix[i + 1][j] + prefix[i][j + 1] - prefix[i][j] + matrix[i][j]
        )


# 查询子矩阵和：左上角 (r1, c1)，右下角 (r2, c2)，下标从0开始
def range_sum_2d(r1, c1, r2, c2):
    return (
        prefix[r2 + 1][c2 + 1]
        - prefix[r1][c2 + 1]
        - prefix[r2 + 1][c1]
        + prefix[r1][c1]
    )


# 示例：
print(range_sum_2d(0, 0, 1, 1))  # 输出 1+2+4+5 = 12
print(range_sum_2d(1, 1, 2, 2))  # 输出 5+6+8+9 = 28
