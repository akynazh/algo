package pro2522;

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
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[sCity] = 0;
        Queue<int[]> queue = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        int delay = 0;
        while (!queue.isEmpty()) {
            int[] road = queue.poll();
            int e = road[0], t = road[1];
            if (e == eCity) break;
            for (Integer ee : cities[e]) {
                if (roads.get(conn[e][ee])[2] + t < dis[ee]) {
                    if (dis[ee] != Integer.MAX_VALUE) {
                        delay = Math.max(delay, )
                    }
                    dis[ee] = roads.get(conn[e][ee])[2] + t;
                    queue.offer(new int[]{ee, dis[ee]});
                }
            }
        }
        if (dis[eCity] == Integer.MAX_VALUE) return -1;
        return delay;
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

        //System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

        Scanner sc = new Scanner(System.in);

        TC = sc.nextInt();
        MARK = sc.nextInt();

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        sc.close();
    }
}
