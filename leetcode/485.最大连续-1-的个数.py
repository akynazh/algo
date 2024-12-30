#
# @lc app=leetcode.cn id=485 lang=python3
#
# [485] 最大连续 1 的个数
#


# @lc code=start
class Solution:
    def findMaxConsecutiveOnes(self, nums: List[int]) -> int:
        c = 0
        mc = 0
        for v in nums:
            if v == 1:
                c += 1
                if c > mc:
                    mc = c
            else:
                c = 0
        return mc


# @lc code=end
