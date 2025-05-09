#
# @lc app=leetcode.cn id=1979 lang=python3
#
# [1979] 找出数组的最大公约数
#


# @lc code=start
class Solution:
    def findGCD(self, nums: List[int]) -> int:
        import math

        return math.gcd(max(nums), min(nums))


# @lc code=end
