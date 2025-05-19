MAXN = 100


class Edge:
    def __init(self, v=0, w=0):
        self.v = v
        self.w = w


e = [[Edge() for i in range(MAXN)] for j in range(MAXN)]
INF = 0x3F3F3F3F


def dijkstra(n, s):
    dis = [INF] * (n + 1)
    vis = [0] * (n + 1)

    dis[s] = 0
    for _ in range(n):
        u = 0
        mind = INF
        for j in range(1, n + 1):
            if not vis[j] and dis[j] < mind:
                u = j
                mind = dis[j]
        vis[u] = True
        for ed in e[u]:
            v, w = ed.v, ed.w
            # if dis[v] > dis[u] + w: # 都可以，因为如果 vis[v] == 1，那么 dis[v] 一定 < dis[u] + w
            if not vis[v] and dis[v] > dis[u] + w:
                dis[v] = dis[u] + w


import heapq
from collections import defaultdict


def dijkstra(e, s):
    """
    输入：
    e:邻接表
    s:起点
    返回：
    dis:从s到每个顶点的最短路长度
    """
    dis = defaultdict(lambda: float("inf"))
    dis[s] = 0
    q = [(0, s)]
    vis = set()
    while q:
        _, u = heapq.heappop(q)
        if u in vis:
            continue
        vis.add(u)
        for v, w in e[u]:
            if dis[v] > dis[u] + w:
                dis[v] = dis[u] + w
                heapq.heappush(q, (dis[v], v))
    return dis
