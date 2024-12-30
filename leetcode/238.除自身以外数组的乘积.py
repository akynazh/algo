from typing import List

#
# @lc app=leetcode.cn id=238 lang=python3
#
# [238] 除自身以外数组的乘积
#


# @lc code=start


class Solution:
    def productExceptSelf(self, nums: List[int]) -> List[int]:
        sum_l, sum_r, l, i, ans = [1] * len(nums), [1] * len(nums), len(nums), 1, []
        while i < l:
            sum_l[i] = sum_l[i - 1] * nums[i - 1]
            i += 1
        i = l - 2
        while i >= 0:
            sum_r[i] = sum_r[i + 1] * nums[i + 1]
            i -= 1
        i = 0
        while i < l:
            ans.append(sum_l[i] * sum_r[i])
            i += 1
        return ans


# @lc code=end
