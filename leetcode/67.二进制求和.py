#
# @lc app=leetcode.cn id=67 lang=python3
#
# [67] 二进制求和
#


# @lc code=start
class Solution:
    def addBinary(self, a: str, b: str) -> str:
        if len(a) >= len(b):
            b, a = a, b
        a, b = a[-1::-1], b[-1::-1]
        x = ""
        p = 0
        for i in range(len(a)):
            a1, b1 = int(a[i]), int(b[i])
            x += str((a1 + b1 + p) % 2)
            p = (a1 + b1 + p) // 2
        for i in range(len(a), len(b)):
            b1 = int(b[i])
            x += str((b1 + p) % 2)
            p = (b1 + p) // 2
        if p == 1:
            x += "1"
        return x[-1::-1]


# @lc code=end
