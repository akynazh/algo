class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))  # 每个节点初始指向自己
        self.rank = [1] * n  # 秩（可看作集合的深度）

    def find(self, x):
        # 路径压缩优化：递归压缩父节点路径
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        # 合并两个集合，按秩优化
        root_x = self.find(x)
        root_y = self.find(y)

        if root_x == root_y:
            return False  # 已在同一集合

        # 小树挂到大树下面
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1

        return True


class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]

    def union(self, x, y):
        self.parent[self.find(x)] = self.find(y)
