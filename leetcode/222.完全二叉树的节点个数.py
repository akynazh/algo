#
# @lc app=leetcode.cn id=222 lang=python3
#
# [222] 完全二叉树的节点个数
#


# @lc code=start
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution:
    def countNodes(self, root: Optional[TreeNode]) -> int:
        if not root:
            return 0
        # import queue

        # q = queue.Queue()
        # q.put(root)
        # c = 0
        # while not q.empty():
        #     x = q.get()
        #     c += 1
        #     if x.left:
        #         q.put(x.left)
        #     if x.right:
        #         q.put(x.right)
        # return c
        xl, xr = 0, 0
        t = root
        while t:
            xl += 1
            t = t.left
        t = root
        while t:
            xr += 1
            t = t.right
        if xl == xr:
            return 2**xl - 1
        return 1 + self.countNodes(root.left) + self.countNodes(root.right)


# @lc code=end
