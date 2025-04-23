#
# @lc app=leetcode.cn id=2370 lang=python3
#
# [2370] 最长理想子序列
#


# @lc code=start
class Solution:
    def longestIdealString(self, s: str, k: int) -> int:
        # n = len(s)
        # dp = [1 for _ in range(n)]
        # for i in range(n):
        #     for j in range(0, i):
        #         if abs(ord(s[i]) - ord(s[j])) <= k:
        #             dp[i] = max(dp[i], dp[j] + 1)
        # return max(dp)

        dp = [0 for _ in range(26)]
        for t in s:
            x = ord(t) - ord("a")
            dp[x] = max(dp[max(x - k, 0) : min(x + k + 1, 26)]) + 1
        return max(dp)


# @lc code=end
