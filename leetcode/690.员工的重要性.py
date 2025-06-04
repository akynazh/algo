#
# @lc app=leetcode.cn id=690 lang=python3
#
# [690] 员工的重要性
#

# @lc code=start
"""
# Definition for Employee.
class Employee:
    def __init__(self, id: int, importance: int, subordinates: List[int]):
        self.id = id
        self.importance = importance
        self.subordinates = subordinates
"""


class Solution:
    def getImportance(self, employees: List["Employee"], id: int) -> int:
        values = [0 for _ in range(2001)]
        emps = {}
        for emp in employees:
            x = emp.id
            values[x] = emp.importance
            emps[x] = emp.subordinates
        v = 0
        import queue

        q = queue.Queue()
        q.put(id)
        vis = [False for _ in range(2001)]
        while not q.empty():
            x = q.get()
            if vis[x]:
                continue
            vis[x] = True
            v += values[x]
            for i in emps[x]:
                q.put(i)
        return v


# @lc code=end
