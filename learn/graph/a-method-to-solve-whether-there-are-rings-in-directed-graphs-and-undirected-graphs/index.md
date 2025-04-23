---
title: 求解有向图和无向图中是否存在环的方法
date: 2020-12-02T23:50:44+08:00
categories: [CS]
tags: [algorithm, graph]
slug: a-method-to-solve-whether-there-are-rings-in-directed-graphs-and-undirected-graphs
---

## 求解无向图中是否存在环

无向图中是否存在环，可以通过 DFS 来实现。

由于他是无向图，所以每次开始一次新的递归即进入一个新的连通子图，在该次 dfs 中可以通过判断新纳入的结点是否与所在连通子图中其他已经访问过的结点存在边，如果存在则证明存在环。

```cpp
int mat[maxn][maxn], vis[maxn];
int dfs(int v) {
    vis[v] = 1;
    for (int i = 0; i < num; i++) if (mat[v][i]) {
        if (vis[i] || dfs(i)) return 1; // 该连通子图新纳入的结点与原连通子图中的某一结点有边，即产生了环
    }
    return 0;
}

memset(vis, 0, sizeof(vis));
int flag = 0; // 无环标志
for (int j = 0; j < num; j++) {
    if (!vis[j]) flag = dfs(j); 
    // 因为是无向图，所以每开始一次新的递归即进入一个新的连通子图，一定不会访问到其他连通子图的结点
    if (flag) break; // 如果在该连通子图中找到环则直接结束递归
} 
```

## 求解有向图中是否存在环

这里我也尝试过使用如上 DFS 的解法，但似乎行不通。

**因为有向图的 DFS，每开始一次新的递归未必进入一个新的连通子图！**

基于这一点，使用上述无向图的方法是行不通的，因为如果以后的递归中访问一个结点时，该结点可以指向其他连通子图访问过的结点，导致误判为出现了环，而实际上并没有。

所以需要转换思路。

转念一想，不是还有一个拓扑序的思想吗？！

**对一个有向图来说，如果存在拓扑序则该图一定不存在环。**

所以完全可以使用拓扑排序的算法来判断环的存在与否。

由于存在拓扑序的一个充要条件是：

**算法进栈结点数等于总结点数**

所以比较进栈的结点数和总的结点数即可！

```cpp
int topo() {
    stack<int> st;
    int ncount = 0;
    for (int i = 0; i < num; i++) if (!in_num[i]) st.push(i); // 找到第一个入度为 0 的结点
    while(!st.empty()) {
        int k = st.top(); st.pop(); ncount++;
        for (int i = 0; i < num; i++) {
            if (mat[k][i]) 
                if (--in_num[i] == 0) st.push(i); // 如果减去 1 后入度为 0 则属于拓扑序结点，进栈
        }
    }
    return ncount;
}

memset(in_num, 0, sizeof(in_num));
for (int j = 0; j < num; j++)
    for (int k = 0; k < num; k++) {
        scanf("%d", &mat[j][k]);
        if (mat[j][k]) in_num[k]++; // 计算入度
    }
int ncount = topo(); // 如果拓扑序中进栈的结点小于总结点数则证明含有环
if (ncount < num) ans.push_back(1); // 1 表示有环
else ans.push_back(0);
```
