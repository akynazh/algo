package pro0711e;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

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
        for (int q = 0; q < Q; ++q) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                N = Integer.parseInt(st.nextToken());
                M = Integer.parseInt(st.nextToken());
                K = Integer.parseInt(st.nextToken());

                for (int i = 0; i < K; ++i) {
                    st = new StringTokenizer(br.readLine(), " ");
                    mIDArr[i] = Integer.parseInt(st.nextToken());
                    aStorageArr[i] = Integer.parseInt(st.nextToken());
                    bStorageArr[i] = Integer.parseInt(st.nextToken());
                    mAttrArr[i] = st.nextToken().toCharArray();
                }
                userSolution.init(N, M, K, mIDArr, aStorageArr, bStorageArr, mAttrArr);
                okay = true;
            } else if (cmd == CMD_ADD) {
                mID = Integer.parseInt(st.nextToken());
                aStorage = Integer.parseInt(st.nextToken());
                bStorage = Integer.parseInt(st.nextToken());
                mAttr = st.nextToken().toCharArray();
                userSolution.add(mID, aStorage, bStorage, mAttr);
            } else if (cmd == CMD_REMOVE) {
                mID = Integer.parseInt(st.nextToken());
                userSolution.remove(mID);

            } else if (cmd == CMD_TRANSPORT) {
                sStorage = Integer.parseInt(st.nextToken());
                eStorage = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                mAttr = st.nextToken().toCharArray();
                ret = userSolution.transport(sStorage, eStorage, mAttr);
                if (ans != ret) {
                    okay = false;
                }
            }
        }
        return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

        System.setIn(new java.io.FileInputStream("acm/samples/pro0711e/sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        T = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        long s = System.currentTimeMillis();
        for (int tc = 1; tc <= T; tc++) {
            long a = System.currentTimeMillis();
            int score = run() ? MARK : 0;
            long b = System.currentTimeMillis();
            System.out.println("#" + tc + " " + score + " " + (b - a));
        }
        System.out.println(System.currentTimeMillis() - s);

        br.close();
    }
}


class UserSolution2 {
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

class UserSolution1 {
    int N;
    int M;
    int K;
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

    static class State implements Comparable<State> {
        int unit;
        char[] attr;
        boolean converted;
        int cost;

        State(int unit, char[] attr, boolean converted, int cost) {
            this.unit = unit;
            this.attr = attr;
            this.converted = converted;
            this.cost = cost;
        }

        String getKey() {
            return unit + String.valueOf(attr) + converted;
        }

        @Override
        public int compareTo(State o) {
            return this.cost - o.cost;
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
        Queue<State> queue = new PriorityQueue<>();
        queue.offer(new State(sStorage, mAttr, false, 0));
        int cost = Integer.MAX_VALUE;
        Map<String, Integer> minCostMap = new HashMap<>();
        while (!queue.isEmpty()) {
            State s = queue.poll();
            if (s.unit == eStorage) {
                cost = Math.min(s.cost, cost);
                continue;
            }
            if (s.cost >= cost) {
                continue;
            }
            if (minCostMap.containsKey(s.getKey()) && minCostMap.get(s.getKey()) <= s.cost) {
                continue;
            }
            minCostMap.put(s.getKey(), s.cost);

            for (int i = 1; i <= N; i++) {
                int pId = units[s.unit][i];
                if (pId != 0) {
                    queue.offer(new State(i, s.attr, s.converted, s.cost + getCost(pipelines.get(pId).attr, s.attr)));
                    if (!s.converted) {
                        queue.offer(new State(i, pipelines.get(pId).attr, true, s.cost + 1));
                    }
                }
            }
        }
        return cost == Integer.MAX_VALUE ? -1 : cost;
    }
}

class UserSolution {
    int N;
    int M;
    int K;
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

    static class State implements Comparable<State> {
        int unit;
        char[] attr;
        boolean converted;
        int cost;

        State(int unit, char[] attr, boolean converted, int cost) {
            this.unit = unit;
            this.attr = attr;
            this.converted = converted;
            this.cost = cost;
        }

        @Override
        public int compareTo(State o) {
            return this.cost - o.cost;
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

    String getKey(int unit, boolean converted) {
        return String.valueOf(unit) + converted;
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
        Queue<State> queue = new PriorityQueue<>();
        queue.offer(new State(sStorage, mAttr, false, 0));
        Map<String, Integer> minCostMap = new HashMap<>();
        while (!queue.isEmpty()) {
            State s = queue.poll();
            if (s.unit == eStorage) {
                return s.cost;
            }
            String key = getKey(s.unit, false);
            // 只和没 convert 过的进行比较
            if (minCostMap.containsKey(key) && minCostMap.get(key) <= s.cost) {
                continue;
            }
            minCostMap.put(getKey(s.unit, s.converted), s.cost);

            for (int i = 1; i <= N; i++) {
                int pId = units[s.unit][i];
                if (pId != 0) {
                    queue.offer(new State(i, s.attr, s.converted, s.cost + getCost(pipelines.get(pId).attr, s.attr)));
                    if (!s.converted) {
                        queue.offer(new State(i, pipelines.get(pId).attr, true, s.cost + 1));
                    }
                }
            }
        }
        return -1;
    }
}