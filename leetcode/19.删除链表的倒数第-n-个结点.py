#
# @lc app=leetcode.cn id=19 lang=python3
#
# [19] 删除链表的倒数第 N 个结点
#


# @lc code=start
# Definition for singly-linked list.
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


from typing import *


class Solution:
    def removeNthFromEnd(self, head: Optional[ListNode], n: int) -> Optional[ListNode]:
        first, fast, slow, prev = head, head, head, head
        while n != 0:
            fast = fast.next
            n -= 1
        while fast:
            fast = fast.next
            prev = slow
            slow = slow.next
        if first == slow:
            return first.next
        prev.next = slow.next
        return first


# @lc code=end
