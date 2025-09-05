#include <algorithm>
#include <unordered_map>
#include <vector>

using namespace std;

#define MAP_SIZE    10000
#define BSIZE        25

struct PIXEL {
    int row, col;
    int depth;
};

struct {
    int size;
    int bidx;
    int max;
    vector<PIXEL> data[4][30][30];
} Print[40];
int pcnt;

struct {
    int vkey;
    int pid, row, col, dir;
} Used[5000];
int ucnt;

struct {
    vector <int> uids;
} Map[4][MAP_SIZE / BSIZE + 1][MAP_SIZE / BSIZE + 1];


unordered_map<int, int> pIDs;

int bsize[4] = { 25, 100, 300, 700 };
int mapMax[4];
int visitKey;


void init(int N)
{
    pcnt = 0;
    ucnt = 0;
    pIDs.clear();


    for (int t = 0; t < 4; t++) {
        mapMax[t] = MAP_SIZE / bsize[t] + 1;
        for (int i = 0; i < mapMax[t]; i++) {
            for (int k = 0; k < mapMax[t]; k++) {
                Map[t][i][k].uids.clear();
            }
        }
    }
}


void addPrint(int mID, int mSize, int mCnt, int mPixel[][3])
{
    int pid = pcnt++;
    pIDs[mID] = pid;


    Print[pid].size = mSize;
    Print[pid].max = mSize / BSIZE + 1;
    Print[pid].bidx = 0;
    for (int i = 0; i < 4; i++)
        if (bsize[i] == mSize) Print[pid].bidx = i;


    for (int d = 0; d < 4; d++) {
        for (int i = 0; i < Print[pid].max; i++) {
            for (int k = 0; k < Print[pid].max; k++) {
                Print[pid].data[d][i][k].clear();
            }
        }
    }
    for (int i = 0; i < mCnt; i++) {
        Print[pid].data[0][mPixel[i][0] / BSIZE][mPixel[i][1] / BSIZE].push_back({ mPixel[i][0], mPixel[i][1], mPixel[i][2] });
        Print[pid].data[1][(mSize - 1 - mPixel[i][1]) / BSIZE][mPixel[i][0] / BSIZE].push_back({ mSize - 1 - mPixel[i][1], mPixel[i][0], mPixel[i][2] });
        Print[pid].data[2][(mSize - 1 - mPixel[i][0]) / BSIZE][(mSize - 1 - mPixel[i][1]) / BSIZE].push_back({ mSize - 1 - mPixel[i][0], mSize - 1 - mPixel[i][1], mPixel[i][2] });
        Print[pid].data[3][mPixel[i][1] / BSIZE][(mSize - 1 - mPixel[i][0]) / BSIZE].push_back({ mPixel[i][1], mSize - 1 - mPixel[i][0], mPixel[i][2] });
    }
}


void pressPrint(int mID, int mRow, int mCol, int mDir)
{
    int pid = pIDs[mID];


    int uid = ucnt++;
    Used[uid].pid = pid;
    Used[uid].row = mRow;
    Used[uid].col = mCol;
    Used[uid].dir = mDir;


    int size = Print[pid].size;
    int sidx = Print[pid].bidx;


    int srow = mRow / bsize[sidx];
    int erow = min((mRow + size - 1) / bsize[sidx], mapMax[sidx] - 1);


    int scol = mCol / bsize[sidx];
    int ecol = min((mCol + size - 1) / bsize[sidx], mapMax[sidx] - 1);


    for (int i = srow; i <= erow; i++) {
        for (int k = scol; k <= ecol; k++) {
            Map[sidx][i][k].uids.push_back(uid);
        }
    }
}


int calc_depth(int mRow, int mCol, int uid)
{
    int ret = 0;
    int pid = Used[uid].pid;
    int row = Used[uid].row;
    int col = Used[uid].col;
    int dir = Used[uid].dir;


    int size = Print[pid].size;
    int max = Print[pid].max;


    if (mRow + BSIZE <= row || row + size <= mRow) return 0;
    if (mCol + BSIZE <= col || col + size <= mCol) return 0;


    int srow, erow, scol, ecol;
    if (mRow <= row) {
        srow = erow = 0;
    }
    else {
        srow = (mRow - row) / BSIZE;
        erow = min((mRow - row + BSIZE - 1) / BSIZE, max - 1);
    }


    if (mCol <= col) {
        scol = ecol = 0;
    }
    else {
        scol = (mCol - col) / BSIZE;
        ecol = min((mCol - col + BSIZE - 1) / BSIZE, max - 1);
    }


    for (int i = srow; i <= erow; i++) {
        for (int k = scol; k <= ecol; k++) {
            for (PIXEL p : Print[pid].data[dir][i][k]) {
                if (mRow <= row + p.row && row + p.row < mRow + BSIZE) {
                    if (mCol <= col + p.col && col + p.col < mCol + BSIZE) {
                        ret += p.depth;
                    }
                }
            }
        }
    }


    return ret;
}


int getDepth(int mRow, int mCol)
{
    int ret = 0;


    for (int t = 0; t < 4; t++) {
        int srow = mRow / bsize[t];
        int erow = min((mRow + BSIZE - 1) / bsize[t], mapMax[t] - 1);


        int scol = mCol / bsize[t];
        int ecol = min((mCol + BSIZE - 1) / bsize[t], mapMax[t] - 1);


        visitKey++;
        for (int i = srow; i <= erow; i++) {
            for (int k = scol; k <= ecol; k++) {
                for (int uid : Map[t][i][k].uids) {
                    if (Used[uid].vkey != visitKey) {
                        Used[uid].vkey = visitKey;


                        ret += calc_depth(mRow, mCol, uid);
                    }
                }
            }
        }
    }


    return ret;
}