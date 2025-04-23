#
# @lc app=leetcode.cn id=496 lang=python3
#
# [496] 下一个更大元素 I
#


# @lc code=start
class Solution:
    def nextGreaterElement(self, nums1: List[int], nums2: List[int]) -> List[int]:
        stk = []
        mp = [-1 for _ in range(100001)]
        ans = []
        for v in nums2:
            while stk and v > stk[-1]:
                mp[stk.pop()] = v
            stk.append(v)
        for v in nums1:
            ans.append(mp[v])
        return ans


# @lc code=end
