package pro.pro0725e;

import java.util.*;

class UserSolution {
    final int MAX_B = 301;
    int N;
    int[] cities; // city - charge
    Map<Integer, Road> roads; // roadId - road
    Set<Integer>[] conn; // cityS -> cityE(set<>)
    int[][] connRoads; // cityS -> cityE = roadId (0: unreachable)

    static class Road {
        int id, s, e, time, power;

        Road(int id, int s, int e, int time, int power) {
            this.id = id;
            this.s = s;
            this.e = e;
            this.time = time;
            this.power = power;
        }
    }

    public void init(int N, int mCharge[], int K, int mId[], int sCity[], int eCity[], int mTime[], int mPower[]) {
        this.N = N;
        cities = new int[N];
        roads = new HashMap<>();
        conn = new HashSet[N];
        connRoads = new int[N][N];

        for (int i = 0; i < N; i++) {
            conn[i] = new HashSet<>();
            cities[i] = mCharge[i];
        }
        for (int i = 0; i < K; i++) {
            add(mId[i], sCity[i], eCity[i], mTime[i], mPower[i]);
        }
    }

    public void add(int mId, int sCity, int eCity, int mTime, int mPower) {
        Road road = new Road(mId, sCity, eCity, mTime, mPower);
        roads.put(mId, road);
        conn[road.s].add(road.e);
        connRoads[road.s][road.e] = road.id;
    }

    public void remove(int mId) {
        Road road = roads.remove(mId);
        conn[road.s].remove(road.e);
        connRoads[road.s][road.e] = 0;
    }

    static class State implements Comparable<State> {
        int city; // current city
        int cb; // current battery
        int ct; // current time

        State(int city, int cb, int ct) {
            this.city = city;
            this.cb = cb;
            this.ct = ct;
        }

        @Override
        public int compareTo(State o) {
            return this.ct - o.ct;
        }
    }

    static class VirusState implements Comparable<VirusState> {
        // s -> city
        int city;
        int ct;

        VirusState(int city, int ct) {
            this.city = city;
            this.ct = ct;
        }

        @Override
        public int compareTo(VirusState o) {
            return this.ct - o.ct;
        }
    }

    public int[] getVirusTime(int M, int mCity[], int mTime[]) {
        int[] virusTime = new int[N];
        Arrays.fill(virusTime, Integer.MAX_VALUE);
        Queue<VirusState> queue = new PriorityQueue<>();
        for (int i = 0; i < M; i++) {
            virusTime[mCity[i]] = mTime[i];
            queue.offer(new VirusState(mCity[i], mTime[i]));
        }
        while (!queue.isEmpty()) {
            VirusState state = queue.poll();
            for (Integer nextCity : conn[state.city]) {
                Road road = roads.get(connRoads[state.city][nextCity]);
                if (virusTime[nextCity] > state.ct + road.time) {
                    virusTime[nextCity] = state.ct + road.time;
                    queue.offer(new VirusState(nextCity, state.ct + road.time));
                }
            }
        }
        return virusTime;
    }

    public int cost(int B, int sCity, int eCity, int M, int mCity[], int mTime[]) {
        int[] virusTime = getVirusTime(M, mCity, mTime);
        int[][] minTime = new int[N][MAX_B];
        for (int i = 0; i < N; i++) {
            Arrays.fill(minTime[i], Integer.MAX_VALUE);
        }
        Queue<State> queue = new PriorityQueue<>();
        queue.offer(new State(sCity, B, 0));
        // 可以发现 minTime 和 State 是对应的
        minTime[sCity][B] = 0;
        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (state.city == eCity) {
                return state.ct;
            }
            if (state.ct > minTime[state.city][state.cb]) {
                continue;
            }
            minTime[state.city][state.cb] = state.ct;

            int s = state.city, cb = state.cb, ct = state.ct, charge = cities[state.city];
            // no charge
            for (Integer nextCity : conn[s]) {
                Road road = roads.get(connRoads[s][nextCity]);
                int newT = ct + road.time, newB = cb - road.power;
                if (newB < 0 || newT >= virusTime[nextCity] || newT >= minTime[nextCity][newB]) continue;
                queue.offer(new State(nextCity, newB, newT));
                for (int i = cb; i >= 0; i--) {
                    if (minTime[s][i] > ct) {
                        minTime[s][i] = ct;
                    }
                }
            }
            // charge
            if (cb < B) {
                int newT = ct + 1, newB = Math.min(B, cb + charge);
                if (newT < virusTime[s] && newT < minTime[s][newB]) {
                    queue.offer(new State(s, newB, newT));
                    for (int i = newB; i >= 0; i--) {
                        if (minTime[s][i] > newT) {
                            minTime[s][i] = newT;
                        }
                    }
                }
            }
        }
        return -1;
    }
}


