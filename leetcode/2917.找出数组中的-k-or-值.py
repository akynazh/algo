#
# @lc app=leetcode.cn id=2917 lang=python3
#
# [2917] 找出数组中的 K-or 值
#


# @lc code=start
class Solution:
    def findKOr(self, nums: List[int], k: int) -> int:
        bits = [0 for _ in range(32)]
        for num in nums:
            i = 0
            while num != 0:
                b = num % 2
                if b == 1:
                    bits[i] += 1
                num //= 2
                i += 1
        p = 0
        for i in range(32):
            if bits[i] >= k:
                p += 2**i
        return p


# @lc code=end
