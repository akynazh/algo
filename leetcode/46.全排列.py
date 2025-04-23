#
# @lc app=leetcode.cn id=46 lang=python3
#
# [46] 全排列
#


# @lc code=start
class Solution:
    def solve_vis(self, state):
        if len(state) == len(self.nums):
            self.res.append(list(state))
            return
        for i in range(len(self.nums)):
            if not self.vis[i]:
                self.vis[i] = True
                state.append(self.nums[i])
                self.solve(state)
                state.pop()
                self.vis[i] = False

    def solve(self, cur):
        nums = self.nums
        if cur == len(nums):
            self.res.append(list(nums))
            return
        for i in range(cur, len(nums)):
            nums[i], nums[cur] = nums[cur], nums[i]
            self.solve(cur + 1)
            nums[i], nums[cur] = nums[cur], nums[i]

    def permute(self, nums: List[int]) -> List[List[int]]:
        # self.nums = nums
        # self.vis = [False for _ in range(len(nums))]
        # self.res = []
        # self.solve([])
        # return self.res
        self.nums = nums
        self.res = []
        self.solve(0)
        return self.res


# @lc code=end
