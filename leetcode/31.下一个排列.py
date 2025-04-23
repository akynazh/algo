#
# @lc app=leetcode.cn id=31 lang=python3
#
# [31] 下一个排列
#
from typing import *

"""
标准的 “下一个排列” 算法可以描述为：

从后向前 查找第一个 相邻升序 的元素对 (i,j)，满足 A[i] < A[j]。此时 [j,end) 必然是降序
在 [j,end) 从后向前 查找第一个满足 A[i] < A[k] 的 k。A[i]、A[k] 分别就是上文所说的「小数」、「大数」
将 A[i] 与 A[k] 交换
可以断定这时 [j,end) 必然是降序，逆置 [j,end)，使其升序
如果在步骤 1 找不到符合的相邻元素对，说明当前 [begin,end) 为一个降序顺序，则直接跳到步骤 4

作者：Imageslr
链接：https://leetcode.cn/problems/next-permutation/solutions/80560/xia-yi-ge-pai-lie-suan-fa-xiang-jie-si-lu-tui-dao-/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
"""


# @lc code=start
class Solution:
    def nextPermutation(self, nums: List[int]) -> None:
        """
        Do not return anything, modify nums in-place instead.
        """
        a, b, n = -1, -1, len(nums)
        for i in range(n - 1, 0, -1):
            if nums[i] > nums[i - 1]:
                a, b = i - 1, i
                break
        if a == -1:
            nums.reverse()
            return nums
        for i in range(n - 1, a, -1):
            if nums[i] > nums[a]:
                nums[i], nums[a] = nums[a], nums[i]
                break
        i, j = b, n - 1
        while i < j:
            nums[i], nums[j] = nums[j], nums[i]
            i += 1
            j -= 1
        return nums


# @lc code=end
print(Solution().nextPermutation([3, 2, 1]))
