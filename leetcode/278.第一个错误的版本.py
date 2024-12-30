#
# @lc app=leetcode.cn id=278 lang=python3
#
# [278] 第一个错误的版本
#
def isBadVersion(version: int) -> bool:
    pass


# @lc code=start
# The isBadVersion API is already defined for you.
# def isBadVersion(version: int) -> bool:


class Solution:
    def firstBadVersion(self, n: int) -> int:
        l, r = 1, n
        while l < r:
            m = int(l + (r - l) / 2)
            if isBadVersion(m):
                r = m
            else:
                l = m + 1
        return l


# @lc code=end
