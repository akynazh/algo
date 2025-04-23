"""
层序遍历
"""

# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None


# root = TreeNode(4)
# root.left = TreeNode(21)
# root.right = TreeNode(19)


#
# Note: 类名、方法名、参数名已经指定，请勿修改
#
#
#
# @param root TreeNode类  二叉树数组
# @return int整型二维数组
#
class Solution:
    def levelNode(self, root):
        q = []
        if not root:
            return q
        q.append((root, 0))
        res = {}
        mc = -1
        while len(q) != 0:
            xy = q.pop(0)
            node, c = xy[0], xy[1]
            if c > mc:
                mc = c
            if c not in res.keys():
                res[c] = [node.val]
            else:
                res[c].append(node.val)
            left, right = node.left, node.right
            if left:
                q.append((left, c + 1))
            if right:
                q.append((right, c + 1))
        res1 = [[] for _ in range(mc + 1)]
        for k, v in res.items():
            res1[k] = v
        return res1


# print(Solution().levelNode(root))
