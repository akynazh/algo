#
# @lc app=leetcode.cn id=283 lang=python3
#
# [283] 移动零
#

# @lc code=start
from typing import List


class Solution:
    def moveZeroes(self, nums: List[int]) -> None:
        """
        Do not return anything, modify nums in-place instead.
        """
        # s = len(nums)
        # for i in range(s - 1, -1, -1):
        #     if nums[i] == 0:
        #         for j in range(i, s - 1):
        #             if nums[j + 1] == 0:
        #                 break
        #             nums[j], nums[j + 1] = nums[j + 1], nums[j]
        i = j = 0
        while j < len(nums):
            if nums[j] != 0:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1
            j += 1


# @lc code=end
