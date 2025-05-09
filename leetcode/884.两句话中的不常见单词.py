#
# @lc app=leetcode.cn id=884 lang=python3
#
# [884] 两句话中的不常见单词
#


# @lc code=start
class Solution:
    def uncommonFromSentences(self, s1: str, s2: str) -> List[str]:
        x = s1.split(" ") + s2.split(" ")
        mp, res = {}, []
        for s in x:
            mp[s] = 1 if s not in mp else mp[s] + 1
        return [k for k, v in mp.items() if v == 1]


# @lc code=end
