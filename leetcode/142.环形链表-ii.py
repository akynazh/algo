#
# @lc app=leetcode.cn id=142 lang=python3
#
# [142] 环形链表 II
#


# @lc code=start
# Definition for singly-linked list.
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None


from typing import *


class Solution:
    def detectCycle(self, head: Optional[ListNode]) -> Optional[ListNode]:
        # 哈希表解法
        # nodes = set()
        # while head:
        #     if head in nodes:
        #         return head
        #     nodes.add(head)
        #     head = head.next
        # return None

        # 快慢指针解法
        if not head:
            return None
        slow, fast = head, head
        while True:
            if not fast or not fast.next:
                return None
            slow = slow.next
            fast = fast.next.next
            if slow == fast:
                break

        while head != slow:
            head = head.next
            slow = slow.next
        return head


# @lc code=end