class UserSolutionRef {
    static class City {
        int id, charge, virusTime;
        ArrayList<Road> roads;

        City(int id, int charge) {
            this.id = id;
            this.charge = charge;
            roads = new ArrayList<>();
        }
    }

    static class Road {
        int id, power, time, prevCity, nextCity;

        Road(int id, int power, int time, int prevCity, int nextCity) {
            this.id = id;
            this.power = power;
            this.time = time;
            this.prevCity = prevCity;
            this.nextCity = nextCity;
        }
    }

    static class Node implements Comparable<Node> {
        int city, power, time;

        Node(int city, int power, int time) {
            this.city = city;
            this.power = power;
            this.time = time;
        }

        @Override
        public int compareTo(Node o) {
            return this.time - o.time;
        }
    }

    City[] cities;
    HashMap<Integer, Road> hmapRoad;
    int n;
    int[][] minTime;
    Queue<Node> queue;

    public void init(int N, int mCharge[], int K, int mId[], int sCity[],
                     int eCity[], int mTime[], int mPower[]) {
        cities = new City[N];
        n = N;
        hmapRoad = new HashMap<>();
        minTime = new int[n][301];
        queue = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            cities[i] = new City(i, mCharge[i]);
        }
        for (int i = 0; i < K; i++) {
            add(mId[i], sCity[i], eCity[i], mTime[i], mPower[i]);
        }
    }

    public void add(int mId, int sCity, int eCity, int mTime, int mPower) {
        Road r = new Road(mId, mPower, mTime, sCity, eCity);
        cities[sCity].roads.add(r);
        hmapRoad.put(mId, r);
    }

    public void remove(int mId) {
        Road r = hmapRoad.remove(mId);
        cities[r.prevCity].roads.remove(r);
    }

    public int cost(int B, int sCity, int eCity, int M, int mCity[],
                    int mTime[]) {
        for (int i = 0; i < n; i++) {
            cities[i].virusTime = Integer.MAX_VALUE;
        }
        // Virus explode
        bfs(M, mCity, mTime);

        for (int i = 0; i < n; i++) {
            Arrays.fill(minTime[i], Integer.MAX_VALUE);
        }
        queue.clear();
        queue.add(new Node(sCity, B, 0));
        minTime[sCity][B] = 0;

        while (!queue.isEmpty()) {
            Node n = queue.poll();
            City c = cities[n.city];
            if (n.city == eCity)
                return n.time;
            if (n.time > minTime[c.id][n.power])
                continue;
            // No charge
            for (Road r : c.roads) {
                City nCity = cities[r.nextCity];
                int newP = n.power - r.power;
                int newT = n.time + r.time;
                if (newP < 0 || newT >= nCity.virusTime
                        || newT >= minTime[nCity.id][newP])
                    continue;
                queue.add(new Node(nCity.id, newP, newT));
                for (int i = n.power; i >= 0; i--)
                    if (minTime[c.id][i] > n.time) {
                        minTime[c.id][i] = n.time;
                    }
            }

            // Charge
            if (n.power < B) {
                n.power = Math.min(n.power + c.charge, B);
                n.time++;
                if (n.time < c.virusTime && n.time < minTime[c.id][n.power]) {
                    queue.add(new Node(n.city, n.power, n.time));
                    for (int i = n.power; i >= 0; i--)
                        if (minTime[c.id][i] > n.time) {
                            minTime[c.id][i] = n.time;
                        }
                }
            }
        }

        return -1;
    }

    private void bfs(int m, int[] city, int[] time) {
        LinkedList<City> queue = new LinkedList<>();

        for (int i = 0; i < m; i++) {
            cities[city[i]].virusTime = time[i];
            queue.add(cities[city[i]]);
        }

        while (!queue.isEmpty()) {
            City c = queue.poll();
            for (Road r : c.roads) {
                City nCity = cities[r.nextCity];
                if (nCity.virusTime > r.time + c.virusTime) {
                    nCity.virusTime = c.virusTime + r.time;
                    queue.add(nCity);
                }
            }
        }
    }
}

