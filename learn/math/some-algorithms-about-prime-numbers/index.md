---
title: 有关素数的一些算法
date: 2020-07-21T20:43:04+08:00
tags: [algorithm, math]
mathjax: true
slug: some-algorithms-about-prime-numbers
---

## 埃氏筛法

问 1000000000000 以内有多少个素数？

运用朴素算法必 TLE，这时考虑埃氏筛法。

算法思路：

1. 建立 `is_prime[]` 数组，初始化为 true；
2. 从 2 开始筛取，(注意从 2 开始很重要，因为 2 为素数，否则需要改变相应后续操作)，若为 true，则继续判断是否为素数，若为素数，则将所有该素数的倍数置为 false。

相应代码：

```cpp
bool is_prime[MAXN];
//返回 n 以内的素数个数 
int sieve(int n) {
    int c = 0;
    for (int i = 2; i <= n; ++i)
        is_prime[i] = true;
    for (int i = 2; i <= n; ++i) 
        if (is_prime[i]) {
			c++;
            for (int j = 2 * i; j <= n; j += i) is_prime[j] = false;
        }    
    return c;
}
```

## 区间筛法

问 `[21479862, 21499877)` 之间有多少个素数？

这时若采用埃氏筛法，会浪费大量时间计算前面未涉及的区间，这时考虑区间筛法。

算法思路：

对$[a,b)$，由于b以内任意合数的最小质因数不大于$\sqrt{b}$，则可对$[2, \sqrt{b})$进行埃氏筛法，每次得到一个素数，就可以知道在 a 即 a以后的该素数的倍数不为质数。

相应代码：

```cpp
typedef long long ll;
bool is_prime[MAX1]; // [2, 根号b)
bool is_prime_small[MAX2]; // [0, b-a]
void sieve(ll a, ll b) {
	for(ll i = 0; i * i < b; i++) is_prime_small[i] = true;
	for(ll i = 0; i < b - a; i++) is_prime[i] = true;
	for(ll i = 2; i * i < b; i++) {
		if(is_prime_small[i]) {
			for(ll j = 2 * i; j * j < b; j += i) is_prime_small[j] = false;
			for(ll j = max(2ll, (a + i - 1) / i)) * i; j < b; j += i) is_prime[j - a] = false;
		}
	}
} 
```

注意点：

1. `max(2ll, (a + i - 1) / i) * i `，得到 a 或 a 以后的第一个该素数的倍数，最小为 2a，其中的 `2ll` 隐式地将 max 的参数类型转换为 long long 型。

2. 为什么`(a + i - 1) / i  * i` 可以得到的是 a 或 a 以后的第一个该素数（i）的倍数？证明如下：

（1）当 a 为该素数（i）的倍数时，设倍为 x，则 a 等于 ix，可以推出如下结果：

$$\begin{aligned}
(a+i-1)\div i \times i 
&= (ix + i - 1) \div i \times i \\  
&= [ix \div i + (i - 1) / i] * i \\
&= ix
\end{aligned}$$

（2）当 a 不为该素数（i）的倍数时，设 $a=ix + t$, $t >= 1 且 t < x$，从而可以推出如下结果：

$$\begin{aligned}
(ix + t + i - 1) / i \times i 
&= [ix \div i + (i - 1 + t) / i] * i \\ 
&= (x + 1) * i \\ 
&= ix + i > ix + t
\end{aligned}$$

易知 ix+i 为 i 的倍数，证毕。
