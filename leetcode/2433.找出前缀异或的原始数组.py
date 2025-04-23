#
# @lc app=leetcode.cn id=2433 lang=python3
#
# [2433] 找出前缀异或的原始数组
#


# @lc code=start
class Solution:
    def findArray(self, pref: List[int]) -> List[int]:
        arr = [pref[0]]
        for i in range(1, len(pref)):
            arr.append(pref[i - 1] ^ pref[i])
        return arr


# @lc code=end
