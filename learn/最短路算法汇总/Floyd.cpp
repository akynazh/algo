// 弗洛伊德算法 O(V^3)
#include <bits/stdc++.h>
using namespace std;
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
void print_ans() {
	for (int i = 0; i < V; i++) {
		cout << endl;
		for (int j = 0; j < V; j++) {
			if (d[i][j] > 100) printf("%3d ", -1);
			// 大于某个值视为不可达 
			else printf("%3d ", d[i][j]);
		}
	}
}
int main() {
	init();
	Floyd();
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

-1  3  2  5  4 11
-1 -1 -1 -1  1 11
-1 -1 -1  4  5  9
-1 -1 -1 -1 -1 -1
-1 -1 -1 -1 -1 10
-1 -1 -1 -1 -1 -1

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

 -1   3   2 -89   4  11
 -1  -1  -1 -89   1  11
 -1  -1  -1 -91   5   9
 -1  -1  -1  -1  -1  -1
 -1  -1  -1 -90  -1  10
 -1  -1  -1 -100  -1  -1
*/


