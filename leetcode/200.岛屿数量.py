#
# @lc app=leetcode.cn id=200 lang=python3
#
# [200] 岛屿数量
#


# @lc code=start
import queue


class Solution:
    def getIsland(self, m, n, i, j, vis, grid):
        q = queue.Queue()
        q.put((i, j))
        dx, dy = [-1, 0, 1, 0], [0, -1, 0, 1]
        while not q.empty():
            t = q.get()
            x, y = t[0], t[1]
            for i in range(4):
                xt, yt = x + dx[i], y + dy[i]
                if (
                    xt >= 0
                    and xt < m
                    and yt >= 0
                    and yt < n
                    and (not vis[xt][yt])
                    and grid[xt][yt] == "1"
                ):
                    q.put((xt, yt))
                    vis[xt][yt] = True

    def numIslands(self, grid: List[List[str]]) -> int:
        m, n = len(grid), len(grid[0])
        c = 0
        vis = [[False for _ in range(n)] for _ in range(m)]
        for i in range(m):
            for j in range(n):
                if grid[i][j] == "1" and not vis[i][j]:
                    c += 1
                    vis[i][j] = True
                    self.getIsland(m, n, i, j, vis, grid)
        return c


# @lc code=end
