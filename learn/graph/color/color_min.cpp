
// 最小着色数问题

#include <iostream>
using namespace std;
const int MAX = 100; // 最大顶点数

int G[MAX][MAX]; // init: 0
int color[MAX];  // init: 0
int V, E, c_min = MAX + 1;
bool Judge_Color(int v, int col)
{ // 判断v是否可以用col涂色
    for (int i = 1; i <= V; i++)
        if (G[i][v] == 1 && color[i] == col)
            return false;
    return true;
}
/*
def judge(v, c):
    for i in range(1, V + 1):
        if G[i][v] == 1 and color[i] = c:
            return False
    return True
*/
/*
def solve(v, num_color):
    if num_color >= c_min:
        return
    if v > V:
        c_min = min(c_min, num_color)
        return
    for i in range(1, num_color + 1):
        if judge_color(v, i):
            color[v] = i
            solve(v + 1, num_color)
            color[v] = 0
    color[v] = num_color + 1
    solve(v + 1, num_color + 1)
    color[v] = 0
*/
void Solve(int v, int num_color)
{
    if (num_color >= c_min)
        return; // 剪枝
    if (v > V)
    { // 涂色完毕
        c_min = min(c_min, num_color);
        return;
    }
    for (int i = 1; i <= num_color; i++)
    {
        if (Judge_Color(v, i))
        { // 如果可以用这种颜色涂色
            color[v] = i;
            Solve(v + 1, num_color);
            color[v] = 0; // 回溯
        }
    }
    // num_color 种颜色不够用了
    color[v] = num_color + 1; // 新增一种颜色
    Solve(v + 1, num_color + 1);
    color[v] = 0; // 回溯
}
int main()
{
    Solve(1, 1);
}