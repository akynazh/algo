#
# @lc app=leetcode.cn id=1749 lang=python3
#
# [1749] 任意子数组和的绝对值的最大值
#


# @lc code=start
class Solution:
    def maxAbsoluteSum(self, nums: List[int]) -> int:
        n = len(nums)
        # dp = [0 for _ in range(n)]
        # dpm = [0 for _ in range(n)]
        # dp[0] = max(0, nums[0])
        # dpm[0] = min(0, nums[0])
        # res = abs(nums[0])
        # for i in range(1, n):
        #     dp[i] = max(dp[i - 1] + nums[i], nums[i], 0)
        #     dpm[i] = min(dpm[i - 1] + nums[i], nums[i], 0)
        #     res = max(res, abs(dp[i]), abs(dpm[i]))
        # return res
        x, y, res = max(0, nums[0]), min(0, nums[0]), abs(nums[0])
        for i in range(1, n):
            x = max(x + nums[i], nums[i], 0)
            y = min(y + nums[i], nums[i], 0)
            res = max(res, abs(x), abs(y))
        return res


# @lc code=end
