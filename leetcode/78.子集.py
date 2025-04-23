#
# @lc app=leetcode.cn id=78 lang=python3
#
# [78] 子集
#


# @lc code=start
class Solution:
    def solve(self, cur, state, nums):
        if cur == len(nums):
            self.ans.append(state[:])
            return
        state.append(nums[cur])
        self.solve(cur + 1, state, nums)
        state.pop()
        self.solve(cur + 1, state, nums)

    def subsets(self, nums: List[int]) -> List[List[int]]:
        # self.ans = []
        # self.solve(0, [], nums)
        # return self.ans

        n = len(nums)
        t = 1 << n
        ans = []
        for i in range(t):
            state = []
            for j in range(n):
                if i & (1 << j):
                    state.append(nums[j])
            ans.append(state)
        return ans


# @lc code=end
