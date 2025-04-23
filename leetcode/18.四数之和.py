#
# @lc app=leetcode.cn id=18 lang=python3
#
# [18] 四数之和
#


# @lc code=start
class Solution:
    def fourSum(self, nums: List[int], target: int) -> List[List[int]]:
        n = len(nums)
        if (not nums) or n < 4:
            return []
        res = []
        nums = sorted(nums)
        mp = {}
        for i in range(n):
            for j in range(i + 1, n):
                L, R = j + 1, n - 1
                while L < R:
                    a, b, c, d = nums[i], nums[j], nums[L], nums[R]
                    if a + b + c + d == target:
                        x = [a, b, c, d]
                        x1 = "-".join([str(v) for v in x])
                        if x1 not in mp:
                            res.append(x)
                            mp[x1] = 1
                        while L < R and nums[L + 1] == nums[L]:
                            L += 1
                        while L < R and nums[R - 1] == nums[R]:
                            R -= 1
                        L += 1
                        R -= 1

                    elif a + b + c + d < target:
                        L += 1
                    else:
                        R -= 1
        return res


# @lc code=end
