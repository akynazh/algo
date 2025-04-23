#
# @lc app=leetcode.cn id=242 lang=python3
#
# [242] 有效的字母异位词
#


# @lc code=start
class Solution:
    def put(self, k, d):
        if k not in d:
            d[k] = 1
        else:
            d[k] += 1

    def isAnagram(self, s: str, t: str) -> bool:
        if len(s) != len(t):
            return False
        d1 = {}
        d2 = {}
        l = len(s)
        for i in range(l):
            self.put(s[i], d1)
            self.put(t[i], d2)
        for i in range(l):
            k = s[i]
            if k not in d2 or d1[k] != d2[k]:
                return False
        return True


# @lc code=end
