class SegmentTree:
    def __init__(self, data):
        self.n = len(data)
        self.size = self.n * 4
        self.tree = [0] * self.size  # 区间和
        self.lazy = [0] * self.size  # 懒惰标记
        self.build(data, 1, 0, self.n - 1)

    def build(self, data, node, l, r):
        if l == r:
            self.tree[node] = data[l]
            return
        mid = (l + r) // 2
        self.build(data, node * 2, l, mid)
        self.build(data, node * 2 + 1, mid + 1, r)
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]

    def push(self, node, l, r):
        if self.lazy[node]:
            mid = (l + r) // 2
            # 更新左右子树
            self.tree[node * 2] += self.lazy[node] * (mid - l + 1)
            self.tree[node * 2 + 1] += self.lazy[node] * (r - mid)
            # 标记传下去
            self.lazy[node * 2] += self.lazy[node]
            self.lazy[node * 2 + 1] += self.lazy[node]
            self.lazy[node] = 0

    def add(self, L, R, val, node=1, l=0, r=None):
        if r is None:
            r = self.n - 1
        if R < l or r < L:
            return
        if L <= l and r <= R:
            self.tree[node] += val * (r - l + 1)
            self.lazy[node] += val
            return
        self.push(node, l, r)
        mid = (l + r) // 2
        self.add(L, R, val, node * 2, l, mid)
        self.add(L, R, val, node * 2 + 1, mid + 1, r)
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]

    def query(self, L, R, node=1, l=0, r=None):
        if r is None:
            r = self.n - 1
        if R < l or r < L:
            return 0
        if L <= l and r <= R:
            return self.tree[node]
        self.push(node, l, r)
        mid = (l + r) // 2
        return self.query(L, R, node * 2, l, mid) + self.query(
            L, R, node * 2 + 1, mid + 1, r
        )


a = [1, 3, 5, 7, 9, 11]
st = SegmentTree(a)

print(st.query(1, 3))  # 查询区间 [1, 3] 的和 -> 3 + 5 + 7 = 15

st.add(1, 4, 2)  # 给区间 [1, 4] 每个元素加 2
print(st.query(1, 3))  # 查询更新后 [1, 3] 的和 -> 5 + 7 + 9 = 21
