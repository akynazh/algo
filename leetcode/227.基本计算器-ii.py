#
# @lc app=leetcode.cn id=227 lang=python3
#
# [227] 基本计算器 II
#


# @lc code=start
class Solution:
    def calculate(self, s: str) -> int:
        s = s.replace(" ", "")
        n = len(s)
        stack = []
        preSign = "+"
        num = 0
        for i in range(n):
            if s[i].isdigit():
                num = num * 10 + int(s[i])
            if i == n - 1 or s[i] in "+-*/":
                if preSign == "+":
                    stack.append(num)
                elif preSign == "-":
                    stack.append(-num)
                elif preSign == "*":
                    stack.append(stack.pop() * num)
                else:
                    stack.append(int(stack.pop() / num))
                preSign = s[i]
                num = 0
        return sum(stack)


# @lc code=end
Solution().calculate("3+2*2")
