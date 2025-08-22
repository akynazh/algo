package pro2522t;

import java.util.*;

class UserSolution {
    int N, K;
    int[][] conn;
    HashSet<Integer>[] cities;
    Map<Integer, int[]> roads;

    public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
        this.N = N;
        this.K = K;
        conn = new int[N][N];
        cities = new HashSet[N];
        for (int i = 0; i < N; i++) {
            cities[i] = new HashSet<>();
        }
        roads = new HashMap<>();

        for (int i = 0; i < K; i++) {
            add(mId[i], sCity[i], eCity[i], mTime[i]);
        }
    }

    public void add(int mId, int sCity, int eCity, int mTime) {
        conn[sCity][eCity] = mId;
        cities[sCity].add(eCity);
        roads.put(mId, new int[]{sCity, eCity, mTime});
    }

    public void remove(int mId) {
        int[] road = this.roads.remove(mId);
        conn[road[0]][road[1]] = 0;
        cities[road[0]].remove(road[1]);
    }

    public int calculate(int sCity, int eCity) {
        int[] dis = new int[N];
        int[] prev = new int[N];
        Arrays.fill(prev, -1);
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[sCity] = 0;
        Queue<int[]> queue = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        queue.offer(new int[]{sCity, 0});
        int delay = 0;
        while (!queue.isEmpty()) {
            int[] road = queue.poll();
            int e = road[0], t = road[1];
            if (e == eCity) break;
            for (Integer ee : cities[e]) {
                if (roads.get(conn[e][ee])[2] + t < dis[ee]) {
                    dis[ee] = roads.get(conn[e][ee])[2] + t;
                    queue.offer(new int[]{ee, dis[ee]});
                    prev[ee] = e;
                }
            }
        }
        if (dis[eCity] == Integer.MAX_VALUE) return -1;
        int ke = eCity;
        while (prev[ke] != -1) {
            int ks = prev[ke];
            int roadId = conn[ks][ke];
            if (roadId == 0) break;
            int[] road = roads.get(roadId);
            remove(roadId);
            int minTime = getMinTime(sCity, eCity);
            add(roadId, ks, ke, road[2]);
            ke = ks;
            if (minTime == Integer.MAX_VALUE) {
                return -1;
            }
            delay = Math.max(minTime - dis[eCity], delay);
        }
        return delay;
    }

    public int getMinTime(int sCity, int eCity) {
        int[] dis = new int[N];
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[sCity] = 0;
        Queue<int[]> queue = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        queue.offer(new int[]{sCity, 0});
        while (!queue.isEmpty()) {
            int[] road = queue.poll();
            int e = road[0], t = road[1];
            if (e == eCity) break;
            for (Integer ee : cities[e]) {
                if (roads.get(conn[e][ee])[2] + t < dis[ee]) {
                    dis[ee] = roads.get(conn[e][ee])[2] + t;
                    queue.offer(new int[]{ee, dis[ee]});
                }
            }
        }
        return dis[eCity];
    }
}

public class Solution {
    private final static int MAX_K = 5000;
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_CALC = 400;

    private final static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) {
        int q = sc.nextInt();

        int n, k;
        int[] mIdArr = new int[MAX_K];
        int[] sCityArr = new int[MAX_K];
        int[] eCityArr = new int[MAX_K];
        int[] mTimeArr = new int[MAX_K];
        int mId, sCity, eCity, mTime;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            cmd = sc.nextInt();
            switch (cmd) {
                case CMD_INIT:
                    okay = true;
                    n = sc.nextInt();
                    k = sc.nextInt();
                    for (int j = 0; j < k; ++j) {
                        mIdArr[j] = sc.nextInt();
                        sCityArr[j] = sc.nextInt();
                        eCityArr[j] = sc.nextInt();
                        mTimeArr[j] = sc.nextInt();
                    }
                    usersolution.init(n, k, mIdArr, sCityArr, eCityArr, mTimeArr);
                    break;
                case CMD_ADD:
                    mId = sc.nextInt();
                    sCity = sc.nextInt();
                    eCity = sc.nextInt();
                    mTime = sc.nextInt();
                    usersolution.add(mId, sCity, eCity, mTime);
                    break;
                case CMD_REMOVE:
                    mId = sc.nextInt();
                    usersolution.remove(mId);
                    break;
                case CMD_CALC:
                    sCity = sc.nextInt();
                    eCity = sc.nextInt();
                    ans = sc.nextInt();
                    ret = usersolution.calculate(sCity, eCity);
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
        int TC, MARK;

        System.setIn(new java.io.FileInputStream("acm/pro2522t/sample_input.txt"));

        Scanner sc = new Scanner(System.in);

        TC = sc.nextInt();
        MARK = sc.nextInt();

        long s = System.currentTimeMillis();
        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }
        System.out.println(System.currentTimeMillis() - s);

        sc.close();
    }
}

class UserSolutionOther {
    int N;
    List<Road>[] roadList;
    Map<Integer, Road> roadMap;

    // index : end city
    // value : road to end city
    Road[] prev;

    @SuppressWarnings("unchecked")
    public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
        this.N = N;
        this.roadMap = new HashMap<>();

        this.roadList = new List[N];
        for (int i = 0; i < N; i++)
            roadList[i] = new ArrayList<>();

        for (int i = 0; i < K; i++)
            add(mId[i], sCity[i], eCity[i], mTime[i]);
    }

    public void add(int mId, int sCity, int eCity, int mTime) {
        Road r = new Road(mId, sCity, eCity, mTime);
        roadMap.put(mId, r);
        roadList[sCity].add(r);
    }

    public void remove(int mId) {
        Road r = roadMap.remove(mId);
        roadList[r.sCity].remove(r);
    }

    public int calculate(int sCity, int eCity) {
        int res = -1;

        // record road path
        prev = new Road[N];

        int before = dijkstra(sCity, eCity, true);
        if (before == -1) {
            return -1;
        }

        // try to destroy the road path recorded before
        int p = eCity;
        while (p != sCity) {
            Road r = prev[p];

            r.destroyed = true;
            int after = dijkstra(sCity, eCity, false);
            r.destroyed = false;

            if (after == -1) {
                return -1;
            } else {
                res = Math.max(res, after - before);
                p = r.sCity;
            }
        }

        return res;
    }

    // city, distance
    Queue<int[]> queue = new PriorityQueue<>((a, b) -> a[1] - b[1]);
    int[] distance = new int[1000];

    private int dijkstra(int sCity, int eCity, boolean update) {
        queue.clear();

        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[sCity] = 0;

        queue.add(new int[] { sCity, 0 });

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();

            if (curr[0] == eCity) {
                return curr[1];
            }

            for (Road r : roadList[curr[0]]) {
                if (!r.destroyed) {
                    int nCity = r.eCity;
                    int nTime = curr[1] + r.mTime;

                    if (nTime < distance[nCity]) {
                        distance[nCity] = nTime;
                        if (update) {
                            prev[nCity] = r;
                        }
                        queue.add(new int[] { nCity, nTime });
                    }
                }
            }
        }

        return -1;
    }
}

class Road {
    int mId, sCity, eCity, mTime;
    boolean destroyed;

    public Road(int mId, int sCity, int eCity, int mTime) {
        this.mId = mId;
        this.sCity = sCity;
        this.eCity = eCity;
        this.mTime = mTime;
        this.destroyed = false;
    }
}