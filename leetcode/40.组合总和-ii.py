#
# @lc app=leetcode.cn id=40 lang=python3
#
# [40] 组合总和 II
#


# @lc code=start
class Solution:

    def solve(self, state, target, start):
        if target == 0:
            self.res.append(list(state))
            return
        for i in range(start, len(self.candidates)):
            if self.candidates[i] > target:
                break

            # 和组合总和-i不同，这里数组有重复元素，所以需要额外处理分支，避免出现重复组合
            if i > start and self.candidates[i] == self.candidates[i - 1]:
                continue

            state.append(self.candidates[i])

            # i + 1 而不是 i: 每个数只能用一次
            self.solve(state, target - self.candidates[i], i + 1)
            state.pop()

    def combinationSum2(self, candidates: List[int], target: int) -> List[List[int]]:
        self.candidates = sorted(candidates)
        self.res = []
        self.solve([], target, 0)
        return self.res


# @lc code=end
