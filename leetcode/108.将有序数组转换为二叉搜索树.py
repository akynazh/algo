#
# @lc app=leetcode.cn id=108 lang=python3
#
# [108] 将有序数组转换为二叉搜索树
#


# @lc code=start
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right


class Solution:
    def build(self, nums, l, r):
        if l > r:
            return
        m = l + (r - l) // 2
        root = TreeNode(nums[m])
        root.left = self.build(nums, l, m - 1)
        root.right = self.build(nums, m + 1, r)
        return root

    def sortedArrayToBST(self, nums: List[int]) -> Optional[TreeNode]:
        return self.build(nums, 0, len(nums) - 1)


# @lc code=end
