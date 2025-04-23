#
# @lc app=leetcode.cn id=684 lang=python3
#
# [684] 冗余连接
#


# @lc code=start
class Solution:
    def findRedundantConnection(self, edges: List[List[int]]) -> List[int]:
        parent = [i for i in range(len(edges) + 1)]

        def find(a):
            return a if parent[a] == a else find(parent[a])

        def put(a, b):
            parent[find(a)] = find(b)

        for a, b in edges:
            if find(a) == find(b):
                return [a, b]
            put(a, b)
        return []


# @lc code=end
