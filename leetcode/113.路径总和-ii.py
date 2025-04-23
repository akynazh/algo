#
# @lc app=leetcode.cn id=113 lang=python3
#
# [113] 路径总和 II
#


# @lc code=start
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution:
    def solve(self, node, targetSum, state, res):
        state.append(node.val)
        if not node.left and not node.right:
            if targetSum - node.val == 0:
                res.append(state[:])
            state.pop()
            return
        if node.left:
            self.solve(node.left, targetSum - node.val, state, res)
        if node.right:
            self.solve(node.right, targetSum - node.val, state, res)
        state.pop()

    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> List[List[int]]:
        if not root:
            return []
        res = []
        self.solve(root, targetSum, [], res)
        return res


# @lc code=end
