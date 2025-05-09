#
# @lc app=leetcode.cn id=207 lang=python3
#
# [207] 课程表
#


# @lc code=start
import queue


class Solution:
    def canFinish(self, numCourses: int, prerequisites: List[List[int]]) -> bool:
        ins = [0 for _ in range(numCourses)]
        cs = {i: set() for i in range(numCourses)}
        for a, b in prerequisites:
            ins[b] += 1
            cs[a].add(b)
        q = queue.Queue()
        for x in [i for i in range(numCourses) if ins[i] == 0]:
            q.put(x)
        c = 0
        while not q.empty():
            x = q.get()
            c += 1
            for y in cs[x]:
                ins[y] -= 1
                if ins[y] == 0:
                    q.put(y)
        return c == numCourses


# @lc code=end
