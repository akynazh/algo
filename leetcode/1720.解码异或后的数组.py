#
# @lc app=leetcode.cn id=1720 lang=python3
#
# [1720] 解码异或后的数组
#


# @lc code=start
class Solution:
    def decode(self, encoded: List[int], first: int) -> List[int]:
        res = [first]
        for i in range(len(encoded)):
            res.append(res[i] ^ encoded[i])
        return res


# @lc code=end
