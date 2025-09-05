package pro.pro0705e;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


/**
 * [E][H2526][Pro] Automated Parking Lot
 */
class UserSolution {
    class Car implements Comparable<Car> {
        int time, zone;
        boolean isTowed;
        String id, slot, zzzz;

        public Car(String id, int time) {
            this.id = id;
            this.time = time;
            this.zzzz = id.substring(3);
        }

        @Override
        public int compareTo(Car o) {
            if (isTowed != o.isTowed)
                return isTowed ? 1 : -1;
            return id.compareTo(o.id);
        }
    }

    Queue<Car> pq = new PriorityQueue<>((a, b) -> a.time - b.time);
    Queue<String>[] map = new PriorityQueue[26];
    HashMap<String, TreeSet<Car>> hmTop;
    HashMap<String, Car> hm;
    int n, l;

    public void init(int N, int M, int L) {
        n = N;
        l = L;
        pq.clear();
        hm = new HashMap<>();
        hmTop = new HashMap<>();
        for (int i = 0; i < N; i++) {
            map[i] = new PriorityQueue<>();
            for (int j = 0; j < M; j++)
                map[i].add(String.format("%c%03d", (char) (i + 'A'), j));
        }
    }

    void update(int mTime) {
        while (!pq.isEmpty() && pq.peek().time <= mTime) {
            Car c = pq.poll();
            map[c.zone].add(c.slot);

            hmTop.get(c.zzzz).remove(c);
            c.isTowed = true;
            hmTop.get(c.zzzz).add(c);
        }
    }

    public Solution.RESULT_E enter(int mTime, String mCarNo) {
        update(mTime);

        Solution.RESULT_E res = new Solution.RESULT_E();
        res.success = 0;
        Car c = hm.get(mCarNo);
        if (c != null) hmTop.get(c.zzzz).remove(c);

        c = new Car(mCarNo, mTime + l);

        Queue<String> zone = map[0];
        for (int i = 1; i < n; i++)
            if (map[i].size() > zone.size()) {
                zone = map[i];
                c.zone = i;
            }

        if (zone.isEmpty())
            hm.remove(mCarNo);
        else {
            pq.add(c);
            hm.put(mCarNo, c);
            hmTop.computeIfAbsent(c.zzzz, k -> new TreeSet<Car>()).add(c);

            res.locname = c.slot = zone.poll();
            res.success = 1;
        }
        return res;
    }

    public int pullout(int mTime, String mCarNo) {
        Car c = hm.get(mCarNo);
        if (c == null) return -1;

        pq.remove(c);
        hm.remove(mCarNo);
        hmTop.get(c.zzzz).remove(c);

        if (c.isTowed) return -l - (mTime - c.time) * 5;

        map[c.zone].add(c.slot);
        if (c.time > mTime) return mTime - c.time + l;
        return -l - (mTime - c.time) * 5;
    }

    public Solution.RESULT_S search(int mTime, String mStr) {
        update(mTime);

        Solution.RESULT_S res = new Solution.RESULT_S();
        res.cnt = 0;
        if (hmTop.containsKey(mStr))
            for (Car car : hmTop.get(mStr)) {
                res.carlist[res.cnt++] = car.id;
                if (res.cnt == 5) break;
            }

        return res;
    }
}

public class Solution {
    private static final int CMD_INIT = 100;
    private static final int CMD_ENTER = 200;
    private static final int CMD_PULL_OUT = 300;
    private static final int CMD_SEARCH = 400;

    private static UserSolution usersolution = new UserSolution();

    public static class RESULT_E {
        int success;
        String locname;

        RESULT_E() {
            success = -1;
        }
    }

    public static class RESULT_S {
        int cnt;
        String[] carlist = new String[5];

        RESULT_S() {
            cnt = -1;
        }
    }

    private static boolean run(BufferedReader br) throws Exception {
        int Q, N, M, L;
        int mTime;

        String mCarNo;
        String mStr;

        int ret = -1, ans;

        RESULT_E res_e;
        RESULT_S res_s;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(st.nextToken());

        boolean okay = false;

        for (int q = 0; q < Q; ++q) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case CMD_INIT:
                    N = Integer.parseInt(st.nextToken());
                    M = Integer.parseInt(st.nextToken());
                    L = Integer.parseInt(st.nextToken());
                    usersolution.init(N, M, L);
                    okay = true;
                    break;
                case CMD_ENTER:
                    mTime = Integer.parseInt(st.nextToken());
                    mCarNo = st.nextToken();
                    res_e = usersolution.enter(mTime, mCarNo);
                    ans = Integer.parseInt(st.nextToken());
                    if (res_e.success != ans)
                        okay = false;
                    if (ans == 1) {
                        mStr = st.nextToken();
                        if (!mStr.equals(res_e.locname))
                            okay = false;
                    }
                    break;
                case CMD_PULL_OUT:
                    mTime = Integer.parseInt(st.nextToken());
                    mCarNo = st.nextToken();
                    ret = usersolution.pullout(mTime, mCarNo);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_SEARCH:
                    mTime = Integer.parseInt(st.nextToken());
                    mStr = st.nextToken();
                    res_s = usersolution.search(mTime, mStr);
                    ans = Integer.parseInt(st.nextToken());
                    if (res_s.cnt != ans)
                        okay = false;
                    for (int i = 0; i < ans; ++i) {
                        mCarNo = st.nextToken() + mStr;
                        if (!mCarNo.equals(res_s.carlist[i]))
                            okay = false;
                    }
                    break;
                default:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception {
        System.setIn(new java.io.FileInputStream("acm/pro0705e/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int TC = Integer.parseInt(st.nextToken());
        int MARK = Integer.parseInt(st.nextToken());

        long s = System.currentTimeMillis();
        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }
        System.out.println(System.currentTimeMillis() - s);
        br.close();
    }
}