class UserSolution0725TLE {
    int N;
    int[] cities; // city - charge
    Map<Integer, Road> roads; // roadId - road
    Set<Integer>[] conn; // cityS -> cityE(set<>)
    int[][] connRoads; // cityS -> cityE = roadId (0: unreachable)

    static class Road {
        int id, s, e, time, power;

        Road(int id, int s, int e, int time, int power) {
            this.id = id;
            this.s = s;
            this.e = e;
            this.time = time;
            this.power = power;
        }
    }

    public void init(int N, int mCharge[], int K, int mId[], int sCity[], int eCity[], int mTime[], int mPower[]) {
        this.N = N;
        cities = new int[N];
        roads = new HashMap<>();
        conn = new HashSet[N];
        connRoads = new int[N][N];

        for (int i = 0; i < N; i++) {
            conn[i] = new HashSet<>();
            cities[i] = mCharge[i];
        }
        for (int i = 0; i < K; i++) {
            Road road = new Road(mId[i], sCity[i], eCity[i], mTime[i], mPower[i]);
            roads.put(mId[i], road);
            conn[road.s].add(road.e);
            connRoads[road.s][road.e] = road.id;
        }
    }

    public void add(int mId, int sCity, int eCity, int mTime, int mPower) {
        Road road = new Road(mId, sCity, eCity, mTime, mPower);
        roads.put(mId, road);
        conn[road.s].add(road.e);
        connRoads[road.s][road.e] = road.id;
    }

    public void remove(int mId) {
        Road road = roads.remove(mId);
        conn[road.s].remove(road.e);
        connRoads[road.s][road.e] = 0;
        return;
    }

    static class State implements Comparable<State> {
        int city; // current city
        int cb; // current battery
        int ct; // current time

        State(int city, int cb, int ct) {
            this.city = city;
            this.cb = cb;
            this.ct = ct;
        }

        @Override
        public int compareTo(State o) {
            return this.ct - o.ct;
        }
    }

    static class DisState implements Comparable<DisState> {
        // s -> city
        int city;
        int ct;

        DisState(int city, int ct) {
            this.city = city;
            this.ct = ct;
        }

        @Override
        public int compareTo(DisState o) {
            return this.ct - o.ct;
        }
    }

    public int[] getTmpDisTime(int s, int[] tmpDisTime) {
        Queue<DisState> queue = new PriorityQueue<>();
        queue.offer(new DisState(s, tmpDisTime[s]));
        while (!queue.isEmpty()) {
            DisState state = queue.poll();
            for (Integer nextCity : conn[state.city]) {
                Road road = roads.get(connRoads[state.city][nextCity]);
                if (tmpDisTime[nextCity] > state.ct + road.time) {
                    tmpDisTime[nextCity] = state.ct + road.time;
                    queue.offer(new DisState(nextCity, state.ct + road.time));
                }
            }
        }
        return tmpDisTime;
    }

