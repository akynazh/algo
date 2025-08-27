// 贝尔曼福德算法的优化 O(VE) SPFA_优先队列 
#include <bits/stdc++.h>
using namespace std;
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
void print_ans() {
	cout << endl;
	for (int i = 0; i < V; i++) cout << d[i] << " ";
}

int main() {
	init();
	SPFA(0); 
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

可解决含负边问题 
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

0 3 2 -89 4 11

可判断是否含负环 
6 7
0 1 2
1 2 2
2 3 3
2 4 4
3 4 5
4 5 -7
5 3 1

有负环

0 2 4 -2 4 -3
*/
