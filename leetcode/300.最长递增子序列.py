#
# @lc app=leetcode.cn id=300 lang=python3
#
# [300] 最长递增子序列
#


# @lc code=start
from typing import List


class Solution:
    # def lengthOfLIS(self, nums: List[int]) -> int:
    #     mx = 1
    #     n = len(nums)
    #     for i in range(n - 1):
    #         cur = 1
    #         cur_v = nums[i]
    #         for j in range(i + 1, n):
    #             if nums[j] > cur_v:
    #                 cur += 1
    #                 cur_v = nums[j]
    #         mx = max(mx, cur)
    #     return mx

    def lengthOfLIS(self, nums: List[int]) -> int:
        n = len(nums)
        dp = [1 for _ in range(n)]
        for i in range(n):
            v = nums[i]
            for j in range(0, i):
                if nums[j] < v:
                    dp[i] = max(dp[i], dp[j] + 1)
        return max(dp)


# @lc code=end
