#
# @lc app=leetcode.cn id=888 lang=python3
#
# [888] 公平的糖果交换
#


# @lc code=start
class Solution:
    def fairCandySwap(self, aliceSizes: List[int], bobSizes: List[int]) -> List[int]:
        sa, sb = sum(aliceSizes), sum(bobSizes)
        mpb = {v: 1 for v in bobSizes}
        x = -(sa - sb) // 2 if sa > sb else (sb - sa) // 2
        for v in aliceSizes:
            if v + x in mpb:
                return [v, v + x]


# @lc code=end
