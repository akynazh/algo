#
# @lc app=leetcode.cn id=26 lang=python3
#
# [26] 删除有序数组中的重复项
#


# @lc code=start
class Solution:
    def removeDuplicates(self, nums: List[int]) -> int:
        n = len(nums)
        i, j = 0, 1
        while j < n:
            if nums[j] == nums[j - 1]:
                j += 1
            else:
                i += 1
                nums[i] = nums[j]
                j += 1
        return i + 1


# @lc code=end
