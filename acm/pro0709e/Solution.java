package pro0709e;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    int N, M, K;
    Map<Integer, Pipeline> pipelines;
    int[][] units;

    static class Pipeline {
        int id, a, b;
        char[] attr;

        Pipeline(int id, int a, int b, char[] attr) {
            this.id = id;
            this.a = a;
            this.b = b;
            this.attr = attr;
        }
    }

    static class State {
        int unit;
        int cost;

        // 比较特殊的迪杰斯特拉
        // attr 和 converted 只是中间状态，不用保存在 cost 上面去比较
        char[] attr;
        int converted;

        State(int unit, char[] attr, int converted, int cost) {
            this.unit = unit;
            this.attr = attr;
            this.converted = converted;
            this.cost = cost;
        }
    }

    int getCost(char[] attr1, char[] attr2) {
        int cost = 0;
        for (int i = 0; i < M; i++) {
            if ((attr1[i] == 'D' || attr2[i] == 'D') || (attr1[i] != attr2[i])) {
                cost += 1;
            }
        }
        return cost;
    }

    void init(int N, int M, int K, int mID[], int aStorage[], int bStorage[], char mAttr[][]) {
        this.N = N;
        this.M = M;
        this.K = K;
        this.pipelines = new HashMap<>();
        this.units = new int[N + 1][N + 1];
        for (int i = 0; i < K; i++) {
            Pipeline p = new Pipeline(mID[i], aStorage[i], bStorage[i], mAttr[i]);
            pipelines.put(mID[i], p);
            units[aStorage[i]][bStorage[i]] = units[bStorage[i]][aStorage[i]] = mID[i];
        }
    }

    void add(int mID, int aStorage, int bStorage, char mAttr[]) {
        pipelines.put(mID, new Pipeline(mID, aStorage, bStorage, mAttr));
        units[aStorage][bStorage] = units[bStorage][aStorage] = mID;
    }

    void remove(int mID) {
        Pipeline p = pipelines.remove(mID);
        units[p.a][p.b] = units[p.b][p.a] = 0;
    }

    int transport(int sStorage, int eStorage, char mAttr[]) {
        Queue<State> queue = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        queue.offer(new State(sStorage, mAttr, 0, 0));
        int[] cost = new int[N + 1];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[sStorage] = 0;

        while (!queue.isEmpty()) {
            State s = queue.poll();
            if (s.unit == eStorage) {
                return s.cost;
            }
            for (int i = 1; i <= N; i++) {
                int pId = units[s.unit][i];
                if (pId == 0) {
                    continue;
                }
                Pipeline p = pipelines.get(pId);

                // 和 cost[i][1] 比会漏一些 state，由于转换只能有一次，后面进行转换可能得到更小的结果
                int nextCost = s.cost + getCost(p.attr, s.attr);
                if (nextCost < cost[i]) {
                    queue.offer(new State(i, s.attr, s.converted, nextCost));
                    if (s.converted == 0) {
                        cost[i] = nextCost;
                    }
                }

                nextCost = s.cost + 1;
                if (s.converted == 0 && nextCost < cost[i]) {
                    queue.offer(new State(i, p.attr, 1, nextCost));
                }
            }
        }
        return -1;
    }
}


class UserSolution1 {
    int N, M, K;
    Map<Integer, Pipeline> pipelines;
    int[][] units;

    static class Pipeline {
        int id, a, b;
        char[] attr;

        Pipeline(int id, int a, int b, char[] attr) {
            this.id = id;
            this.a = a;
            this.b = b;
            this.attr = attr;
        }
    }

    static class State {
        int unit;
        char[] attr;
        int converted;
        int cost;

        State(int unit, char[] attr, int converted, int cost) {
            this.unit = unit;
            this.attr = attr;
            this.converted = converted;
            this.cost = cost;
        }
    }

    int getCost(char[] attr1, char[] attr2) {
        int cost = 0;
        for (int i = 0; i < M; i++) {
            if ((attr1[i] == 'D' || attr2[i] == 'D') || (attr1[i] != attr2[i])) {
                cost += 1;
            }
        }
        return cost;
    }

