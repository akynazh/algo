package pro0802e;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    int N;
    int[] growthTime;
    boolean[][] farm;
    final int boxSize = 50;
    Box[][] boxes;

    static class Box {
        HashSet<Crop> crops = new HashSet<>();
        int water;
    }

    static class Crop {
        int x, y, c, time, curBoxWater, water;
        boolean deleted;

        public Crop(int x, int y, int c, int time, int curBoxWater) {
            this.x = x;
            this.y = y;
            this.c = c;
            this.time = time;
            this.curBoxWater = curBoxWater;
        }
    }

    void init(int N, int mGrowthTime[]) {
        this.N = N;
        this.growthTime = mGrowthTime;
        this.farm = new boolean[N][N];
        int m = N / boxSize + 1;
        this.boxes = new Box[m][m];
        for (int i = 0; i < m; i++) {
//            !! Arrays.fill 会让所有元素引用同一个对象
//            Arrays.fill(this.boxes[i], new Box());
            for (int j = 0; j < m; j++) {
                this.boxes[i][j] = new Box();
            }
        }
    }

    int sow(int mTime, int mRow, int mCol, int mCategory) {
        if (farm[mRow][mCol]) return 0;
        farm[mRow][mCol] = true;
        Box box = boxes[mRow / boxSize][mCol / boxSize];
        Crop crop = new Crop(mRow, mCol, mCategory, mTime, box.water);
        box.crops.add(crop);
        return 1;
    }

    int water(int mTime, int G, int mRow, int mCol, int mHeight, int mWidth) {
        int x = mRow, y = mCol, x1 = mRow + mHeight - 1, y1 = mCol + mWidth - 1;
        int bx = x / boxSize, by = y / boxSize, bx1 = x1 / boxSize, by1 = y1 / boxSize;
        int res = 0;
        for (int i = bx; i <= bx1; i++) {
            for (int j = by; j <= by1; j++) {
                Box box = boxes[i][j];
                if (x <= i * boxSize && i * boxSize + boxSize - 1 <= x1
                        && y <= j * boxSize && j * boxSize + boxSize - 1 <= y1) {
                    box.water += G;
                    res += box.crops.size();
                } else {
                    for (Crop crop : box.crops) {
                        if (x <= crop.x && crop.x <= x1 && y <= crop.y && crop.y <= y1) {
                            crop.water += G;
                            res++;
                        }
                    }
                }
            }
        }
        return res;
    }

    int harvest(int mTime, int L, int mRow, int mCol, int mHeight, int mWidth) {
        int x = mRow, y = mCol, x1 = mRow + mHeight - 1, y1 = mCol + mWidth - 1;
        int bx = x / boxSize, by = y / boxSize, bx1 = x1 / boxSize, by1 = y1 / boxSize;
        Set<Crop> crops = new HashSet<>();
        for (int i = bx; i <= bx1; i++) {
            for (int j = by; j <= by1; j++) {
                for (Crop crop : boxes[i][j].crops) {
                    if (x <= crop.x && crop.x <= x1 && y <= crop.y && crop.y <= y1) {
                        if (!cropDone(mTime, L, crop, boxes[i][j])) return 0;
                        crops.add(crop);
                    }
                }
            }
        }
        for (Crop crop : crops) {
            farm[crop.x][crop.y] = false;
            crop.deleted = true;
        }
        for (int i = bx; i <= bx1; i++) {
            for (int j = by; j <= by1; j++) {
                if (x <= i * boxSize && i * boxSize + boxSize - 1 <= x1
                        && y <= j * boxSize && j * boxSize + boxSize - 1 <= y1) {
                    boxes[i][j].crops.clear();
                } else {
                    boxes[i][j].crops.removeIf(crop -> crop.deleted);
                }
            }
        }
        return crops.size();
    }

    boolean cropDone(int curTime, int L, Crop crop, Box box) {
        int t = growthTime[crop.c];
        int time = curTime - crop.time + (crop.water + box.water - crop.curBoxWater) * t;
        return time / t >= L;
    }
}


class Solution {
    private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private final static int CATEGORY_NUM = 3;

    private final static int CMD_INIT = 100;
    private final static int CMD_SOW = 200;
    private final static int CMD_WATER = 300;
    private final static int CMD_HARVEST = 400;

    private static boolean run() throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int Q = Integer.parseInt(st.nextToken());
        int N, mTime, mCategory, L, G, mRow, mCol, mHeight, mWidth, ans, ret;
        int[] mGrowthTime = new int[CATEGORY_NUM];

        boolean okay = false;
        for (int q = 0; q < Q; ++q) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                N = Integer.parseInt(st.nextToken());
                for (int i = 0; i < 3; ++i)
                    mGrowthTime[i] = Integer.parseInt(st.nextToken());

                userSolution.init(N, mGrowthTime);
                okay = true;
            } else if (cmd == CMD_SOW) {
                mTime = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mCategory = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                ret = userSolution.sow(mTime, mRow, mCol, mCategory);
                if (ans != ret)
                    okay = false;
            } else if (cmd == CMD_WATER) {
                mTime = Integer.parseInt(st.nextToken());
                G = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mHeight = Integer.parseInt(st.nextToken());
                mWidth = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                ret = userSolution.water(mTime, G, mRow, mCol, mHeight, mWidth);
                if (ans != ret)
                    okay = false;

            } else if (cmd == CMD_HARVEST) {
                mTime = Integer.parseInt(st.nextToken());
                L = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mHeight = Integer.parseInt(st.nextToken());
                mWidth = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                ret = userSolution.harvest(mTime, L, mRow, mCol, mHeight, mWidth);
                if (ans != ret)
                    okay = false;
            }
        }
        return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

        System.setIn(new java.io.FileInputStream("acm/pro0802e/sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        T = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        long s = System.currentTimeMillis();
        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        System.out.println(System.currentTimeMillis() - s);

        br.close();
    }
}
