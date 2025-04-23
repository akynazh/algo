#
# @lc app=leetcode.cn id=125 lang=python3
#
# [125] 验证回文串
#


# @lc code=start
class Solution:
    def isPalindrome(self, s: str) -> bool:
        i, j = 0, len(s) - 1
        while i < j:
            while i < len(s) and not str.isalnum(s[i]):
                i += 1
            while j >= 0 and not str.isalnum(s[j]):
                j -= 1
            if (
                (i < len(s) and j >= 0 and str.lower(s[i]) != str.lower(s[j]))
                or (i == len(s) and j >= 0)
                or (i < len(s) and j < 0)
            ):
                return False
            i += 1
            j -= 1
        return True


# @lc code=end
