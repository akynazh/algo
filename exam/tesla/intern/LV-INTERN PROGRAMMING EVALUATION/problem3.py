class CircleQueue:
    def __init__(self, k):
        self.k = k
        self.data = []
        self.cur = 0

    class __Full:
        def enQueue(self, x):
            # change order
            self.data[self.cur] = x
            self.cur = (self.cur + 1) % self.k

        def get(self):
            ret = self.data[self.cur :] + self.data[: self.cur]
            return ret

    # fix function name
    def enQueue(self, x):
        # def append(self, x):
        self.data.append(x)
        if len(self.data) == self.k:
            self.__class__ = self.__Full

    def get(self):
        return self.data


x = CircleQueue(1000)

for i in range(int(1e6)):
    x.enQueue(i)

print(x.get()[:10])
