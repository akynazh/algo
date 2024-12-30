#include <bits/stdc++.h>
using namespace std;
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
		edge[cnt].next = head[from]; // 因此可将head初始化为-1，当next为-1时代表以该点为起点的边访问结束 
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
int main() {
	memset(head, -1, sizeof(head));
	cin >> V >> E;
	build(); 
	traverse();
	cout << endl;
	traverse_v(1);
	return 0;
} 
/*
5 7
1 2 1
2 3 2
3 4 3
1 3 4
4 1 5
1 5 6
4 5 7
*/
