# 原始数组
nums = [3, 2, 1, 5, 6]

# 构建差分数组
diff = [0] * (len(nums) + 1)
diff[0] = nums[0]
for i in range(1, len(nums)):
    diff[i] = nums[i] - nums[i - 1]


# 区间加 val：[l, r] 加上 val
def range_add(l, r, val):
    diff[l] += val
    diff[r + 1] -= val


# 应用操作
range_add(1, 3, 4)  # 给 nums[1] 到 nums[3] 每个加 4

# 还原数组
res = [0] * len(nums)
res[0] = diff[0]
for i in range(1, len(nums)):
    res[i] = res[i - 1] + diff[i]

print(res)  # 输出: [3, 6, 5, 9, 6]
