#
# @lc app=leetcode.cn id=234 lang=python3
#
# [234] 回文链表
#


# @lc code=start
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution:
    def isPalindrome(self, head: Optional[ListNode]) -> bool:
        s, f = head, head.next
        while f and f.next:
            s = s.next
            f = f.next.next
        head1 = s.next
        s.next = None

        pre = None
        cur = head1
        while cur:
            t = cur.next
            cur.next = pre
            pre = cur
            cur = t
        head1 = pre

        while head and head1:
            if head.val != head1.val:
                return False
            head, head1 = head.next, head1.next
        return True


# @lc code=end