    void init(int N, int M, int K, int mID[], int aStorage[], int bStorage[], char mAttr[][]) {
        this.N = N;
        this.M = M;
        this.K = K;
        this.pipelines = new HashMap<>();
        this.units = new int[N + 1][N + 1];
        for (int i = 0; i < K; i++) {
            Pipeline p = new Pipeline(mID[i], aStorage[i], bStorage[i], mAttr[i]);
            pipelines.put(mID[i], p);
            units[aStorage[i]][bStorage[i]] = units[bStorage[i]][aStorage[i]] = mID[i];
        }
    }

    void add(int mID, int aStorage, int bStorage, char mAttr[]) {
        pipelines.put(mID, new Pipeline(mID, aStorage, bStorage, mAttr));
        units[aStorage][bStorage] = units[bStorage][aStorage] = mID;
    }

    void remove(int mID) {
        Pipeline p = pipelines.remove(mID);
        units[p.a][p.b] = units[p.b][p.a] = 0;
    }

    int transport(int sStorage, int eStorage, char mAttr[]) {
        Queue<State> queue = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        queue.offer(new State(sStorage, mAttr, 0, 0));
        int[][] cost = new int[N + 1][2];
        for (int[] c : cost) {
            c[0] = c[1] = Integer.MAX_VALUE;
        }
        cost[sStorage][0] = cost[sStorage][1] = 0;

        while (!queue.isEmpty()) {
            State s = queue.poll();
            if (s.unit == eStorage) {
                return s.cost;
            }
            for (int i = 1; i <= N; i++) {
                int pId = units[s.unit][i];
                if (pId == 0) {
                    continue;
                }
                Pipeline p = pipelines.get(pId);

                // 和 cost[i][1] 比会漏一些 state，由于转换只能有一次，后面进行转换可能得到更小的结果
                int nextCost = s.cost + getCost(p.attr, s.attr);
                if (nextCost < cost[i][0]) {
                    queue.offer(new State(i, s.attr, s.converted, nextCost));
                    cost[i][s.converted] = nextCost;
                }

                nextCost = s.cost + 1;
                if (s.converted == 0 && nextCost < cost[i][0]) {
                    queue.offer(new State(i, p.attr, 1, nextCost));
                    cost[i][1] = nextCost;
                }
            }
        }
        return -1;
    }
}

public class Solution {
    private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private final static int MAX_K = 1000;

    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_TRANSPORT = 400;

    private static boolean run() throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int Q = Integer.parseInt(st.nextToken());
        int N, M, K, mID, aStorage, bStorage, sStorage, eStorage, ans, ret;
        int[] mIDArr = new int[MAX_K];
        int[] aStorageArr = new int[MAX_K];
        int[] bStorageArr = new int[MAX_K];
        char[] mAttr;
        char[][] mAttrArr = new char[MAX_K][];

        boolean okay = false;
        for (int q = 0; q < Q; ++q)
        {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT)
            {
                N = Integer.parseInt(st.nextToken());
                M = Integer.parseInt(st.nextToken());
                K = Integer.parseInt(st.nextToken());

                for(int i = 0; i < K; ++i)
                {
                    st = new StringTokenizer(br.readLine(), " ");
                    mIDArr[i] = Integer.parseInt(st.nextToken());
                    aStorageArr[i] = Integer.parseInt(st.nextToken());
                    bStorageArr[i] = Integer.parseInt(st.nextToken());
                    mAttrArr[i] = st.nextToken().toCharArray();
                }
                userSolution.init(N, M, K, mIDArr, aStorageArr, bStorageArr, mAttrArr);
                okay = true;
            }
            else if (cmd == CMD_ADD)
            {
                mID = Integer.parseInt(st.nextToken());
                aStorage = Integer.parseInt(st.nextToken());
                bStorage = Integer.parseInt(st.nextToken());
                mAttr = st.nextToken().toCharArray();
                userSolution.add(mID, aStorage, bStorage, mAttr);
            }
            else if (cmd == CMD_REMOVE)
            {
                mID = Integer.parseInt(st.nextToken());
                userSolution.remove(mID);

            }
            else if(cmd == CMD_TRANSPORT)
            {
                sStorage = Integer.parseInt(st.nextToken());
                eStorage = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                mAttr = st.nextToken().toCharArray();
                ret = userSolution.transport(sStorage, eStorage, mAttr);
                if(ans != ret)
                {
                    okay = false;
                }
            }
        }
        return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

         System.setIn(new java.io.FileInputStream("acm/pro0709e/sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        T = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}
