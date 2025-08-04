# 原始数组
nums = [3, 2, 1, 5, 6]

# 构建前缀和数组
prefix = [0] * (len(nums) + 1)
for i in range(len(nums)):
    prefix[i + 1] = prefix[i] + nums[i]


# 查询区间和 nums[l:r+1]
def range_sum(l, r):
    return prefix[r + 1] - prefix[l]


# 示例：
print(range_sum(1, 3))  # 输出 2 + 1 + 5 = 8
