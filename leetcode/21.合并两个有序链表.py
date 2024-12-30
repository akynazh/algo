#
# @lc app=leetcode.cn id=21 lang=python3
#
# [21] 合并两个有序链表
#
from typing import Optional


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


# @lc code=start
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next


class Solution:
    def mergeTwoLists(
        self, list1: Optional[ListNode], list2: Optional[ListNode]
    ) -> Optional[ListNode]:
        if not list1 and not list2:
            return None
        if not list1:
            return list2
        if not list2:
            return list1
        lst = ListNode()
        root = lst
        p1, p2 = list1, list2
        while p1 and p2:
            if p1.val <= p2.val:
                lst.next = ListNode(val=p1.val)
                p1 = p1.next
            else:
                lst.next = ListNode(val=p2.val)
                p2 = p2.next
            lst = lst.next
        if p1:
            lst.next = p1
        elif p2:
            lst.next = p2

        return root.next


# @lc code=end
