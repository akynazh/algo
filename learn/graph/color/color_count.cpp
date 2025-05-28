// 图的最多着色方法数问题

// 对 V 个顶点，E 条边的图，用小于 num_color 种颜色进行涂色，要求共边的两点颜色不相同，一共有多少种涂法？
/*
以上问题我们给定了最小的涂色数，求的是方法的个数，反过来呢？

    现在我们想要用 **最少的颜色 **达到目标，就是要求一幅图的最小着色数了：

        **思路 **：从小到大增加着色数，判断以当前数量的颜色是否可以成功涂色，如果可以一一选取其中可以涂色的颜色进行涂色，如果不可以则新增颜色涂色


*/
const int MAX = 100; // 最大顶点数
#include <iostream>
using namespace std;

int G[MAX][MAX]; // init: 0
int color[MAX];  // init: 0
int V, E, num_color, ccount = 0;
bool Judge_Color(int v, int col)
{ // 对 v 点用 col 继续涂色
    for (int i = 1; i <= V; i++)
        if (G[i][v] == 1 && color[i] == col)
            return false;
    return true;
}
void Solve(int v)
{
    if (v > V)
    { // 涂色完毕
        ccount++;
        for (int i = 1; i <= V; i++)
            cout << color[i] << " ";
        cout << endl;
        return;
    }
    for (int i = 1; i <= num_color; i++)
    {
        if (Judge_Color(v, i))
        { // 如果可以涂色则进行涂色
            color[v] = i;
            Solve(v + 1);
            color[v] = 0; // 回溯
        }
    }
}
int main()
{
    Solve(1); // 从第一个结点开始涂色
}