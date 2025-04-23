# 链表反转


class Node:
    def __init__(self, val):
        self.next = None
        self.val = val


def solve(root: Node):
    node = root
    prev = None
    while node:
        next = node.next
        node.next = prev
        prev = node
        node = next
    return prev


def solve1(node: Node, prev: Node):
    if node.next:
        solve1(node.next, node)

    node.next = prev
    print(node.val)


root = Node(1)
root.next = Node(2)
root.next.next = Node(3)
node = root
while node:
    print(node.val)
    node = node.next
node1 = root

solve1(node1, None)

# t = solve(node1)

# print(t)
# while t:
#     print(t.val)
#     t = t.next
