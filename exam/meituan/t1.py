# todo: sys 如何停止输入
def func(s1, s2):
    return len(s1) > len(s2) if len(s1) != len(s2) else s1 > s2


n = int(input())
pwd = input()
pwds = []
for _ in range(n):
    pwds.append(input())


# pwds = sorted(list(set(pwds)), key=len)
# pwds = sorted(pwds)
pwds = list(set(pwds))
n = len(pwds)
for i in range(n):
    for j in range(i + 1, n):
        if func(pwds[i], pwds[j]):
            pwds[j], pwds[i] = pwds[i], pwds[j]

idx = pwds.index(pwd)
l, r = idx - 1, idx + 1
while l >= 0 and len(pwds[l]) == len(pwd):
    l -= 1
l += 1
while r < len(pwds) and len(pwds[r]) == len(pwd):
    r += 1
r -= 1
c = r - l + 1

res1 = l + 1
res2 = res1 + c - 1
print(str(res1) + " " + str(res2))
