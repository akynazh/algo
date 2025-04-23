#
# Note: 类名、方法名、参数名已经指定，请勿修改
#
#
#
# @param items int整型 二维数组
# @return int整型二维数组
#
from functools import cmp_to_key


class Solution:
    def rerankItems(self, items):
        # def func(a, b):
        #     return 1 if a[1] <= b[1] else -1

        # return sorted(items, key=cmp_to_key(func))
        return list(filter(lambda x: x[1] > 0, items)) + list(
            filter(lambda x: x[1] == 0, items)
        )


print(Solution().rerankItems([[2, 100], [5, 0], [3, 200], [4, 0], [100, 0]]))
