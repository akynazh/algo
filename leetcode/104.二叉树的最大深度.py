#
# @lc app=leetcode.cn id=104 lang=python3
#
# [104] 二叉树的最大深度
#
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# @lc code=start
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution:
    def check(self, node, l):
        if not node:
            return
        self.k = max(self.k, l + 1)
        self.check(node.left, l + 1)
        self.check(node.right, l + 1)

    def maxDepth(self, root: Optional[TreeNode]) -> int:
        self.k = 0
        self.check(root, 0)
        return self.k


# @lc code=end
