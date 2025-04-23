#
# @lc app=leetcode.cn id=122 lang=python3
#
# [122] 买卖股票的最佳时机 II
#


# @lc code=start
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        a, b, p, r = 0, 1, 0, 0
        while b < len(prices):
            k = prices[b] - prices[a]
            if k <= p:
                r += p
                p = 0
                a = b
            else:
                p = k
            b += 1
        r += p
        return r


# @lc code=end
