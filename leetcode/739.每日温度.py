#
# @lc app=leetcode.cn id=739 lang=python3
#
# [739] 每日温度
#


# @lc code=start
class Solution:
    def dailyTemperatures(self, temperatures: List[int]) -> List[int]:
        stk = []
        n = len(temperatures)
        ans = [0 for _ in range(n)]
        for i in range(n):
            while stk and temperatures[stk[-1]] < temperatures[i]:
                idx = stk.pop()
                ans[idx] = i - idx
            stk.append(i)
        return ans

        stk = []
        n = len(temperatures)
        ans = [0 for _ in range(n)]
        for i in range(n):
            temperatures[i] = str(temperatures[i]) + f"_{i}"
        for t in temperatures:
            x = t.find("_")
            v = int(t[:x])
            idx = int(t[x + 1 :])
            while stk:
                t1 = stk[-1]
                x1 = t1.find("_")
                v1 = int(t1[:x1])
                idx1 = int(t1[x1 + 1 :])
                if v1 < v:
                    stk.pop()
                    ans[idx1] = idx - idx1
                else:
                    break
            stk.append(t)
        return ans


# @lc code=end
