#
# @lc app=leetcode.cn id=90 lang=python3
#
# [90] 子集 II
#


# @lc code=start
class Solution:
    def solve(self, nums, state, cur, vis, res):
        if cur == len(nums):
            res.append(state[:])
            return
        if cur > 0 and nums[cur] == nums[cur - 1] and not vis[cur - 1]:
            self.solve(nums, state, cur + 1, vis, res)
        else:
            state.append(nums[cur])
            vis[cur] = True
            self.solve(nums, state, cur + 1, vis, res)
            state.pop()
            vis[cur] = False
            self.solve(nums, state, cur + 1, vis, res)

    def subsetsWithDup(self, nums: List[int]) -> List[List[int]]:
        res = []
        self.solve(sorted(nums), [], 0, [False for _ in range(len(nums))], res)
        return res


# @lc code=end
