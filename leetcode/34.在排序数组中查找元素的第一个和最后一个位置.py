#
# @lc app=leetcode.cn id=34 lang=python3
#
# [34] 在排序数组中查找元素的第一个和最后一个位置
#
from typing import List

# @lc code=start


class Solution:
    def searchRange(self, nums: List[int], target: int) -> List[int]:
        res_l, res_r = -1, -1
        l, r = 0, len(nums) - 1
        while l <= r:
            m = int(l + (r - l) / 2)
            if nums[m] > target:
                r = m - 1
            elif nums[m] < target:
                l = m + 1
            else:
                res_l = m
                r = m - 1
        l, r = 0, len(nums) - 1
        while l <= r:
            m = int(l + (r - l) / 2)
            if nums[m] > target:
                r = m - 1
            elif nums[m] < target:
                l = m + 1
            else:
                res_r = m
                l = m + 1
        return [res_l, res_r]


# @lc code=end
