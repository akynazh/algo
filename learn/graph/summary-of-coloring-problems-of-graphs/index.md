---
title: 图的着色问题汇总
date: 2020-12-05T12:34:05+08:00
tags: [algorithm, graph]
slug: summary-of-coloring-problems-of-graphs
---

## 二分图问题

能否只用 2 种颜色对一个图上色，并且使得共边顶点不同色？

代码解决：

```c
vector<int> G[MAX_V];
int vertexes;
int edges;
int color[MAX_V];  //二分图，颜色为1或-1
bool dfs(int v, int c) {
  color[v] = c;
  for (int i = 0; i < G[v].size(); i++) {
    if (color[G[v][i]] == c) return false;
    if (color[G[v][i]] == 0 && !dfs(G[v][i], -c)) return false;
  }
  return true;
}
void solve() {
  for (int i = 0; i < vertexes; i++) {
    if (color[i] == 0) {
      if (!dfs(i, 1)) {
        printf("No\n");
        return;
      }
    }
  }
  printf("Yes\n");
}
solve();
```

二分图是着色问题的特殊情况，即图的最小着色数为2，接下来的问题是：

## 图的最多着色方法数问题

对 V 个顶点，E 条边的图，用小于 num_color 种颜色进行涂色，要求共边的两点颜色不相同，一共有多少种涂法？

重要思想：回溯

代码解决：

```c
int G[MAX][MAX]; // init: 0
int color[MAX]; // init: 0
int V, E, num_color, ccount = 0;
bool Judge_Color(int v, int col) { // 对v点用col继续涂色
  for (int i = 1; i <= V; i++)
    if (G[i][v] == 1 && color[i] == col) return false;
  return true;
}
void Solve(int v) {
  if (v > V) { // 涂色完毕
    ccount++;
    for (int i = 1; i <= V; i++) cout << color[i] << " ";
    cout << endl;
    return;
  }
  for (int i = 1; i <= num_color; i++) {
    if (Judge_Color(v, i)) { // 如果可以涂色则进行涂色
      color[v] = i;
      Solve(v + 1);
      color[v] = 0; // 回溯
    }
  }
}
Solve(1); // 从第一个结点开始涂色
```

以上问题我们给定了最小的涂色数，求的是方法的个数，反过来呢？

现在我们想要用**最少的颜色**达到目标，就是要求一幅图的最小着色数了：

**思路**：从小到大增加着色数，判断以当前数量的颜色是否可以成功涂色，如果可以一一选取其中可以涂色的颜色进行涂色，如果不可以则新增颜色涂色

## 最小着色数问题

基本思想：剪枝与回溯

```c
int G[MAX][MAX]; // init: 0
int color[MAX]; // init: 0
int V, E, c_min = MAX + 1;
bool Judge_Color(int v, int col) { // 判断v是否可以用col涂色
  for (int i = 1; i <= V; i++)
    if (G[i][v] == 1 && color[i] == col) return false;
  return true;
}
void Solve(int v, int num_color) {
  if (num_color >= c_min) return;  //剪枝
  if (v > V) { // 涂色完毕
    c_min = min(c_min, num_color);
    return;
  }
  for (int i = 1; i <= num_color; i++) {
    if (Judge_Color(v, i)) { // 如果可以用这种颜色涂色
      color[v] = i;
      Solve(v + 1, num_color);
      color[v] = 0;  //回溯
    }
  }
  // num_color种颜色不够用了
  color[v] = num_color + 1; // 新增一种颜色
  Solve(v + 1, num_color + 1);
  color[v] = 0; // 回溯
}
Solve(1, 1);
```

下面是有关最小着色问题相应实际问题的解决。

## 分考场问题

n 个人参加某项特殊考试。为了公平，要求任何两个认识的人不能分在同一个考场。求是少需要分几个考场才能满足条件。

```c
int G[MAX][MAX]; // init 0
int room[MAX][MAX]; // 保存教室i中第j个学生的id, init: 0
int c_stu, c_relation, min_room = MAX + 1, count_room;
void solve(int x, int count_room) {
  if (count_room >= min_room) return; // 剪枝
  if (x > c_stu) { // 学生分配完毕
    min_room = min(min_room, count_room);
    return;
  }
  for (int i = 1; i <= count_room; i++) {
    int flag = true, j = 0;
    while (room[i][j]) { //查找学生教室中是否有认识的人
      if (G[x][room[i][j]]) {
        flag = false;
        break;
      }
      j++;
    }
    if (flag) { // 教室里没有认识的人
      room[i][j] = x;
      solve(x + 1, count_room); // 继续用现有教室分配下一个学生
      room[i][j] = 0;
    }
  }
  // 教室不够 新增一个教室并把学生放进教室
  room[count_room + 1][0] = x;
  solve(x + 1, count_room + 1);
  room[count_room + 1][0] = 0;
}

solve(1, 1); // 最开始用一个教室分配学生
```
