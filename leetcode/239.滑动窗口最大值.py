#
# @lc app=leetcode.cn id=239 lang=python3
#
# [239] 滑动窗口最大值
#
import heapq


# @lc code=start
class Solution:
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        pq = [(-nums[i], i) for i in range(k)]
        heapq.heapify(pq)
        ans = [-pq[0][0]]
        for i in range(k, len(nums)):
            heapq.heappush(pq, (-nums[i], i))
            while len(pq) != 0 and pq[0][1] <= i - k:
                heapq.heappop(pq)
            ans.append(-pq[0][0])
        return ans


# @lc code=end
