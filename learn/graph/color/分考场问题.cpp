// 下面是有关最小着色问题相应实际问题的解决。

// n 个人参加某项特殊考试。为了公平，要求任何两个认识的人不能分在同一个考场。求是少需要分几个考场才能满足条件。

#include <iostream>
using namespace std;
const int MAX = 100;
int G[MAX][MAX];    // init 0
int room[MAX][MAX]; // 保存教室 i 中第 j 个学生的 id, init: 0
int c_stu, c_relation, min_room = MAX + 1, count_room;
void solve(int x, int count_room)
{
    if (count_room >= min_room)
        return; // 剪枝
    if (x > c_stu)
    { // 学生分配完毕
        min_room = min(min_room, count_room);
        return;
    }
    for (int i = 1; i <= count_room; i++)
    {
        int flag = true, j = 0;
        while (room[i][j])
        { // 查找学生教室中是否有认识的人
            if (G[x][room[i][j]])
            {
                flag = false;
                break;
            }
            j++;
        }
        if (flag)
        { // 教室里没有认识的人
            room[i][j] = x;
            solve(x + 1, count_room); // 继续用现有教室分配下一个学生
            room[i][j] = 0;
        }
    }
    // 教室不够 新增一个教室并把学生放进教室
    room[count_room + 1][0] = x;
    solve(x + 1, count_room + 1);
    room[count_room + 1][0] = 0;
}

int main()
{
    solve(1, 1); // 最开始用一个教室分配学生
}
