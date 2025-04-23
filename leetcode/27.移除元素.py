#
# @lc app=leetcode.cn id=27 lang=python3
#
# [27] 移除元素
#


# @lc code=start
class Solution:
    def removeElement(self, nums: List[int], val: int) -> int:
        i, j, n = 0, 0, len(nums)
        while j < n:
            if nums[j] == val:
                j += 1
            else:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1
                j += 1
        return i


# @lc code=end
