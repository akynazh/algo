#
# @lc app=leetcode.cn id=876 lang=python3
#
# [876] 链表的中间结点
#


# @lc code=start
# Definition for singly-linked list.
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


from typing import *


class Solution:
    def middleNode(self, head: Optional[ListNode]) -> Optional[ListNode]:
        fast, slow = head, head
        while fast:
            if not fast.next:
                return slow
            if not fast.next.next:
                return slow.next
            slow = slow.next
            fast = fast.next.next


# @lc code=end
