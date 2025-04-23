def findLCA(root, p, q):
    if not root or root == p or root == q:
        return root
    left = findLCA(root.left, p, q)
    right = findLCA(root.right, p, q)
    if left and right:
        return root
    return left if left else right


"""
这是一个 二叉树的最近公共祖先（LCA） 问题的解法，使用了递归方法。
函数 findLCA(root, p, q) 的逻辑如下：

🔎 算法思路
	1.	终止条件：
	•	如果 root 为空（即遍历到空节点），直接返回 None。
	•	如果 root == p 或 root == q，说明当前节点是 p 或 q，直接返回当前节点。
	2.	递归遍历左右子树：
	•	递归在左子树中查找最近公共祖先：left = findLCA(root.left, p, q)
	•	递归在右子树中查找最近公共祖先：right = findLCA(root.right, p, q)
	3.	返回结果：
	•	如果左右子树都返回非空值，说明 p 和 q 分别在当前节点的左右子树中，当前节点就是最近公共祖先，返回 root。
	•	如果只有左子树返回非空值，说明 p 和 q 都在左子树中，返回 left。
	•	如果只有右子树返回非空值，说明 p 和 q 都在右子树中，返回 right。

⸻

✅ 示例

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

# 示例树结构：
#         3
#        / \
#       5   1
#      / \ / \
#     6  2 0  8
#       / \
#      7   4

root = TreeNode(3)
root.left = TreeNode(5)
root.right = TreeNode(1)
root.left.left = TreeNode(6)
root.left.right = TreeNode(2)
root.right.left = TreeNode(0)
root.right.right = TreeNode(8)
root.left.right.left = TreeNode(7)
root.left.right.right = TreeNode(4)

# 找 5 和 1 的最近公共祖先
lca = findLCA(root, root.left, root.right)
print(lca.val)  # 输出: 3

# 找 5 和 4 的最近公共祖先
lca = findLCA(root, root.left, root.left.right.right)
print(lca.val)  # 输出: 5



⸻

🚀 复杂度
	•	时间复杂度：
O(n) — 需要遍历整棵树的所有节点。
	•	空间复杂度：
O(h) — 递归调用栈的最大深度，h 是树的高度。
	•	最坏情况：树是链状，h = n
	•	平均情况：树是平衡的，h = \log n

⸻

🌟 核心思路总结
	•	利用 递归的后序遍历，从底向上查找最近公共祖先。
	•	通过返回的左右子树的结果，判断 p 和 q 的分布情况。
	•	如果 p 和 q 分别出现在当前节点的左右子树中，当前节点就是最近公共祖先。 😎
"""
