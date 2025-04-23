---
title: 最短路问题汇总
date: 2020-11-17T17:55:32+08:00
categories: [CS]
tags: [algorithm]
slug: summary-of-shortest-path-problems
---

注意，这里为了方便描述算法，所以都用了最易理解的邻接矩阵来写，比赛中为了追求效率，一般将邻接矩阵改为**链式前向星**或者邻接表。

## 迪杰斯特拉算法 O(V^2)

```cpp
const int MAXN = 100;
const int INF = 0x3f3f3f3f;
// 有向无环图 DAG
int V, E; 				// 顶点数和边数
int graph[MAXN][MAXN];  // DAG邻接矩阵，初始值为INF，不可达为INF，否则为cost值 
int d[MAXN]; 			// 从某点s出发到其它任意结点的最短路径长度，初始值为INF 
int visited[MAXN]; 		// 某点是否访问过，访问过则为1否则为0

// 初始化图 
void init() {
	memset(graph, 0x3f, sizeof(graph));
	cin >> V >> E;
	int from, to, cost;
	for (int i = 0 ; i < E; i++) {
		cin >> from >> to >> cost;
		graph[from][to] = cost;
	}
}
// 迪杰斯特拉算法求解最短路，针对点展开 
void Dijkstra(int s) {
	memset(d, 0x3f, sizeof(d));
	memset(visited, 0, sizeof(visited));
	visited[s] = 1;
	for(int i = 0; i < V; i++) d[i] = graph[s][i];
	d[s] = 0;
	int k, min_cost;
	// 无负边时最多更新n-1(其他结点数)次 
	for(int i = 0; i < V - 1; i++){
		min_cost = INF;
		// 寻找最未被访问的且权值最小的路径，需要优化的地方 
		for(int j = 0; j < V; j++){
			if(!visited[j] && d[j] < min_cost){
				k = j;
				min_cost = d[j];
			}
		}
		visited[k] = 1;
		// 利用找到的结点更新最短路径
		for(int j = 0; j < V; j++){
			if(!visited[j] && min_cost + graph[k][j] < d[j]){
				d[j] = min_cost + graph[k][j];
			}
		}
	}
} 
```

## 迪杰斯特拉算法的堆优化 O(ElogV) 不含负权就用它 ~

```cpp
const int MAXN = 100;
const int INF = 0x3f3f3f3f;
// 有向无环图 DAG
int V, E; 				// 顶点数和边数
int graph[MAXN][MAXN];  // DAG邻接矩阵，初始值为INF，不可达为INF，否则为cost值 
int d[MAXN]; 			// 从某点s出发到其它任意结点的最短路径长度，初始值为INF 
int visited[MAXN]; 		// 某点是否访问过，访问过则为1否则为0

typedef pair<int, int> P; // first: 最短距离 second：通往的顶点编号 
priority_queue<P, vector<P>, greater<P> > que; // greater<T> 从小到大取出 

// 初始化图 
void init() {
	memset(graph, 0x3f, sizeof(graph));
	cin >> V >> E;
	int from, to, cost;
	for (int i = 0 ; i < E; i++) {
		cin >> from >> to >> cost;
		graph[from][to] = cost;
	}
}
// 迪杰斯特拉算法堆优化求解最短路，针对点展开
void Dijkstra_optimise(int s) {
	memset(d, 0x3f, sizeof(d));
	memset(visited, 0, sizeof(visited));
	// for(int i = 0; i < V; i++) d[i] = graph[s][i];
	// 开始min_cost为0，若加上这一步，0 + graph[k][i]  == d[i] == graph[k][i]，导致无法更新队列 
	d[s] = 0;
	int k, min_cost;
	que.push(P(0, s));
	while (!que.empty()) {
		P p = que.top(); que.pop();
		// 通过堆优化直接获得了目标 
		min_cost = p.first;
		k = p.second;
		// 若已经有更短的路径则不更新 
		if (min_cost > d[k]) continue;
		visited[k] = 1;
		for (int i = 0; i < V; i++) {
			if (!visited[i] && min_cost + graph[k][i] < d[i]) {
				d[i] = graph[k][i] + min_cost;
				que.push(P(d[i], i));
			}
		} 
	} 
}
```

## 贝尔曼福德算法 O(VE)

```cpp
using namespace std;
const int MAXN = 100;
const int MAXE = 100; 
const int INF = 0x3f3f3f3f;
// 有向无环图 DAG
struct Edge {
	int from, to, cost;
};
int V, E; 				// 顶点数和边数 
int d[MAXN]; 			// 从某点s出发到其它任意结点的最短路径长度，初始值为INF 
Edge edge[MAXE];		// 边集  

// 初始化图 
void init() {
	cin >> V >> E;
	int from, to, cost;
	for (int i = 0 ; i < E; i++) 
		cin >> edge[i].from >> edge[i].to >> edge[i].cost;
}
// 贝尔曼福德算法求解最短路，针对边展开 
void Bellman_Ford(int s)
{
	memset(d, 0x3f, sizeof(d));
	d[s] = 0;
	while (1) {
		int flag = 0;
		// DAG下最少更新E次
		for (int i = 0; i < E; i++) {
			Edge e = edge[i];
			if (d[e.from] != INF && d[e.to] > d[e.from] + e.cost) {
				d[e.to] = d[e.from] + e.cost;
				flag = 1;
			}
		}
		// 如果没出现更新则退出
		if (!flag) break;
	} 
} 
```

