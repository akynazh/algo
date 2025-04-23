#
# @lc app=leetcode.cn id=111 lang=python3
#
# [111] 二叉树的最小深度
#


# @lc code=start
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def check(self, node: TreeNode, c):
        if not node.left and not node.right:
            self.v = min(self.v, c)
            return
        if node.left:
            self.check(node.left, c + 1)
        if node.right:
            self.check(node.right, c + 1)

    def minDepth(self, root: Optional[TreeNode]) -> int:
        if not root:
            return 0
        self.v = 100_0000
        self.check(root, 1)
        return self.v


# @lc code=end
