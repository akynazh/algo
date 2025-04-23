#
# @lc app=leetcode.cn id=16 lang=python3
#
# [16] 最接近的三数之和
#


# @lc code=start
class Solution:
    def threeSumClosest(self, nums: List[int], target: int) -> int:
        n = len(nums)
        dis = 1000000000
        ans = 0
        nums = sorted(nums)
        for i in range(n):
            # if i > 0 and nums[i] == nums[i - 1]:
            #     continue
            L = i + 1
            R = n - 1
            while L < R:
                a, b, c = nums[i], nums[L], nums[R]
                v = a + b + c
                dv = v - target
                d = abs(v - target)
                if d < dis:
                    ans = v
                    dis = d
                if dv < 0:
                    L += 1
                elif dv > 0:
                    R -= 1
                else:
                    return v

        return ans


# @lc code=end
