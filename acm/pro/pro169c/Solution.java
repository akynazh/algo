package pro.pro169c;

import java.util.*;

class UserSolution {
    int N, K;
    Map<Integer, Sample> samples;
    int[][][] grid;
    int[][][] prefix;
    Tree tree;
    int totalLoss;

    static class Sample {
        int id, x, y, c;

        Sample(int id, int x, int y, int c) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.c = c;
        }
    }

    static class Tree {
        Tree left, right;
        int ax, ay, bx, by;
        int loss;

        public Tree(Tree left, Tree right, int ax, int ay, int bx, int by, int loss) {
            this.left = left;
            this.right = right;
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.loss = loss;
        }
    }

    static class DivResult implements Comparable<DivResult> {
        int ax1, ay1, bx1, by1;
        int ax2, ay2, bx2, by2;
        int loss;

        public DivResult(int ax1, int ay1, int bx1, int by1, int ax2, int ay2, int bx2, int by2, int loss) {
            this.ax1 = ax1;
            this.ay1 = ay1;
            this.bx1 = bx1;
            this.by1 = by1;
            this.ax2 = ax2;
            this.ay2 = ay2;
            this.bx2 = bx2;
            this.by2 = by2;
            this.loss = loss;
        }

        @Override
        public int compareTo(DivResult o) {
            if (this.loss != o.loss) return this.loss - o.loss;
            if (this.bx1 != o.bx1) return this.bx1 - o.bx1;
            return this.by1 - o.by1;
        }
    }

    public void init(int N, int K) {
        this.N = N;
        this.K = K;
        this.samples = new HashMap<>();
        this.grid = new int[K + 1][N][N];
        this.prefix = new int[K + 1][N + 1][N + 1];
    }

    public void addSample(int mID, int mX, int mY, int mC) {
        samples.put(mID, new Sample(mID, mX, mY, mC));
        grid[mC][mX][mY] = 1;
    }

    public void deleteSample(int mID) {
        Sample sample = samples.remove(mID);
        grid[sample.c][sample.x][sample.y] = 0;
    }

    public int fit() {
        buildPrefixSum();
        int ax = 0, ay = 0, bx = N - 1, by = N - 1;
        totalLoss = 0;
        tree = buildTree(ax, ay, bx, by);
        return totalLoss;
    }

    public int predict(int mX, int mY) {
        Tree node = tree;
        while (node.left != null) {
            if (mX >= node.left.ax && mX <= node.left.bx && mY >= node.left.ay && mY <= node.left.by) node = node.left;
            else node = node.right;
        }
        return getCategory(node.ax, node.ay, node.bx, node.by);
    }

    Tree buildTree(int ax, int ay, int bx, int by) {
        int parentLoss = getLoss(ax, ay, bx, by);
        Tree parent = new Tree(null, null, ax, ay, bx, by, parentLoss);
        DivResult divResult = div(ax, ay, bx, by);
        if (divResult != null && divResult.loss < parentLoss) {
            parent.left = buildTree(divResult.ax1, divResult.ay1, divResult.bx1, divResult.by1);
            parent.right = buildTree(divResult.ax2, divResult.ay2, divResult.bx2, divResult.by2);
        }
        if (parent.left == null) {
            totalLoss += parentLoss;
        }
        return parent;
    }

    DivResult div(int ax, int ay, int bx, int by) {
        Queue<DivResult> result = new PriorityQueue<>();
        for (int x = ax; x <= bx; x++) {
            int loss1 = getLoss(ax, ay, x, by);
            int loss2 = getLoss(x + 1, ay, bx, by);
            if (loss1 != -1 && loss2 != -1) {
                result.offer(new DivResult(ax, ay, x, by, x + 1, ay, bx, by, loss1 + loss2));
            }
        }
        for (int y = ay; y <= by; y++) {
            int loss1 = getLoss(ax, ay, bx, y);
            int loss2 = getLoss(ax, y + 1, bx, by);
            if (loss1 != -1 && loss2 != -1) {
                result.offer(new DivResult(ax, ay, bx, y, ax, y + 1, bx, by, loss1 + loss2));
            }
        }
        return result.peek();
    }

    void buildPrefixSum() {
        for (int c = 1; c <= K; c++) {
            int[][] curGrid = grid[c];
            int[][] curPrefix = prefix[c];
            for (int x = 1; x <= N; x++) {
                for (int y = 1; y <= N; y++) {
                    curPrefix[x][y] = curGrid[x - 1][y - 1]
                            + curPrefix[x - 1][y]
                            + curPrefix[x][y - 1]
                            - curPrefix[x - 1][y - 1];
                }
            }
        }
    }

    int getCount(int category, int ax, int ay, int bx, int by) {
        int[][] curPrefix = prefix[category];
        return curPrefix[bx + 1][by + 1]
                - curPrefix[ax][by + 1]
                - curPrefix[bx + 1][ay]
                + curPrefix[ax][ay];
    }

    int getCategory(int ax, int ay, int bx, int by) {
        int category = 1, categoryCount = 0;
        for (int c = 1; c <= K; c++) {
            int count = getCount(c, ax, ay, bx, by);
            if (count > categoryCount) {
                categoryCount = count;
                category = c;
            }
        }
        return category;
    }

    int getLoss(int ax, int ay, int bx, int by) {
        int categoryCount = 0, totalCount = 0;
        for (int c = 1; c <= K; c++) {
            int count = getCount(c, ax, ay, bx, by);
            totalCount += count;
            categoryCount = Math.max(categoryCount, count);
        }
        if (totalCount == 0) {
            return -1;
        }
        return totalCount - categoryCount;
    }
}

class Solution {
    private static UserSolution usersolution = new UserSolution();

    private final static int CMD_INIT = 100;
    private final static int CMD_ADD_SAMPLE = 200;
    private final static int CMD_DELETE_SAMPLE = 300;
    private final static int CMD_FIT = 400;
    private final static int CMD_PREDICT = 500;

    private static boolean run(Scanner sc) throws Exception {
        int Q, N, K;
        int mID, mX, mY, mC;

        int ret = -1, ans;

        Q = sc.nextInt();

        boolean okay = false;

        for (int q = 0; q < Q; ++q) {
            int cmd;
            cmd = sc.nextInt();
            switch (cmd) {
                case CMD_INIT:
                    N = sc.nextInt();
                    K = sc.nextInt();
                    usersolution.init(N, K);
                    okay = true;
                    break;
                case CMD_ADD_SAMPLE:
                    mID = sc.nextInt();
                    mX = sc.nextInt();
                    mY = sc.nextInt();
                    mC = sc.nextInt();
                    usersolution.addSample(mID, mX, mY, mC);
                    break;
                case CMD_DELETE_SAMPLE:
                    mID = sc.nextInt();
                    usersolution.deleteSample(mID);
                    break;
                case CMD_FIT:
                    ret = usersolution.fit();
                    ans = sc.nextInt();
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_PREDICT:
                    mX = sc.nextInt();
                    mY = sc.nextInt();
                    ret = usersolution.predict(mX, mY);
                    ans = sc.nextInt();
                    if (ret != ans)
                        okay = false;
                    break;
                default:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception {
        System.setIn(new java.io.FileInputStream("acm/pro169c/input"));

        Scanner sc = new Scanner(System.in);

        int TC = sc.nextInt();
        int MARK = sc.nextInt();

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        sc.close();
    }
}