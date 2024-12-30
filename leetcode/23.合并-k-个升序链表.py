#
# @lc app=leetcode.cn id=23 lang=python3
#
# [23] 合并 K 个升序链表
#
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


from typing import *

# @lc code=start
# Definition for singly-linked list.

from queue import PriorityQueue


class Solution:
    def mergeKLists(self, lists: List[Optional[ListNode]]) -> Optional[ListNode]:
        pq = PriorityQueue()
        for head in lists:
            while head:
                pq.put(head.val)
                head = head.next
        head = ListNode()
        tail = head
        while not pq.empty():
            tail.next = ListNode(pq.get())
            tail = tail.next
        return head.next


# @lc code=end
