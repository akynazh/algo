#
# @lc app=leetcode.cn id=20 lang=python3
#
# [20] 有效的括号
#


# @lc code=start
class Solution:
    def isValid(self, s: str) -> bool:
        lst = []
        for i in range(len(s)):
            if len(lst) != 0 and (
                (lst[-1] == "(" and s[i] == ")")
                or (lst[-1] == "{" and s[i] == "}")
                or (lst[-1] == "[" and s[i] == "]")
            ):
                lst.pop()
            else:
                lst.append(s[i])
        return len(lst) == 0


# @lc code=end
