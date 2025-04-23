from functools import cmp_to_key


class Solution:
    def rerankItems(self, items):
        def func(a, b):
            return len(a[0]) - len(b[0]) if a[1] == b[1] else a[1] - b[1]

        t = sorted(items, key=cmp_to_key(func))
        # t = sorted(items, key=lambda x: x[1], reverse=True)
        return t


print(Solution().rerankItems([["x", 100], ["x1", 0], ["a", 200], ["b", 0], ["c", 200]]))
