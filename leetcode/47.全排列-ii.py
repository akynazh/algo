#
# @lc app=leetcode.cn id=47 lang=python3
#
# [47] 全排列 II
#


# @lc code=start
class Solution:
    def solve(self, state: list, vis: list, nums: list, res: list):
        if len(state) == len(nums):
            res.append(list(state))
            return
        for i in range(len(nums)):
            # 如果相邻两数相等，那么必须是先取前数再取后数
            if vis[i] or (i > 0 and nums[i] == nums[i - 1] and not vis[i - 1]):
                continue
            state.append(nums[i])
            vis[i] = True
            self.solve(state, vis, nums, res)
            vis[i] = False
            state.pop()

    def permuteUnique(self, nums: List[int]) -> List[List[int]]:
        res = []
        self.solve([], [False for _ in range(len(nums))], sorted(nums), res)
        return res


# @lc code=end
