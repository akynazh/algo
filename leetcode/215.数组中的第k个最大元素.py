#
# @lc app=leetcode.cn id=215 lang=python3
#
# [215] 数组中的第K个最大元素
#

# @lc code=start
from typing import List


class Solution:
    def down(self, n, j, nums):
        while j < n:
            if (
                2 * j + 2 < n
                and nums[2 * j + 1] <= nums[2 * j + 2]
                and nums[j] < nums[2 * j + 2]
            ):
                nums[j], nums[2 * j + 2] = nums[2 * j + 2], nums[j]
                j = 2 * j + 2
            elif 2 * j + 1 < n and nums[2 * j + 1] > nums[j]:
                nums[j], nums[2 * j + 1] = nums[2 * j + 1], nums[j]
                j = 2 * j + 1
            else:
                break

    # 堆排序做法
    # def findKthLargest(self, nums: List[int], k: int) -> int:
    #     # 构造大顶堆 | 完全二叉树最后一个非叶子节点序号 n / 2 - 1 (序号从 0 开始)
    #     n = len(nums)
    #     for i in range(n // 2 - 1, -1, -1):
    #         self.down(n, i, nums)
    #     # 获取第 k 大的数, 先取前 k - 1 个数
    #     for i in range(1, k):
    #         nums[0] = nums[n - i]
    #         self.down(n - i, 0, nums)
    #     return nums[0]

    def div(self, nums, l, r):
        # 选取中间元素作为切分值, 避免时间复杂度退化
        m = l + (r - l) // 2
        nums[l], nums[m] = nums[m], nums[l]

        i, j = l, r
        while i < j:
            while i < j and nums[j] >= nums[l]:
                j -= 1
            while i < j and nums[i] <= nums[l]:
                i += 1
            nums[i], nums[j] = nums[j], nums[i]
        nums[l], nums[i] = nums[i], nums[l]
        return i

    def qsort(self, nums, l, r):
        if l >= r:
            return
        m = self.div(nums, l, r)
        self.qsort(nums, 0, m - 1)
        self.qsort(nums, m + 1, r)

    def qsort_k(self, nums, l, r, k1):
        m = self.div(nums, l, r)
        if m == k1:
            return nums[m]
        elif m > k1:
            return self.qsort_k(nums, l, m - 1, k1)
        else:
            return self.qsort_k(nums, m + 1, r, k1)

    # 快速排序做法
    def findKthLargest(self, nums: List[int], k: int) -> int:
        return self.qsort_k(nums, 0, len(nums) - 1, len(nums) - k)


# @lc code=end
print(Solution().findKthLargest([2, 1], 2))
