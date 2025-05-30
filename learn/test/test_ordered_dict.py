import collections

dt = collections.OrderedDict()
dt["a"] = 1
dt["b"] = 2
dt["c"] = 3

for k, v in dt.items():
    print(k, v)

dt.pop("a")
dt["a"] = 1

dt.popitem(last=False)
for k, v in dt.items():
    print(k, v)
