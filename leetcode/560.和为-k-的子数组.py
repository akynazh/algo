#
# @lc app=leetcode.cn id=560 lang=python3
#
# [560] 和为 K 的子数组
#


# @lc code=start
class Solution:
    def subarraySum(self, nums: List[int], k: int) -> int:
        c, pre, mp = 0, 0, {0: 1}
        for i in range(len(nums)):
            pre += nums[i]
            if pre - k in mp:
                c += mp[pre - k]
            mp[pre] = 1 if pre not in mp else mp[pre] + 1
        return c


# @lc code=end
