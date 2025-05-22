#
# @lc app=leetcode.cn id=1319 lang=python3
#
# [1319] 连通网络的操作次数
#

from typing import *


# @lc code=start
class Solution:
    def union(self, x, y):
        self.parent[self.find(x)] = self.find(y)

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def makeConnected(self, n: int, connections: List[List[int]]) -> int:
        # 找出所有并查集，看并查集多余线缆数是否>=并查集数-1
        self.parent = [i for i in range(n)]
        for x, y in connections:
            self.union(x, y)
        for i in range(n):
            self.parent[i] = self.find(self.parent[i])
        # 并查集数
        c = len(set(self.parent))
        # 各并查集元素数
        cm = {x: 0 for x in set(self.parent)}
        for i in range(n):
            cm[self.parent[i]] += 1
        # 各并查集线缆数
        cn = {x: 0 for x in set(self.parent)}
        for x, y in connections:
            cn[self.parent[x]] += 1
        # 多余线缆数
        c1 = 0
        for x in cm.keys():
            c1 += cn[x] - (cm[x] - 1)
        # print(cn, cm)
        # print(self.parent)
        return -1 if c1 < c - 1 else c - 1


# @lc code=end

print(Solution().makeConnected(4, [[0, 1], [0, 2], [1, 2]]))
