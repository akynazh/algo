#
# @lc app=leetcode.cn id=415 lang=python3
#
# [415] 字符串相加
#


# @lc code=start
class Solution:
    def addStrings(self, num1: str, num2: str) -> str:
        k1, k2 = len(num1), len(num2)
        if k1 >= k2:
            bn, sn, i, j = num1, num2, k1 - 1, k2 - 1
        else:
            bn, sn, i, j = num2, num1, k2 - 1, k1 - 1
        k = 0
        res = ""
        while j >= 0:
            n1, n2 = bn[i], sn[j]
            n = int(n1) + int(n2) + k
            res += str(n % 10)
            k = int(n / 10)
            j -= 1
            i -= 1
        while i >= 0:
            n = int(bn[i]) + k
            res += str(n % 10)
            k = int(n / 10)
            i -= 1
        if k != 0:
            res += str(k)
        return res[::-1]


# @lc code=end
