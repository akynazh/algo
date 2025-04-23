# 拼正方形
# Note: 类名、方法名、参数名已经指定，请勿修改
#
#
#
# @param planks int整型 一维数组
# @return bool布尔型
#
class Solution:
    def canFormSquare(self, planks):
        n = len(planks)
        if n <= 3 or sum(planks) % 4 != 0:
            return False
        self.res = 0
        self.solve(sorted(planks), sum(planks) // 4, 0)
        return self.res >= 4

    def solve(self, planks, x, idx):
        if x == 0:
            self.res += 1
            return True
        for i in range(idx, len(planks)):
            if x - planks[i] < 0:
                return False
            self.solve(planks, x - planks[i], i + 1)
        return False
