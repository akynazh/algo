package pro.pro2519t;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    int N, T;
    Map<Integer, Order> orders;
    Set<Order> sortedOrders;
    Kitchen[] kitchens;
    Set<Integer> cancelledOrders;

    static class Order {
        int id, m, c, t;
        int[] all; // type - count
        int[] done; // type - doneCount

        public Order(int n, int id, int m, int[] beverages, int t) {
            this.id = id;
            this.m = m;
            this.all = new int[n + 1];
            this.done = new int[n + 1];
            for (int i = 0; i < m; i++) {
                all[beverages[i]] += 1;
            }
            this.t = t;
        }
    }

    static class Kitchen {
        Queue<Order> orderQueue = new ArrayDeque<>();
    }

    public void init(int N) {
        this.N = N;
        this.T = 0;
        this.orders = new HashMap<>();
        this.kitchens = new Kitchen[N + 1];
        for (int i = 1; i <= N; i++) {
            this.kitchens[i] = new Kitchen();
        }
        this.cancelledOrders = new HashSet<>();

        this.sortedOrders = new TreeSet<>((o1, o2) -> {
            int k1 = o1.m - o1.c, k2 = o2.m - o2.c;
            return k1 == k2 ? o1.t - o2.t : k2 - k1;
        });
    }

    public int order(int mID, int M, int mBeverages[]) {
        Order order = new Order(N, mID, M, mBeverages, T);
        orders.put(mID, order);
        sortedOrders.add(order);
        for (int type = 1; type <= N; type++) {
            if (order.all[type] != 0) {
                kitchens[type].orderQueue.offer(order);
            }
        }
        T++;
        return orders.size();
    }

    public int supply(int mBeverage) {
        Queue<Order> orderQueue = kitchens[mBeverage].orderQueue;
        if (orderQueue.isEmpty()) {
            return -1;
        }
        Order order = orderQueue.peek();
        sortedOrders.remove(order);
        // !!! 必须在修改 order 前从 TreeSet 中删除 order，否则修改后删除失效

        order.done[mBeverage] += 1;
        order.c += 1;
        if (order.done[mBeverage] == order.all[mBeverage]) {
            orderQueue.poll();
        }
        if (order.c == order.m) {
            orders.remove(order.id);
        } else {
            sortedOrders.add(order);
        }
        return order.id;
    }

    public int cancel(int mID) {
        if (cancelledOrders.contains(mID)) return -1;
        Order order = orders.get(mID);
        if (order == null) return 0;

        orders.remove(mID);
        sortedOrders.remove(order);
        cancelledOrders.add(mID);
        int[] done = order.done;

        for (int i = 1; i <= N; i++) {
            kitchens[i].orderQueue.remove(order);
            if (done[i] > 0) {
                for (int k = 0; k < done[i]; k++) {
                    if (-1 == supply(i)) break;
                }
            }
        }

        return order.m - order.c;
    }

    public int getStatus(int mID) {
        if (cancelledOrders.contains(mID)) return -1;
        if (!orders.containsKey(mID)) return 0;
        Order order = orders.get(mID);
        return order.m - order.c;
    }

    Solution.RESULT hurry() {
        Solution.RESULT res = new Solution.RESULT();
//        ArrayList<Order> hurryOrders = new ArrayList<>(orders.values());
//        hurryOrders.sort((o1, o2) -> {
//            int k1 = o1.m - o1.c, k2 = o2.m - o2.c;
//            return k1 == k2 ? o1.sample_input.txt - o2.sample_input.txt : k2 - k1;
//        });
        int cnt = 0;
        for (Order order : sortedOrders) {
            if (cnt == 5) break;
            res.IDs[cnt] = order.id;
            cnt++;
        }
        res.cnt = cnt;
        return res;
    }
}

class Solution {
    private static final int CMD_INIT = 100;
    private static final int CMD_ORDER = 200;
    private static final int CMD_SUPPLY = 300;
    private static final int CMD_CANCEL = 400;
    private static final int CMD_GET_STATUS = 500;
    private static final int CMD_HURRY = 600;

    private static UserSolution usersolution = new UserSolution();

    private static final int MAX_NUM_BEVERAGES = 10;

    public static class RESULT {
        int cnt;
        int[] IDs = new int[5];

        RESULT() {
            cnt = -1;
        }
    }

    private static boolean run(BufferedReader br) throws Exception {
        int Q, N, M;
        int mID, mBeverage;

        int[] mBeverages = new int[MAX_NUM_BEVERAGES];

        int ret = -1, ans, cnt;

        RESULT res;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(st.nextToken());

        boolean okay = false;

        for (int q = 0; q < Q; ++q) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case CMD_INIT:
                    N = Integer.parseInt(st.nextToken());
                    usersolution.init(N);
                    okay = true;
                    break;
                case CMD_ORDER:
                    mID = Integer.parseInt(st.nextToken());
                    M = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < M; ++i)
                        mBeverages[i] = Integer.parseInt(st.nextToken());
                    ret = usersolution.order(mID, M, mBeverages);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_SUPPLY:
                    mBeverage = Integer.parseInt(st.nextToken());
                    ret = usersolution.supply(mBeverage);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_CANCEL:
                    mID = Integer.parseInt(st.nextToken());
                    ret = usersolution.cancel(mID);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_GET_STATUS:
                    mID = Integer.parseInt(st.nextToken());
                    ret = usersolution.getStatus(mID);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_HURRY:
                    res = usersolution.hurry();
                    cnt = Integer.parseInt(st.nextToken());
                    if (res.cnt != cnt)
                        okay = false;
                    for (int i = 0; i < cnt; ++i) {
                        ans = Integer.parseInt(st.nextToken());
                        if (res.IDs[i] != ans)
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
        System.setIn(new java.io.FileInputStream("acm/pro2519t/sample_input.txt"));

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