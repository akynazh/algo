from functools import reduce

res = reduce(lambda x, y: x * y, [1, 2, 3, 4, 5], 100)

print(res)
