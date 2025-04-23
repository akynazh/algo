#
# @lc app=leetcode.cn id=28 lang=python3
#
# [28] 找出字符串中第一个匹配项的下标
#


# @lc code=start
class Solution:
    def strStr(self, haystack: str, needle: str) -> int:
        return haystack.find(needle)


# @lc code=end
