#
# @lc app=leetcode.cn id=69 lang=python3
#
# [69] x 的平方根
#


# @lc code=start
class Solution:
    def mySqrt(self, x: int) -> int:
        # if x <= 1:
        #     return x
        # for v in range(1, x + 1):
        #     if v * v == x:
        #         return v
        #     if v * v > x:
        #         return v - 1
        l, r, ans = 0, x, x
        while l <= r:
            mid = l + (r - l) // 2
            if mid * mid <= x:
                ans = mid
                l = mid + 1
            else:
                r = mid - 1
        return ans


# @lc code=end