## 贝尔曼福德算法优化（SPFA + 优先队列）含负边就用它~

虽然大家都说SPFA已死，但其实是说笑罢了，含负权边或者负环的题还真离不开这东西~

```cpp
const int MAXN = 100;
const int INF = 0x3f3f3f3f;
// 有向无环图 DAG
int V, E; 				// 顶点数和边数
int graph[MAXN][MAXN];  // DAG邻接矩阵，初始值为INF，不可达为INF，否则为cost值 
int d[MAXN]; 			// 从某点s出发到其它任意结点的最短路径长度，初始值为INF 
int vis[MAXN]; 		 // 结点是否已入队 
int push_count[MAXN]; // 结点入队次数，若大于等于V，则证明存在负环 
priority_queue<int, vector<int>, greater<int> > que; // greater<T> 从小到大取出 

// 初始化图 
void init() {
	memset(graph, 0x3f, sizeof(graph));
	cin >> V >> E;
	int from, to, cost;
	for (int i = 0 ; i < E; i++) {
		cin >> from >> to >> cost;
		graph[from][to] = cost;
	}
}
// SPFA算法求解最短路 
void SPFA(int s) {
	memset(d, 0x3f, sizeof(d));
	memset(vis, 0, sizeof(vis));
	memset(push_count, 0, sizeof(push_count));
	d[s] = 0;
	que.push(s);
	vis[s] = 1;
	push_count[s]++;
	while (!que.empty()) {
		int flag = 0; // 负环判断 
		int v = que.top(); que.pop(); vis[v] = 0;
		for (int i = 0; i < V; i++) {
			if (d[v] + graph[v][i] < d[i]) {
				d[i] = d[v] + graph[v][i];
				if (!vis[i]) {
					que.push(i);
					vis[i] = 1;
					push_count[i]++;
					if (push_count[i] >= V) {
						flag = 1;
						break;
					}
				}
			}
		}
		if (flag) {
			cout << "有负环" << endl;
			break;
		} 
	} 
}
```

## 弗洛伊德算法 O(V^3) 多源问题可以考虑用它~

```cpp
const int MAXN = 100;
const int INF = 0x3f3f3f3f;
// 有向无环图 DAG
int V, E; 				// 顶点数和边数
int graph[MAXN][MAXN];  // DAG邻接矩阵，初始值为INF，不可达为INF，否则为cost值
int d[MAXN][MAXN];		// 从任意结点出发到其它任意结点的最短路径长度，初始值为INF
 
// 初始化图 
void init() {
	memset(graph, 0x3f, sizeof(graph));
	cin >> V >> E;
	int from, to, cost;
	for (int i = 0 ; i < E; i++) {
		cin >> from >> to >> cost;
		graph[from][to] = cost;
	}
}
// 弗洛伊德算法求解最短路
void Floyd()
{
	// 初始化矩阵d 
	memset(d, 0x3f, sizeof(d));
	for(int i = 0; i < V; i++)
		for(int j = 0; j < V; j++)
			d[i][j] = graph[i][j];
	for(int k = 0; k < V; k++){//以顶点k为中转
		for(int i = 0; i < V; i++){//从顶点i到顶点j
			for(int j = 0; j < V; j++){
				if (d[i][j] > d[i][k] + d[k][j]) 
					d[i][j] = d[i][k] + d[k][j];
			}
		} 
	} 
}
```

**附：**

## 链式前向星

```C++
const int MAXN = 100;
struct Edge {
	int to; // 边的终点 
	int next; // 与该边同起点的下一条边的编号 
	int w; // 边的权值 
};
Edge edge[MAXN]; // 所有边的集合
int head[MAXN]; // 所有起点的第一条边的编号，初始化为-1 
int V, E;
// 建立图 
void build() {
	int from, to, w, cnt = 0;
	for (int i = 0; i < E; i++) {
		cin >> from >> to >> w;
		edge[cnt].to = to;
		edge[cnt].w = w;
		edge[cnt].next = head[from]; 
        // 因此可将head初始化为-1，当next为-1时代表以该点为起点的边访问结束 
		head[from] = cnt++;
	}
}
// 遍历整个图 
void traverse() {
	for (int i = 0; i < V; i++) {
		for (int j = head[i]; j != -1; j = edge[j].next) {
			cout << " from " << i << " to " << edge[j].to << " weight: " << edge[j].w << endl;
		}
	}
}
// 遍历某个结点的邻接点 
void traverse_v(int v) {
	for (int i = head[v]; i != -1; i = edge[i].next) {
		cout << " from " << v << " to " << edge[i].to << " weight: " << edge[i].w << endl; 
	}
}
```
