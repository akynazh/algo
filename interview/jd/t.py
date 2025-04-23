# 峰值
def solve(nums):
    # for i in range(len(nums) - 1):
    #     if nums[i] > nums[i + 1]:
    #         return nums[i]
    l, r = 0, len(nums) - 1
    while l <= r:
        m = l + (r - l) // 2
        if nums[m] > nums[m - 1] and nums[m] > nums[m + 1]:
            return nums[m]
        if nums[m] < nums[m - 1]:
            r = m
        else:
            l = m
    return nums[l]


print(solve([1, 2, 3, 2, 1]))


# 取最少硬币凑数
class Solution:
    def dfs(self, nums, left, count, target):
        if left == 0:
            self.ans = min(self.ans, count)
            return
        if count >= self.ans:
            return
        for v in nums:
            if left >= v:
                self.dfs(nums, left - v, count + 1, target)
                break

    def solve(self, nums, target):
        self.ans = target
        nums = sorted(nums, reverse=True)
        print(nums)
        self.dfs(nums, target, 0, target)
        print(self.ans)


# Solution().solve([1, 2, 5], 11)
