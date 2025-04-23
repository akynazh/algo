"""
让所有数字出现次数不一样的删除数字的最小代价
"""

n = int(input())
t = input().split(" ")
nums = [int(s) for s in t]
mp = {}
for v in nums:
    if v in mp:
        mp[v] += 1
    else:
        mp[v] = 1
tmp = [[] for _ in range(100001)]
for k, v in mp.items():
    tmp[v].append(k)
ans = 0
for i in range(100000, 0, -1):
    ln = len(tmp[i])
    if ln == 0 or ln == 1:
        continue
    tmp[i] = sorted(tmp[i])
    tmp[i - 1] += tmp[i][: ln - 1]
    ans += sum(tmp[i][: ln - 1])
print(ans)
