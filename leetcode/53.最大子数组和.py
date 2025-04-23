#
# @lc app=leetcode.cn id=53 lang=python3
#
# [53] 最大子数组和
#


# @lc code=start
class Solution:
    def maxSubArray(self, nums: List[int]) -> int:
        n = len(nums)
        # dp = [0 for _ in range(n)]
        # dp[0] = nums[0]
        # for i in range(1, n):
        #     dp[i] = max(dp[i - 1] + nums[i], nums[i])
        # return max(dp)
        x = nums[0]
        res = x
        for i in range(1, n):
            x = max(x + nums[i], nums[i])
            res = max(res, x)
        return res


# @lc code=end
