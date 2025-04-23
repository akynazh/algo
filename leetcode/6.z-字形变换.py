#
# @lc app=leetcode.cn id=6 lang=python3
#
# [6] Z 字形变换
#


# @lc code=start
class Solution:
    def convert(self, s: str, numRows: int) -> str:
        if numRows < 2:
            return s
        res = ["" for _ in range(numRows)]
        i, inc = 0, 1
        for c in s:
            res[i] += c
            if i + inc == numRows or i + inc < 0:
                inc = -inc
            i += inc
        return "".join(res)


# @lc code=end
