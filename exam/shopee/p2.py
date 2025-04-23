class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


#
# Note: 类名、方法名、参数名已经指定，请勿修改
#
#
#
# @param root TreeNode类
# @return int整型二维数组
#
import queue


class Solution:
    def levelOrder(self, root):
        q = queue.Queue()
        q.put((root, 0))
        res = [[] for _ in range(1001)]
        cc = 0
        while not q.empty():
            t = q.get()
            node, c = t[0], t[1]
            res[c].append(node.val)
            if node.left:
                q.put((node.left, c + 1))
            if node.right:
                q.put((node.right, c + 1))
            cc = max(cc, c)
        return res[: cc + 1]


# {8,17,21,18,#,#,6}

root = TreeNode(8)
root.left = TreeNode(17)
root.right = TreeNode(21)
print(Solution().levelOrder(root))
