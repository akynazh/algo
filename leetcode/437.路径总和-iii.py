#
# @lc app=leetcode.cn id=437 lang=python3
#
# [437] 路径总和 III
#
from typing import *


# @lc code=start
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def solve(self, node, preSum, mp, targetSum):
        if not node:
            return
        preSum += node.val
        if preSum - targetSum in mp:
            self.res += mp[preSum - targetSum]
        mp[preSum] = mp[preSum] + 1 if preSum in mp else 1
        self.solve(node.left, preSum, mp, targetSum)
        self.solve(node.right, preSum, mp, targetSum)
        mp[preSum] = mp[preSum] - 1

    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> int:
        self.res = 0
        self.solve(root, 0, {0: 1}, targetSum)
        return self.res


# @lc code=end
