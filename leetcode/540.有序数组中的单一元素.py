#
# @lc app=leetcode.cn id=540 lang=python3
#
# [540] 有序数组中的单一元素
#


# @lc code=start
class Solution:
    def singleNonDuplicate(self, nums: List[int]) -> int:
        l = 0
        r = len(nums) - 1
        while l < r:
            m = l + (r - l) // 2
            if m % 2 != 0:
                m = m - 1
            if nums[m] == nums[m + 1]:
                l = m + 2
            else:
                r = m
        return nums[l]


# @lc code=end
