from functools import reduce

f = lambda m, n: sorted(  # 对生成的素数序列进行排序
    list(
        filter(
            lambda a: a in range(m, n),  # 过滤出 [m, n) 范围内的所有素数
            list(
                reduce(
                    (
                        lambda x, y: x - set(range(y**2, n, y)) if (y in x) else x
                    ),  # 埃拉托色尼筛法，从 x 中筛去所有 y 的倍数
                    range(2, int(n**0.5) + 1),  # 需要用于筛选的数字(所有素数的倍数)
                    set(range(2, n)),  # 初始数序列
                )
            ),
        )
    )
)


# 一个清晰版本
def simple_version(m, n):
    primes = [True] * (n + 1)
    p = 2
    while p * p <= n:
        if primes[p]:
            for i in range(p * p, n + 1, p):
                primes[i] = False
        p += 1
    return [p for p in range(m, n) if primes[p]]
