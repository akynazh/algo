#
# @lc app=leetcode.cn id=787 lang=python3
#
# [787] K 站中转内最便宜的航班
#


# @lc code=start
class Solution:
    def findCheapestPrice(
        self, n: int, flights: List[List[int]], src: int, dst: int, k: int
    ) -> int:
        dis = [float("inf") for _ in range(n)]
        dis[src] = 0
        for _ in range(k + 1):
            dis1 = list(dis)
            for u, v, w in flights:
                if dis[v] > dis1[u] + w:
                    dis[v] = dis1[u] + w
        return -1 if dis[dst] == float("inf") else dis[dst]


# @lc code=end
