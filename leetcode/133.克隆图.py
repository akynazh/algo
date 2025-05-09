#
# @lc app=leetcode.cn id=133 lang=python3
#
# [133] 克隆图
#
# Definition for a Node.
class Node:
    def __init__(self, val=0, neighbors=None):
        self.val = val
        self.neighbors = neighbors if neighbors is not None else []


# @lc code=start
"""
# Definition for a Node.
class Node:
    def __init__(self, val = 0, neighbors = None):
        self.val = val
        self.neighbors = neighbors if neighbors is not None else []
"""

from typing import Optional
import queue


class Solution:
    def cloneGraph(self, node: Optional["Node"]) -> Optional["Node"]:
        if not node:
            return
        vis = [False for _ in range(101)]
        nodes = [Node(i) for i in range(101)]

        q = queue.Queue()
        q.put(node)
        while not q.empty():
            c = q.get()
            v = c.val
            if not vis[v]:
                vis[v] = True
                for n in c.neighbors:
                    nodes[v].neighbors.append(nodes[n.val])
                    q.put(n)
        return nodes[node.val]


# @lc code=end
