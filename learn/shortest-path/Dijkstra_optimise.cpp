// 迪杰斯特拉算法的堆优化 O(ElogV) 不含负权用 
#include <bits/stdc++.h>
using namespace std;
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
void print_ans() {
	cout << endl;
	for (int i = 0; i < V; i++) cout << d[i] << " ";
}

int main() {
	init();
	Dijkstra_optimise(0); 
	print_ans();
	return 0;
} 
/*
6 8
0 1 3
0 2 2
0 3 5
1 4 1
2 4 5
2 5 9
2 3 4
4 5 10

0 3 2 5 4 11

不可解决含负边问题 
6 9
0 1 3
0 2 2
0 3 5
1 4 1
2 4 5
2 5 9
2 3 4
4 5 10
5 3 -100

0 3 2 5 4 11
*/
