#
# @lc app=leetcode.cn id=42 lang=python3
#
# [42] 接雨水
#


# @lc code=start
class Solution:
    def trap(self, height: List[int]) -> int:
        n = len(height)
        hl_mx, hr_mx = [0 for _ in range(n)], [0 for _ in range(n)]
        for i in range(1, n):
            hl_mx[i] = max(hl_mx[i - 1], height[i - 1])
        for i in range(n - 2, -1, -1):
            hr_mx[i] = max(hr_mx[i + 1], height[i + 1])
        ans = 0
        for i in range(n):
            if height[i] < hl_mx[i] and height[i] < hr_mx[i]:
                ans += min(hl_mx[i], hr_mx[i]) - height[i]
        return ans


# @lc code=end
