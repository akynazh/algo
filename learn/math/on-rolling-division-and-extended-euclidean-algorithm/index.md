---
title: 关于辗转相除法和扩展欧几里得算法
date: 2020-07-21T20:48:53+08:00
tags: [algorithm, math]
slug: on-rolling-division-and-extended-euclidean-algorithm
---

## gcd 辗转相除法求最大公约数

思路：反复交换取余，直到小的数为 0。

```cpp
int gcd(int a, int b){
	if(b == 0) return a;
	return gcd(b, a % b);
}
```

## exgcd 扩展欧几里得算法

先介绍**贝祖定理**：

若 a,b 为整数，则一定存在整数 x,y，使得 $ax + by = gcd(a,b)$。

即若 $ax + by = m$ 有解，则 m 一定为 gcd(a,b) 的若干倍。

下面是一道题：

有 a, -a, b, -b 四个整数，各用几次可以使得 $ax + by = 1$?

由上述思想则可知 gcd(a,b) 等于 1，可编写一个返回值为 gcd(a,b) 同时递归计算 x 和 y 的函数。

关于求出 x 和 y 推导过程：

由 $ax + by = gcd(a,b)$ (1)

通过辗转相除法的思想得：$bx_1 + (a \mod b) y_1 = gcd(a,b)$

由 $a \mod b = a - (a \div b) \times b$ 带入得:

$ay_1 + b \times (x_1 -(a \div b) \times y_1) == gcd(a,b) $(2)

由（1）（2）可得：

$x = y_1$

$y = (x_1 - (a \div b) \times y_1)$

由等式可知上层的 x,y 可以由下层的 x1,y1 来求，则可以通过递归来实现。

又由（1），当 $b = 0, a \times 1 + b \times 0 = a = gcd(a,b)$

则此时 $x = 1, y = 0$，一定会出现在递归末尾。

```cpp
int exgcd(int a, int b, int &x, int &y) {
	int d = a;
	if (b != 0) {
		d = exgcd(b, a%b, x, y);
		int temp = y;
		y = x - (a / b) * y; //上一层的y
		x = temp; //上一层的x
	} else {
		x = 1; y = 0;
	}
	return d;
}
```
