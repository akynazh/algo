#
# @lc app=leetcode.cn id=14 lang=python3
#
# [14] 最长公共前缀
#

from typing import List


# @lc code=start
class Solution:
    def longestCommonPrefix(self, strs: List[str]) -> str:
        if len(strs) == 1:
            return strs[0]
        ml = 1000
        st = ""
        for s in strs:
            if len(s) < ml:
                ml = len(s)
                st = s
        ans = ""
        for i in range(1, ml + 1):
            t = st[0:i]
            for s in strs:
                if s[0:i] != t:
                    return ans
            ans = t
        return ans


print(Solution().longestCommonPrefix(["ab", "a"]))

# @lc code=end
