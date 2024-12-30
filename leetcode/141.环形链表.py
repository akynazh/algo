#
# @lc app=leetcode.cn id=141 lang=python3
#
# [141] 环形链表
#
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None


from typing import Optional

# @lc code=start
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None


class Solution:
    def hasCycle(self, head: Optional[ListNode]) -> bool:
        if not head:
            return False
        slow, fast = head, head.next
        while slow != fast:
            if not fast or not fast.next:
                return False
            slow = slow.next
            fast = fast.next.next
        return True


# @lc code=end
