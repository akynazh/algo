#
# @lc app=leetcode.cn id=121 lang=python3
#
# [121] 买卖股票的最佳时机
#


# @lc code=start
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        a, b, p = 0, 1, 0
        while b < len(prices):
            if prices[a] < prices[b]:
                p = max(prices[b] - prices[a], p)
            else:
                a = b
            b += 1
        return p


# @lc code=end
