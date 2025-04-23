#
# @lc app=leetcode.cn id=268 lang=python3
#
# [268] 丢失的数字
#


# @lc code=start
class Solution:
    def missingNumber(self, nums: List[int]) -> int:
        return (1 + len(nums)) * len(nums) // 2 - sum(nums)


# @lc code=end
