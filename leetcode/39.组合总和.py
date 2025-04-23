#
# @lc app=leetcode.cn id=39 lang=python3
#
# [39] 组合总和
#

from typing import *


# @lc code=start
class Solution:
    def solve(self, state, target, start):
        if target == 0:
            # 这里必须通过 list() 新建一个 list，否则后续回溯会影响到引用的 list
            self.res.append(list(state))
            return
        for i in range(start, len(self.candidates)):
            if target - self.candidates[i] < 0:
                break
            state.append(self.candidates[i])
            self.solve(state, target - self.candidates[i], i)
            state.pop()

    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        self.res = []
        self.candidates = sorted(candidates)
        self.solve([], target, 0)
        return self.res


Solution().combinationSum([2, 3, 6, 7], 7)
# @lc code=end
