package pro0709e;

import java.util.*;

public class UserSolution {
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
