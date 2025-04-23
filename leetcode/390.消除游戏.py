#
# @lc app=leetcode.cn id=390 lang=python3
#
# [390] 消除游戏
#


# @lc code=start
class Solution:
    def lastRemaining(self, n: int) -> int:
        head, step, flag = 1, 1, True
        while n != 1:
            if flag or n % 2 == 1:
                head += step
            step *= 2
            flag = not flag
            n //= 2
        return head
        # arr = [i for i in range(1, n + 1)]
        # flag = True
        # while len(arr) != 1:
        #     n = len(arr)
        #     tags = [True for _ in range(n)]
        #     x = 2 if flag else -2
        #     s = 0 if flag else n - 1
        #     e = n if flag else -1
        #     for i in range(s, e, x):
        #         tags[i] = False
        #     arr = [arr[i] for i in range(n) if tags[i]]
        #     flag = not flag

        # return arr[0]


# @lc code=end
