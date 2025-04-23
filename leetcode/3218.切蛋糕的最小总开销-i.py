#
# @lc app=leetcode.cn id=3218 lang=python3
#
# [3218] 切蛋糕的最小总开销 I
#


# @lc code=start
class Solution:
    def minimumCost(
        self, m: int, n: int, horizontalCut: List[int], verticalCut: List[int]
    ) -> int:
        horizontalCut = sorted(horizontalCut, reverse=True)
        verticalCut = sorted(verticalCut, reverse=True)
        rc, cc = 1, 1
        i, j = 0, 0
        ans = 0
        while i < m - 1 or j < n - 1:
            if j == n - 1 or (i < m - 1 and horizontalCut[i] >= verticalCut[j]):
                ans += cc * horizontalCut[i]
                i += 1
                rc += 1
            else:
                ans += rc * verticalCut[j]
                j += 1
                cc += 1
        return ans


# @lc code=end
