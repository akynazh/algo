#
# @lc app=leetcode.cn id=207 lang=python3
#
# [207] 课程表
#


# @lc code=start
import queue


class Solution:
    def canFinish(self, numCourses: int, prerequisites: List[List[int]]) -> bool:
        # 入度数
        ins = [0 for _ in range(numCourses)]
        # 各节点指向的节点
        cs = {i: set() for i in range(numCourses)}
        for a, b in prerequisites:
            ins[b] += 1
            cs[a].add(b)
        # 初始化队列：入度为 0 的节点入队
        q = queue.Queue()
        for x in [i for i in range(numCourses) if ins[i] == 0]:
            q.put(x)
        # 处理入度为 0 的节点个数
        c = 0
        while not q.empty():
            x = q.get()
            c += 1
            for y in cs[x]:
                ins[y] -= 1
                if ins[y] == 0:
                    q.put(y)
        # 如果处理入度为 0 的节点个数等于节点总数，则证明没有环，证明可以完成课程
        return c == numCourses


# @lc code=end
