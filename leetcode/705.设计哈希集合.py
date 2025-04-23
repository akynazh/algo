#
# @lc app=leetcode.cn id=705 lang=python3
#
# [705] 设计哈希集合
#


# @lc code=start
class MyHashSet:

    def __init__(self):
        self.hs = [-1 for _ in range(int(1e6 + 1))]
        self.mod = int(1e6) + 1

    def add(self, key: int) -> None:
        self.hs[key] = 1

    def remove(self, key: int) -> None:
        self.hs[key] = -1

    def contains(self, key: int) -> bool:
        return self.hs[key] != -1


# Your MyHashSet object will be instantiated and called as such:
# obj = MyHashSet()
# obj.add(key)
# obj.remove(key)
# param_3 = obj.contains(key)
# @lc code=end
