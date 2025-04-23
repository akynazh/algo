#
# @lc app=leetcode.cn id=1844 lang=python3
#
# [1844] 将所有数字用字符替换
#


# @lc code=start
class Solution:
    def replaceDigits(self, s: str) -> str:
        lst = [t for t in s]
        for i in range(len(s)):
            if i % 2 == 1:
                lst[i] = chr(ord(s[i - 1]) + int(s[i]))
        return "".join(lst)


# @lc code=end
