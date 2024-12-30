"""
迷宫可达算法


"""


#
# Note: 类名、方法名、参数名已经指定，请勿修改
#
#
#
# @param generated_map int整型 二维数组
# @return int整型
#
class Solution:
    def apply(self, generated_map):
        n = len(generated_map)
        vis = [[0] * n for _ in range(n)]
        vis[0][0] = 1
        ans = 1
        q = []
        q.append((0, 0))
        while len(q) != 0:
            xy = q.pop(0)
            x, y = xy[0], xy[1]
            a, b = [0, 1, -1, 0], [1, 0, 0, -1]
            for i in range(4):
                x1, y1 = x + a[i], y + b[i]
                if (
                    x1 >= 0
                    and x1 < n
                    and y1 >= 0
                    and y1 < n
                    and vis[x1][y1] != 1
                    and generated_map[x1][y1] != 1
                ):
                    vis[x1][y1] = 1
                    q.append((x1, y1))
                    ans += 1
        return n * n - ans


# Solution().apply([[0, 1, 1, 0], [1, 0, 0, 0], [0, 1, 0, 1], [0, 1, 1, 0]])
