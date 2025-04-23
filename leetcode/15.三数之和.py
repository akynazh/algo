#
# @lc app=leetcode.cn id=15 lang=python3
#
# [15] 三数之和
#


# @lc code=start
class Solution:
    def threeSum(self, nums: List[int]) -> List[List[int]]:
        # n = len(nums)
        # ans = []
        # mp = {}
        # for i in range(n):
        #     for j in range(i + 1, n):
        #         for k in range(j + 1, n):
        #             if nums[i] + nums[j] + nums[k] == 0:
        #                 t = sorted([nums[i], nums[j], nums[k]])
        #                 t1 = "".join([str(v) for v in t])
        #                 if t1 not in mp:
        #                     ans.append(t)
        #                     mp[t1] = 1
        # return ans
        n = len(nums)
        res = []
        if (not nums) or n < 3:
            return []
        nums = sorted(nums)
        for i in range(n):
            if nums[i] > 0:
                return res
            if i > 0 and nums[i] == nums[i - 1]:
                continue
            L = i + 1
            R = n - 1
            while L < R:
                a, b, c = nums[i], nums[L], nums[R]
                if a + b + c == 0:
                    res.append([a, b, c])
                    while L < R and nums[L] == nums[L + 1]:
                        L += 1
                    while L < R and nums[R] == nums[R - 1]:
                        R -= 1
                    L += 1
                    R -= 1
                elif a + b + c > 0:
                    R -= 1
                else:
                    L += 1
        return res


# @lc code=end
