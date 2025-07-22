package pro1580;

import java.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    private static UserSolution usersolution = new UserSolution();

    private final static int CMD_INIT = 0;
    private final static int CMD_ROAD = 1;
    private final static int CMD_BIKE = 2;
    private final static int CMD_MONEY = 3;

    private static int spotA[] = new int[30];
    private static int spotB[] = new int[30];
    private static int dis[] = new int[30];

    private static boolean run(BufferedReader br) throws Exception {
        int N, K;
        int maxTime;
        int ret, ans, cmd;

        boolean ok = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int Q = Integer.parseInt(st.nextToken());

        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                N = Integer.parseInt(st.nextToken());
                usersolution.init(N);
                ok = true;
            } else if (cmd == CMD_ROAD) {
                K = Integer.parseInt(st.nextToken());
                for (int i = 0; i < K; i++) {
                    st = new StringTokenizer(br.readLine(), " ");
                    spotA[i] = Integer.parseInt(st.nextToken());
                    spotB[i] = Integer.parseInt(st.nextToken());
                    dis[i] = Integer.parseInt(st.nextToken());
                }
                usersolution.addRoad(K, spotA, spotB, dis);
            } else if (cmd == CMD_BIKE) {
                spotA[0] = Integer.parseInt(st.nextToken());
                usersolution.addBikeRent(spotA[0]);
            } else if (cmd == CMD_MONEY) {
                spotA[0] = Integer.parseInt(st.nextToken());
                spotB[0] = Integer.parseInt(st.nextToken());
                maxTime = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());

                ret = usersolution.getMinMoney(spotA[0], spotB[0], maxTime);
                if (ret != ans) {
                    ok = false;
                }
            } else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("acm/samples/pro1580/sample25.in"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line = new StringTokenizer(br.readLine(), " ");

        int T = Integer.parseInt(line.nextToken());
        int MARK = Integer.parseInt(line.nextToken());

        long s = System.currentTimeMillis();
        System.out.println(s);
        for (int tc = 1; tc <= T; tc++) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        System.out.println(System.currentTimeMillis() - s);

        br.close();
    }
}

class UserSolution {
    final int MAX_N = 101;
    int[][] graph;
    boolean[] hasShop;
    int N;

    void init(int N) {
        graph = new int[N + 1][N + 1];
        hasShop = new boolean[N + 1];
        this.N = N;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                graph[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    void addRoad(int K, int mSpotA[], int mSpotB[], int mDis[]) {
        for (int i = 0; i < K; i++) {
            graph[mSpotA[i]][mSpotB[i]] = graph[mSpotB[i]][mSpotA[i]] = mDis[i];
        }
    }

    void addBikeRent(int mSpot) {
        hasShop[mSpot] = true;
    }

    static class State implements Comparable<State> {
        int node;
        boolean hasBike;
        boolean hasTaxi;
        int cost;
        int time;

        State(int node, boolean hasBike, boolean hasTaxi, int cost, int time) {
            this.node = node;
            this.hasBike = hasBike;
            this.hasTaxi = hasTaxi;
            this.cost = cost;
            this.time = time;
        }

        @Override
        public int compareTo(State other) {
            return this.cost - other.cost;
        }
    }

    String getKey(int node, boolean hasBike, boolean hasTaxi) {
        return String.valueOf(node) + hasBike + hasTaxi;
    }

    int getMinMoney(int mStartSpot, int mEndSpot, int mMaxTime) {
        int ans = Integer.MAX_VALUE;
        Map<String, Integer> minCostMap = new HashMap<>();
        Map<String, Integer> minTimeMap = new HashMap<>();
        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.offer(new State(mStartSpot, false, false, 0, 0));

        while (!pq.isEmpty()) {
            State curr = pq.poll();
            String key = getKey(curr.node, curr.hasBike, curr.hasTaxi);

            // cut
            if (minCostMap.containsKey(key) && (minCostMap.get(key) <= curr.cost && minTimeMap.get(key) <= curr.time)) {
                continue;
            }

//            if ((!minCostMap.containsKey(key)) || (minCostMap.get(key) > curr.cost && minTimeMap.get(key) > curr.time)) {
                minCostMap.put(key, curr.cost);
                minTimeMap.put(key, curr.time);
//            }

            // end
            if (curr.node == mEndSpot && curr.time <= mMaxTime && (!curr.hasBike || hasShop[curr.node])) {
                ans = Math.min(ans, curr.cost);
                continue;
            }

            // go
            for (int i = 1; i <= N; i++) {
                if (graph[curr.node][i] == Integer.MAX_VALUE || i == curr.node) {
                    continue;
                }
                int d = graph[curr.node][i];

                // 1. walk
                if ((!curr.hasBike) || (hasShop[curr.node])) {
                    int walkTime = curr.time + d * 17;
                    if (walkTime <= mMaxTime) {
                        int walkCost = curr.cost;
                        pq.offer(new State(i, false, false, walkCost, walkTime));
                    }
                }

                // 2. bike
                if (curr.hasBike || hasShop[curr.node]) {
                    int bikeTime = curr.time + d * 4;
                    if (bikeTime <= mMaxTime) {
                        int bikeCost = curr.cost + d * 4;
                        pq.offer(new State(i, true, false, bikeCost, bikeTime));
                    }
                }

                // 3. taxi
                if ((!curr.hasBike) || (hasShop[curr.node])) {
                    int taxiTime;
                    if (curr.hasTaxi) {
                        taxiTime = curr.time + d;
                    } else {
                        taxiTime = curr.time + d + 7;
                    }
                    if (taxiTime <= mMaxTime) {
                        int taxiCost = curr.cost + d * 19;
                        pq.offer(new State(i, false, true, taxiCost, taxiTime));
                    }
                }
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
