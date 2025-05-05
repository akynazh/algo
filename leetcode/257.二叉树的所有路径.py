#
# @lc app=leetcode.cn id=257 lang=python3
#
# [257] 二叉树的所有路径
#


# @lc code=start
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution:
    def t(self, ns, n):
        ns.append(n.val)
        if not n.left and not n.right:
            self.res.append("->".join([str(s) for s in (ns)]))
            ns.pop()
            return
        if n.left:
            self.t(ns, n.left)
        if n.right:
            self.t(ns, n.right)
        ns.pop()

    def binaryTreePaths(self, root: Optional[TreeNode]) -> List[str]:
        if not root:
            return []
        self.res = []
        self.t([], root)
        return self.res


# @lc code=end
