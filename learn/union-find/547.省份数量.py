#
# @lc app=leetcode.cn id=547 lang=python3
#
# [547] 省份数量
#

from typing import *


# @lc code=start
class Solution:
    def find(self, i):
        if self.parent[i] != i:
            self.parent[i] = self.find(self.parent[i])
        return self.parent[i]

    def union(self, i, j):
        # self.parent[self.find(i)] = self.find(j)

        pi, pj = self.find(i), self.find(j)
        if pi == pj:
            return
        if self.rank[pi] < self.rank[pj]:
            self.parent[pi] = pj
        elif self.rank[pi] > self.rank[pj]:
            self.parent[pj] = pi
        else:
            self.parent[pj] = pi
            self.rank[pi] += 1

    def findCircleNum(self, isConnected: List[List[int]]) -> int:
        n = len(isConnected)
        self.parent = [i for i in range(n)]
        self.rank = [1] * n
        for i in range(n):
            for j in range(n):
                if i != j and isConnected[i][j] == 1:
                    self.union(i, j)
        for i in range(n):
            self.parent[i] = self.find(self.parent[i])
        return len(set(self.parent))


# @lc code=end

Solution().findCircleNum([[1, 0, 0, 1], [0, 1, 1, 0], [0, 1, 1, 1], [1, 0, 1, 1]])
