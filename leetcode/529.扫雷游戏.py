#
# @lc app=leetcode.cn id=529 lang=python3
#
# [529] 扫雷游戏
#


# @lc code=start
import queue


class Solution:
    def updateBoard(self, board: List[List[str]], click: List[int]) -> List[List[str]]:
        q = queue.Queue()
        a, b = click[0], click[1]
        q.put((a, b))
        dx, dy = [-1, 1, 0, 0, -1, -1, 1, 1], [0, 0, -1, 1, 1, -1, 1, -1]
        n, m = len(board), len(board[0])
        vis = [[False for _ in range(m)] for _ in range(n)]
        vis[a][b] = True
        if board[a][b] == "M":
            board[a][b] = "X"
            return board
        while not q.empty():
            t = q.get()
            x, y = t[0], t[1]
            boom = 0
            lt = []
            for i in range(8):
                xi, yi = x + dx[i], y + dy[i]
                if xi >= 0 and yi >= 0 and xi < n and yi < m:
                    if board[xi][yi] == "M":
                        boom += 1
                    else:
                        lt.append((xi, yi))
            if boom == 0:
                board[x][y] = "B"
                for k in lt:
                    if not vis[k[0]][k[1]]:
                        vis[k[0]][k[1]] = True
                        q.put(k)
            else:
                board[x][y] = str(boom)
        return board


# @lc code=end
