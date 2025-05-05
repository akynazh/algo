#
# @lc app=leetcode.cn id=1071 lang=python3
#
# [1071] 字符串的最大公因子
#


# @lc code=start
class Solution:
    def gcd(self, a, b):
        a, b = max(a, b), min(a, b)
        x = a % b
        while x != 0:
            a, b = b, x
            x = a % b
        return b

    def gcdOfStrings(self, str1: str, str2: str) -> str:
        import math

        a, b = len(str1), len(str2)
        # k = math.gcd(a, b)
        k = self.gcd(a, b)

        for i in range(k, 0, -1):
            x = str1[:i]
            if x * (a // i) == str1 and x * (b // i) == str2:
                return x
        return ""


# @lc code=end
