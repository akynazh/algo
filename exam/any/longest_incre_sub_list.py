def solve(nums):
    mx = 1
    n = len(nums)
    for i in range(n - 1):
        cur = 1
        cur_v = nums[i]
        for j in range(i + 1, n):
            if nums[j] >= cur_v:
                cur += 1
                cur_v = nums[j]
        mx = max(mx, cur)
    return mx


print(solve([11, 8, 2, 6, 3, 7, 102, 19]))
