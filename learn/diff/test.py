def apply_range_additions(n, updates):
    """
    初始化长度为 n 的数组，执行 updates 中的区间加法操作
    updates 是一个列表，格式为 [l, r, val]，表示区间 [l, r] 加 val（1-based 下标）
    返回修改后的原数组（1-based 输出）
    """
    # 差分数组初始化，长度 n+2，便于处理边界 r+1
    diff = [0] * (n + 2)

    # 差分操作：区间 [l, r] 加 val -> diff[l] += val, diff[r+1] -= val
    for l, r, val in updates:
        diff[l] += val
        diff[r + 1] -= val

    # 还原原数组 a（前缀和）
    a = [0] * (n + 1)  # a[0] 是占位不用，a[1..n] 是结果
    for i in range(1, n + 1):
        a[i] = a[i - 1] + diff[i]

    return a[1:]  # 返回 1-based 的实际内容
