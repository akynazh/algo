#
# @lc app=leetcode.cn id=743 lang=python3
#
# [743] 网络延迟时间
#
from typing import *

# @lc code=start
import heapq


class Solution:
    def networkDelayTime(self, times: List[List[int]], n: int, k: int) -> int:
        g = [[] for _ in range(n + 1)]
        for t in times:
            g[t[0]].append((t[1], t[2]))
        vis = [False for _ in range(n + 1)]
        dis = [float("inf") for _ in range(n + 1)]
        dis[k] = 0
        dis[0] = 0

        for _ in range(n):
            x = float("inf")
            s = -1
            for i in range(1, n + 1):
                if not vis[i] and dis[i] < x:
                    s, x = i, dis[i]
            vis[s] = True
            for e in g[s]:
                v, w = e[0], e[1]
                if not vis[v] and dis[v] > dis[s] + w:
                    dis[v] = dis[s] + w

        # q = [(0, k)]
        # while q:
        #     _, s = heapq.heappop(q)
        #     if vis[s]:
        #         continue
        #     vis[s] = True
        #     for e in g[s]:
        #         v, w = e[0], e[1]
        #         if not vis[v] and dis[v] > dis[s] + w:
        #             dis[v] = dis[s] + w
        #             heapq.heappush(q, (dis[v], v))

        return max(dis) if float("inf") not in dis else -1


# @lc code=end

print(Solution().networkDelayTime([[2, 1, 1], [2, 3, 1], [3, 4, 1]], 4, 2))