    public int cost(int B, int sCity, int eCity, int M, int mCity[], int mTime[]) {
        int[] disTime = new int[N];
        Arrays.fill(disTime, Integer.MAX_VALUE);
        for (int i = 0; i < M; i++) {
            int[] tmpDisTime = new int[N];
            int ds = mCity[i];
            Arrays.fill(tmpDisTime, Integer.MAX_VALUE);
            tmpDisTime[ds] = mTime[i];
            tmpDisTime = getTmpDisTime(ds, tmpDisTime);
            for (int k = 0; k < N; k++) disTime[k] = Math.min(disTime[k], tmpDisTime[k]);
        }

        Queue<State> queue = new PriorityQueue<>();
        queue.offer(new State(sCity, B, 0));
        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (state.ct >= disTime[state.city]) {
                continue;
            }
            if (state.city == eCity) {
                return state.ct;
            }
            int s = state.city, cb = state.cb, ct = state.ct, charge = cities[state.city];
            for (Integer nextCity : conn[s]) {
                Road road = roads.get(connRoads[s][nextCity]);
                if (road.power > cb) {
                    int needPower = road.power - cb;
                    int chargeTime = needPower / charge + ((needPower % charge) == 0 ? 0 : 1);
                    if (ct + chargeTime < disTime[state.city])
                        queue.offer(new State(nextCity, cb + chargeTime * charge - road.power, ct + chargeTime + road.time));
                } else {
                    queue.offer(new State(nextCity, cb - road.power, ct + road.time));
                    if (charge > cities[nextCity] && ct + 1 < disTime[state.city]) {
                        queue.offer(new State(nextCity, cb + 1 * charge - road.power, ct + 1 + road.time));
                    }
                }
            }
        }

        return -1;
    }
}

class Solution {
    private final static int MAX_N = 500;
    private final static int MAX_M = 5;
    private final static int MAX_K = 4000;
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_COST = 400;

    private final static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) {
        int q = sc.nextInt();

        int n, m, k, b;
        int[] mChargeArr = new int[MAX_N];
        int[] mIdArr = new int[MAX_K];
        int[] sCityArr = new int[MAX_K];
        int[] eCityArr = new int[MAX_K];
        int[] mTimeArr = new int[MAX_K];
        int[] mPowerArr = new int[MAX_K];
        int[] mCityArr = new int[MAX_M];
        int mId, sCity, eCity, mTime, mPower;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            cmd = sc.nextInt();
            switch (cmd) {
                case CMD_INIT:
                    okay = true;
                    n = sc.nextInt();
                    k = sc.nextInt();
                    for (int j = 0; j < n; ++j) {
                        mChargeArr[j] = sc.nextInt();
                    }
                    for (int j = 0; j < k; ++j) {
                        mIdArr[j] = sc.nextInt();
                        sCityArr[j] = sc.nextInt();
                        eCityArr[j] = sc.nextInt();
                        mTimeArr[j] = sc.nextInt();
                        mPowerArr[j] = sc.nextInt();
                        ;
                    }
                    usersolution.init(n, mChargeArr, k, mIdArr, sCityArr, eCityArr, mTimeArr, mPowerArr);
                    break;
                case CMD_ADD:
                    mId = sc.nextInt();
                    sCity = sc.nextInt();
                    eCity = sc.nextInt();
                    mTime = sc.nextInt();
                    mPower = sc.nextInt();
                    usersolution.add(mId, sCity, eCity, mTime, mPower);
                    break;
                case CMD_REMOVE:
                    mId = sc.nextInt();
                    usersolution.remove(mId);
                    break;
                case CMD_COST:
                    b = sc.nextInt();
                    sCity = sc.nextInt();
                    eCity = sc.nextInt();
                    ans = sc.nextInt();
                    m = sc.nextInt();
                    for (int j = 0; j < m; ++j) {
                        mCityArr[j] = sc.nextInt();
                        mTimeArr[j] = sc.nextInt();
                    }
                    ret = usersolution.cost(b, sCity, eCity, m, mCityArr, mTimeArr);
                    if (ans != ret)
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

        System.setIn(new java.io.FileInputStream("acm/pro0725e/sample_input.txt"));

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
