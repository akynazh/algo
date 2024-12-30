// 迪杰斯特拉算法 O(V^2)
#include <bits/stdc++.h>
using namespace std;
const int MAXN = 100;
const int INF = 0x3f3f3f3f;
// 有向无环图 DAG
int V, E;  // 顶点数和边数
int graph[MAXN][MAXN];  // DAG邻接矩阵，初始值为INF，不可达为INF，否则为cost值
int d[MAXN];  // 从某点s出发到其它任意结点的最短路径长度，初始值为INF
int visited[MAXN];  // 某点是否访问过，访问过则为1否则为0

// 初始化图
void init() {
    memset(graph, 0x3f, sizeof(graph));
    cin >> V >> E;
    int from, to, cost;
    for (int i = 0; i < E; i++) {
        cin >> from >> to >> cost;
        graph[from][to] = cost;
    }
}
// 迪杰斯特拉算法求解最短路，针对点展开
void Dijkstra(int s) {
    memset(d, 0x3f, sizeof(d));
    memset(visited, 0, sizeof(visited));
    visited[s] = 1;
    for (int i = 0; i < V; i++) d[i] = graph[s][i];
    d[s] = 0;
    int k, min_cost;
    // 无负边时最多更新n-1(其他结点数)次
    for (int i = 0; i < V - 1; i++) {
        min_cost = INF;
        // 寻找最未被访问的且权值最小的路径，需要优化的地方
        for (int j = 0; j < V; j++) {
            if (!visited[j] && d[j] < min_cost) {
                k = j;
                min_cost = d[j];
            }
        }
        visited[k] = 1;
        // 利用找到的结点更新最短路径
        for (int j = 0; j < V; j++) {
            if (!visited[j] && min_cost + graph[k][j] < d[j]) {
                d[j] = min_cost + graph[k][j];
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
    Dijkstra(0);
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
