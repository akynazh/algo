#
# @lc app=leetcode.cn id=724 lang=python3
#
# [724] 寻找数组的中心下标
#


# @lc code=start
class Solution:
    def pivotIndex(self, nums: List[int]) -> int:
        lg = len(nums)
        sum_l, sum_r = [0 for _ in range(lg)], [0 for _ in range(lg)]
        for i in range(1, lg):
            sum_l[i] = sum_l[i - 1] + nums[i - 1]
        for i in range(lg - 2, -1, -1):
            sum_r[i] = sum_r[i + 1] + nums[i + 1]
        for i in range(0, lg):
            if sum_l[i] == sum_r[i]:
                return i
        return -1


# @lc code=end
