#
# @lc app=leetcode.cn id=236 lang=python3
#
# [236] 二叉树的最近公共祖先
#


# @lc code=start
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution:
    parents = {}

    def get_node_parent(self, node: TreeNode):
        if node.left:
            self.parents[node.left.val] = node
            self.get_node_parent(node.left)
        if node.right:
            self.parents[node.right.val] = node
            self.get_node_parent(node.right)

    def lowestCommonAncestor(
        self, root: "TreeNode", p: "TreeNode", q: "TreeNode"
    ) -> "TreeNode":
        self.get_node_parent(root)
        self.parents[root.val] = None
        visited = set()
        while p:
            visited.add(p.val)
            p = self.parents[p.val]
        while q:
            if q.val in visited:
                return q
            q = self.parents[q.val]


# @lc code=end
