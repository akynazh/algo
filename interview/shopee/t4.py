"""
1. 给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。

注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。

示例1：输入：nums = [10,2]输出："210"

示例2：输入：nums = [3,30,34,5,9]输出："9534330"
"""

from functools import cmp_to_key


def get_arr(num):
    arr = []
    while num != 0:
        arr.append(num % 10)
        num //= 10
    arr.reverse()
    return arr


def cmp(v1, v2):
    if v1 == v2:
        return 0
    v1_arr, v2_arr = get_arr(v1), get_arr(v2)
    n1, n2 = len(v1_arr), len(v2_arr)
    i = 0
    while i < n1 and i < n2:
        if v1_arr[i] > v2_arr[i]:
            return -1
        elif v1_arr[i] < v2_arr[i]:
            return 1
        i += 1
    if i == n1 and v1_arr[0] < v2_arr[i]:
        return 1
    elif i == n2 and v2_arr[0] < v1_arr[i]:
        return -1
    else:
        return 0


def solve(nums):
    nums = sorted(nums, key=cmp_to_key(cmp))
    return "".join([str(s) for s in nums])


print(solve([10, 2]))
