#
# @lc app=leetcode.cn id=110 lang=python3
#
# [110] 平衡二叉树
#


# @lc code=start
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution:
    # def h(self, root):
    #     return 1 + max(self.h(root.left), self.h(root.right)) if root else 0

    def h(self, root):
        if not root:
            return 0
        l = self.h(root.left)
        r = self.h(root.right)
        if l == -1 or r == -1 or abs(l - r) > 1:
            return -1
        return max(l, r) + 1

    def isBalanced(self, root: Optional[TreeNode]) -> bool:
        return self.h(root) != -1
        # if not root:
        #     return True
        # return (
        #     abs(self.h(root.left) - self.h(root.right)) <= 1
        #     and self.isBalanced(root.left)
        #     and self.isBalanced(root.right)
        # )


# @lc code=end
