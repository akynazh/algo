#
# @lc app=leetcode.cn id=287 lang=python3
#
# [287] 寻找重复数
#


# @lc code=start
class Solution:
    def findDuplicate(self, nums: List[int]) -> int:
        # 0 -> 1 -> 4 -> 6 -> 3 -> 6
        fast, slow = nums[nums[0]], nums[0]
        while fast != slow:
            slow = nums[slow]
            fast = nums[nums[fast]]
        slow = 0
        while fast != slow:
            slow = nums[slow]
            fast = nums[fast]
        return slow


# @lc code=end
