#
# @lc app=leetcode.cn id=51 lang=python3
#
# [51] N 皇后
#


# @lc code=start
class Solution:
    def solve(self, n, row, stat, cols, dias1, dias2, res):
        if row == n:
            res.append(stat[:])
            return
        for i in range(n):
            if i not in cols and i + row not in dias1 and i - row not in dias2:
                stat.append(i)
                cols.append(i)
                dias1.append(i + row)
                dias2.append(i - row)
                self.solve(n, row + 1, stat, cols, dias1, dias2, res)
                stat.pop()
                cols.pop()
                dias1.pop()
                dias2.pop()

    def solveNQueens(self, n: int) -> List[List[str]]:
        res = []
        self.solve(n, 0, [], [], [], [], res)
        a, b = ".", "Q"
        ress = []
        # print(res)
        for lst in res:
            t = []
            for i in lst:
                x = ""
                for k in range(n):
                    x += b if k == i else a
                t.append(x)
            ress.append(t)
        return ress


# @lc code=end
