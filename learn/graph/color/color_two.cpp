// 二分图问题

// 能否只用 2 种颜色对一个图上色，并且使得共边顶点不同色？

#include <cstdio>
#include <vector>
using namespace std;

const int MAX_V = 1000; // 最大顶点数

vector<int> G[MAX_V];
int vertexes;
int edges;
int color[MAX_V]; // 二分图，颜色为1或-1
bool dfs(int v, int c)
{
    color[v] = c;
    for (int i = 0; i < G[v].size(); i++)
    {
        if (color[G[v][i]] == c)
            return false;
        if (color[G[v][i]] == 0 && !dfs(G[v][i], -c))
            return false;
    }
    return true;
}
void solve()
{
    for (int i = 0; i < vertexes; i++)
    {
        if (color[i] == 0)
        {
            if (!dfs(i, 1))
            {
                printf("No\n");
                return;
            }
        }
    }
    printf("Yes\n");
}