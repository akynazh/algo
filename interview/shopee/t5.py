def div(nums, l, r):
    i, j = l, r
    while i < j:
        while i < j and nums[j] >= nums[l]:
            j -= 1
        while i < j and nums[i] <= nums[l]:
            i += 1
        nums[i], nums[j] = nums[j], nums[i]
    nums[l], nums[i] = nums[i], nums[l]
    return i


def qsort_k(nums, l, r, k):
    if l >= r:
        return
    p = div(nums, l, r)
    if p < k:
        qsort_k(nums, p + 1, r, k)
    else:
        qsort_k(nums, l, p - 1, k)


def find_mid(nums):
    n = len(nums)
    k = n // 2
    if n % 2 == 0:
        qsort_k(nums, 0, n - 1, k - 1)
        t1 = nums[k - 1]
        qsort_k(nums, 0, n - 1, k)
        t2 = nums[k]
        return (t1 + t2) / 2
    else:
        qsort_k(nums, 0, n - 1, k)
        return nums[k]


print(find_mid([28, 29, 49, 30, 48]))
print(find_mid([39, 14, 22, 17]))
