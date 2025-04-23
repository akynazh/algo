#
# @lc app=leetcode.cn id=12 lang=python3
#
# [12] 整数转罗马数字
#


# @lc code=start
class Solution:
    VALUE_SYMBOLS = [
        (1000, "M"),
        (900, "CM"),
        (500, "D"),
        (400, "CD"),
        (100, "C"),
        (90, "XC"),
        (50, "L"),
        (40, "XL"),
        (10, "X"),
        (9, "IX"),
        (5, "V"),
        (4, "IV"),
        (1, "I"),
    ]

    def intToRoman(self, num: int) -> str:
        roman = ""
        for value, symbol in self.VALUE_SYMBOLS:
            while num >= value:
                num -= value
                roman += symbol
        return roman


# @lc code=end
