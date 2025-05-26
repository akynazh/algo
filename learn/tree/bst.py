def isValidBST(root):
    return checkBST(root, float("-inf"), float("inf"))


def checkBST(node, low, high):
    if node is None:
        return True
    if not (low < node.val < high):
        return False
    return checkBST(node.left, low, node.val) and checkBST(node.right, node.val, high)
