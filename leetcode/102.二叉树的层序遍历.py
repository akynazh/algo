#
# @lc app=leetcode.cn id=102 lang=python3
#
# [102] 二叉树的层序遍历
#


# @lc code=start
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
import queue


class Solution:
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        if not root:
            return []
        max_n = 2001
        res, q = [[] for _ in range(max_n)], queue.Queue()
        r_max_n = -1
        q.put((root, 0))
        while not q.empty():
            x = q.get()
            n, c = x[0], x[1]
            r_max_n = max(c, r_max_n)
            res[c].append(n.val)
            if n.left:
                q.put((n.left, c + 1))
            if n.right:
                q.put((n.right, c + 1))

        return res[: r_max_n + 1]


# @lc code=end
