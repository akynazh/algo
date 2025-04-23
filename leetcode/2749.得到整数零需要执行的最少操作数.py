#
# @lc app=leetcode.cn id=2749 lang=python3
#
# [2749] 得到整数零需要执行的最少操作数
#


# @lc code=start
class Solution:
    def makeTheIntegerZero(self, num1: int, num2: int) -> int:
        k = 1
        while True:
            x = num1 - k * num2
            if x < k:
                return -1
            if x.bit_count() <= k:
                return k
            k += 1


# @lc code=end
