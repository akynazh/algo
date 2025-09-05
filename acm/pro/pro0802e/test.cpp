#include<vector>
#define SCALE 50
#define MAXN 1000
using namespace std;

int minn(int x, int y) {
    return x < y ? x: y;
}

struct Plant{
    int time;
    int type;
    int r,c;
    bool isDel;
}plants[100005];

struct map_scale{
    int w;
    vector<Plant*> list_plant;
}Mapp[MAXN/SCALE+1][MAXN/SCALE+1];

int ts, id, n, grow_time[3];
int ground[MAXN][MAXN];

void init(int N, int mGrowthTime[3]) {
    n = N;
    id = 0;
    ++ts;
    for (int i = 0; i <3; i++)
    {
        grow_time[i] = mGrowthTime[i];
    }
    for (int i = 0; i <= n/SCALE; i++)
    {
        for (int j = 0; j <= n/SCALE; j++)
        {
            Mapp[i][j].w = 0;
            Mapp[i][j].list_plant.clear();
        }
    }
}

int sow(int mTime, int mRow, int mCol, int mCategory) {
    if(ground[mRow][mCol] == ts) return 0;
    int R = mRow/SCALE, C = mCol/SCALE;
    plants[++id] = {mTime + Mapp[R][C].w*grow_time[mCategory], mCategory,mRow,mCol};
    Mapp[R][C].list_plant.push_back(&plants[id]);
    ground[mRow][mCol] = ts;
    return 1;
}

int water(int mTime, int G, int mRow, int mCol, int mHeight, int mWidth) {
    int ret = 0;
    int sx = mRow, sy = mCol, ex = mRow+mHeight-1, ey = mCol + mWidth -1;
    for (int i = sx/SCALE; i <= ex/SCALE; i++)
    {
        for (int j = sy/SCALE; j <= ey/SCALE; j++)
        {
            int x1 =i*SCALE, y1 = j*SCALE, x2 = x1 + SCALE-1,y2 = y1+SCALE-1;
            if(sx <= x1 && sy <= y1 && x2 <= ex && y2 <= ey) {
                Mapp[i][j].w += G;
                ret += Mapp[i][j].list_plant.size();
            }
            else {
                for (auto p: Mapp[i][j].list_plant)
                {
                    if(sx <= p->r && p->r <= ex && sy <= p->c && ey >= p->c) {
                        p->time -= G*grow_time[p->type];
                        ret++;
                    }
                }
            }
        }
    }
    return ret;
}

int harvest(int mTime, int L, int mRow, int mCol, int mHeight, int mWidth) {
    vector<Plant *> delv;
    int sx = mRow, sy = mCol, ex = mRow + mHeight - 1, ey = mCol + mWidth - 1;
    for (int i = sx / SCALE; i <= ex / SCALE; i++) {
        for (int j = sy / SCALE; j <= ey / SCALE; j++) {
            for (auto k : Mapp[i][j].list_plant) {
                if (sx <= k->r && k->r <= ex && sy <= k->c && k->c <= ey) {
                    int sz = ((mTime - k->time) + grow_time[k->type] * Mapp[i][j].w) / grow_time[k->type];
                    if (sz < L) return 0;
                    delv.push_back(k);
                }
            }
        }
    }
    for (auto i : delv) {
        i->isDel = 1;
        ground[i->r][i->c] = 0;
    }

    for (int i = sx / SCALE; i <= ex / SCALE; i++) {
        for (int j = sy / SCALE; j <= ey / SCALE; j++) {
            int x1 = i * SCALE, y1 = j * SCALE, x2 = x1 + SCALE - 1, y2 = y1 + SCALE - 1;
            if (sx <= x1 && sy <= y1 && x2 <= ex && y2 <= ey) {
                Mapp[i][j].list_plant.clear();
            }
            else {
                vector<Plant *> newv;
                for (auto k : Mapp[i][j].list_plant) {
                    if (!k->isDel) newv.push_back(k);
                }
                Mapp[i][j].list_plant = newv;
            }
        }
    }
    return delv.size();
}