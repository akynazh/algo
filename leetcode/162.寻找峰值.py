#
# @lc app=leetcode.cn id=162 lang=python3
#
# [162] 寻找峰值
#


# @lc code=start
from typing import List


class Solution:
    def findPeakElement(self, nums: List[int]) -> int:
        def get(i):
            return float("-inf") if i == -1 or i == len(nums) else nums[i]

        l, r, ans = 0, len(nums) - 1, -1
        while l <= r:
            m = l + (r - l) // 2
            if get(m - 1) < get(m) > get(m + 1):
                return m
            if get(m - 1) < get(m):
                l = m + 1
            else:
                r = m - 1
        return ans


# @lc code=end

Solution().findPeakElement([1])
