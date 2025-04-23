#
# @lc app=leetcode.cn id=66 lang=python3
#
# [66] 加一
#

# @lc code=start
from typing import *


class Solution:
    def plusOne(self, digits: List[int]) -> List[int]:
        prev = 1
        i = len(digits) - 1
        while i >= 0:
            v = digits[i]
            t = int(v + prev)
            digits[i] = t % 10
            prev = int(t / 10)
            i -= 1
            if prev == 0:
                break
        if prev != 0:
            digits = [1] + digits
        return digits


# @lc code=end
