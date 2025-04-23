#
# @lc app=leetcode.cn id=219 lang=python3
#
# [219] 存在重复元素 II
#


# @lc code=start
class Solution:
    def containsNearbyDuplicate(self, nums: List[int], k: int) -> bool:
        s = set()
        for i, v in enumerate(nums):
            if i > k:
                s.remove(nums[i - k - 1])
            if v in s:
                return True
            s.add(v)
        return False


# @lc code=end
