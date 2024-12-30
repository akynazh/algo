#
# @lc app=leetcode.cn id=498 lang=python3
#
# [498] 对角线遍历
#

"""
模拟时注意真实地去模拟那个过程，关键点在于转向的模拟。

1 2 3 4
3 4 5 6
1 8 9 0

1 2 3 5
m 可以不等于 n

1 2 3
4 5 6
7 8 9



0 0 rv=False s = i = 0
0 1 1 0 rv=True i+1 j-1 s = j = 1
2 0 1 1 0 2 rv=False i-1 j+1 s = i = 2

s=1
s=0
                              s = j = 3
"""


# @lc code=start
from typing import List


class Solution:
    def findDiagonalOrder(self, mat: List[List[int]]) -> List[int]:
        m, n = len(mat), len(mat[0])
        rv = False
        lst = []
        nsi, nsj = 0, 0
        for _ in range(m + n - 1):
            si, sj = nsi, nsj
            if not rv:
                while si >= 0 and sj < n:
                    lst.append(mat[si][sj])
                    si -= 1
                    sj += 1
                si += 1
                sj -= 1
                if sj + 1 >= n:
                    nsi = si + 1
                    nsj = sj
                else:
                    nsi = si
                    nsj = sj + 1
            else:
                while sj >= 0 and si < m:
                    lst.append(mat[si][sj])
                    si += 1
                    sj -= 1
                si -= 1
                sj += 1
                if si + 1 >= m:
                    nsi = si
                    nsj = sj + 1
                else:
                    nsi = si + 1
                    nsj = sj
            rv = not rv
        return lst


# @lc code=end

Solution().findDiagonalOrder([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
