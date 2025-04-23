#
# @lc app=leetcode.cn id=70 lang=python3
#
# [70] 爬楼梯
#


# @lc code=start
class Solution:
    def climbStairs(self, n: int) -> int:
        # dp = [0 for _ in range(n + 1)]
        # dp[0] = dp[1] = 1
        # for i in range(2, n + 1):
        #     dp[i] = dp[i - 2] + dp[i - 1]
        # return dp[n]
        a, b, c = 1, 1, 1
        for i in range(2, n + 1):
            c = a + b
            a = b
            b = c
        return c


# @lc code=end
