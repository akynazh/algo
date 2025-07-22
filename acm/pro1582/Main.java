package pro1582;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class Main {

    private static UserSolution usersolution = new UserSolution();

    private final static int CMD_INIT = 0;
    private final static int CMD_ADD = 1;
    private final static int CMD_REMOVE = 2;
    private final static int CMD_GETCNT = 3;

    private static boolean run(BufferedReader br) throws Exception {
        int id, stime, etime;
        int ret, ans;

        boolean ok = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int Q = Integer.parseInt(st.nextToken());

        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                stime = Integer.parseInt(st.nextToken());
                usersolution.init(stime);
                ok = true;
            } else if (cmd == CMD_ADD) {
                id = Integer.parseInt(st.nextToken());
                stime = Integer.parseInt(st.nextToken());
                etime = Integer.parseInt(st.nextToken());
                usersolution.add(id, stime, etime);
            } else if (cmd == CMD_REMOVE) {
                id = Integer.parseInt(st.nextToken());
                usersolution.remove(id);
            } else if (cmd == CMD_GETCNT) {
                stime = Integer.parseInt(st.nextToken());
                ret = usersolution.getCnt(stime);
                ans = Integer.parseInt(st.nextToken());
                if (ret != ans) {
                    ok = false;
                }
            } else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("acm/samples/pro1582/sample.in"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line = new StringTokenizer(br.readLine(), " ");

        int T = Integer.parseInt(line.nextToken());
        int MARK = Integer.parseInt(line.nextToken());

        long s = System.currentTimeMillis();
        for (int tc = 1; tc <= T; tc++) {
            long a = System.currentTimeMillis();
            int score = run(br) ? MARK : 0;
            long b = System.currentTimeMillis();
            System.out.println("#" + tc + " " + score + " " + (b - a));
        }
        System.out.println(System.currentTimeMillis() - s);

        br.close();
    }
}

class UserSolution {
    int musicTime;
    Map<Integer, Integer[]> emps;
    TreeMap<Integer, Set<Integer>> startMap;
    TreeMap<Integer, Set<Integer>> endMap;

    void init(int musicTime) {
        this.musicTime = musicTime;
        emps = new HashMap<>();
        startMap = new TreeMap<>();
        endMap = new TreeMap<>();
    }

    void add(int mID, int mStart, int mEnd) {
        if (emps.containsKey(mID)) {
            Integer[] t = emps.get(mID);
            emps.remove(mID);
            startMap.get(t[0]).remove(mID);
            endMap.get(t[1]).remove(mID);
        }
        emps.put(mID, new Integer[]{mStart, mEnd});
        startMap.computeIfAbsent(mStart, k -> new HashSet<>()).add(mID);
        endMap.computeIfAbsent(mEnd, k -> new HashSet<>()).add(mID);
    }

    void remove(int mID) {
        if (emps.containsKey(mID)) {
            Integer[] t = emps.get(mID);
            emps.remove(mID);
            startMap.get(t[0]).remove(mID);
            endMap.get(t[1]).remove(mID);
        }
    }

    int getCnt(int mBSTime) {
        SortedMap<Integer, Set<Integer>> m1 = startMap.headMap(mBSTime, true);
//        SortedMap<Integer, Set<Integer>> m2 = endMap.tailMap(mBSTime + this.musicTime, true);
        Set<Integer> s1 = new HashSet<>();
        for (Set<Integer> ids : m1.values()) {
            s1.addAll(ids);
        }
        int cnt = 0;
        for (int id : s1) {
            Integer[] t = emps.get(id);
            if (t[1] >= mBSTime + this.musicTime) {
                cnt++;
            }
        }
        return cnt;
    }
}

class SegmentTree {
    int MAX = 2000000;
    int[] tree, lazy;

    public SegmentTree() {
        tree = new int[MAX * 4];
        lazy = new int[MAX * 4];
    }

    public void update(int L, int R, int val) {
        update(1, 0, MAX, L, R, val);
    }

    public int query(int idx) {
        return query(1, 0, MAX, idx);
    }

    private void push(int node, int l, int r) {
        if (lazy[node] != 0) {
            tree[node] += lazy[node] * (r - l + 1);
            if (l != r) {
                lazy[node * 2] += lazy[node];
                lazy[node * 2 + 1] += lazy[node];
            }
            lazy[node] = 0;
        }
    }

    private void update(int node, int l, int r, int L, int R, int val) {
        push(node, l, r);
        if (r < L || l > R) return;
        if (L <= l && r <= R) {
            lazy[node] += val;
            push(node, l, r);
            return;
        }
        int mid = (l + r) / 2;
        update(node * 2, l, mid, L, R, val);
        update(node * 2 + 1, mid + 1, r, L, R, val);
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }

    private int query(int node, int l, int r, int idx) {
        push(node, l, r);
        if (l == r) return tree[node];
        int mid = (l + r) / 2;
        if (idx <= mid) return query(node * 2, l, mid, idx);
        else return query(node * 2 + 1, mid + 1, r, idx);
    }
}

class UserSolution1 {
    private SegmentTree seg;
    private Map<Integer, int[]> emps;
    private int musicTime;

    void init(int musicTime) {
        this.seg = new SegmentTree();
        this.emps = new HashMap<>();
        this.musicTime = musicTime;
    }

    void add(int mID, int mStart, int mEnd) {
        if (emps.containsKey(mID)) {
            remove(mID);
        }

        int end = mEnd - musicTime;
        if (mStart <= end) {
            seg.update(mStart, end, 1);
            emps.put(mID, new int[]{mStart, mEnd});
        }
    }

    void remove(int mID) {
        if (!emps.containsKey(mID)) return;

        int[] range = emps.remove(mID);
        int mStart = range[0];
        int mEnd = range[1];
        int end = mEnd - musicTime;

        if (mStart <= end) {
            seg.update(mStart, end, -1);
        }
    }

    int getCnt(int mBSTime) {
        // start <= mBSTime <= end - musicTime
        return Math.max(0, seg.query(mBSTime));
    }
}
