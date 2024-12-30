#
# @lc app=leetcode.cn id=347 lang=python3
#
# [347] 前 K 个高频元素
#
import heapq


# @lc code=start
class Solution:
    def topKFrequent(self, nums: List[int], k: int) -> List[int]:
        dt = {}
        for v in nums:
            if v in dt.keys():
                dt[v] += 1
            else:
                dt[v] = 1
        pq = [(v, k) for k, v in dt.items()]
        return [x[1] for x in heapq.nlargest(k, pq)]


# @lc code=end
