// 贝尔曼福德算法 O(VE)
#include <bits/stdc++.h>
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
// 贝尔曼福德算法求解最短路，针对边展开，只需建立边集，无需建图即可完成 
void Bellman_Ford(int s)
{
	memset(d, 0x3f, sizeof(d));
	d[s] = 0;
	int c = 0; 
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
		c++;
		if (c >= V) { // 松弛操作最多执行 n-1 次
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
	Bellman_Ford(0); 
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

可解决含负边DAG
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
