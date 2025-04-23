#
# @lc app=leetcode.cn id=189 lang=python3
#
# [189] 轮转数组
#


# @lc code=start
from typing import List


class Solution:
    def swap(self, nums, s, e):
        while s < e:
            t = nums[s]
            nums[s] = nums[e]
            nums[e] = t
            s += 1
            e -= 1

    def rotate(self, nums: List[int], k: int) -> None:
        """
        Do not return anything, modify nums in-place instead.
        """
        n = len(nums)
        k = k % n
        # numst = nums[n - k :] + nums[: n - k]
        nums.reverse()
        self.swap(nums, 0, k - 1)
        self.swap(nums, k, n - 1)


# @lc code=end
Solution().rotate([1, 2, 3], 1)
