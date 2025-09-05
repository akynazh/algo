"""
jobright

合并k个排序链表 · Merge K Sorted Lists

描述
合并 k 个排序链表（序列为升序序列），并且返回合并后的排序链表（序列为升序学列）。尝试分析和描述其复杂度。

样例：

输入：

lists = [2->6->null,5->null,7->null]
输出：

2->5->6->7->null
解释：

将2->6->null、5->null和7->null合并成一个升序的链表。
"""


class ListNode:
    def __init__(self, v: int) -> None:
        self.val = v
        self.next = None


def solve(lists) -> ListNode:
    if not lists:
        return
    res = ListNode(0)
    root = res
    c = len(lists)
    while c > 0:
        m = 0x7F7F7F7F
        idx = -1
        for i in range(len(lists)):
            node = lists[i]
            if node and node.val < m:
                idx = i
                m = node.val
        if idx >= 0:
            lists[idx] = lists[idx].next
            if not lists[idx]:
                c -= 1
            res.next = ListNode(m)
            res = res.next
    return root.next


h = ListNode(1)
h.next = ListNode(2)
h.next.next = ListNode(3)
h1 = ListNode(0)
h1.next = ListNode(4)
res = solve([h, h1])
while res:
    print(res.val)
    res = res.next
