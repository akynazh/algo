package pro164c;

import java.util.Scanner;
import java.util.*;

class Main {
    private static final int CMD_INIT = 100;
    private static final int CMD_ORDER = 200;
    private static final int CMD_CANCEL = 300;
    private static final int CMD_GET_STATUS = 400;

    private static final int MAX_NUM_DISHES = 10;

    private static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) throws Exception {
        int Q, N, M;
        int mTime, mID;

        int[] mCookingTimeList = new int[MAX_NUM_DISHES];
        int[] mDishes = new int[MAX_NUM_DISHES];

        int ret = -1, ans;

        Q = sc.nextInt();

        boolean okay = false;

        for (int q = 0; q < Q; ++q) {
            int cmd;

            cmd = sc.nextInt();
            switch (cmd) {
                case CMD_INIT:
                    N = sc.nextInt();
                    for (int i = 0; i < N; ++i)
                        mCookingTimeList[i] = sc.nextInt();
                    usersolution.init(N, mCookingTimeList);
                    okay = true;
                    break;
                case CMD_ORDER:
                    mTime = sc.nextInt();
                    mID = sc.nextInt();
                    M = sc.nextInt();
                    for (int i = 0; i < M; ++i)
                        mDishes[i] = sc.nextInt();
                    ret = usersolution.order(mTime, mID, M, mDishes);
                    ans = sc.nextInt();
                    System.out.println(ret + " - " + ans);
                    if (ans != ret)
                        okay = false;
                    break;
                case CMD_CANCEL:
                    mTime = sc.nextInt();
                    mID = sc.nextInt();
                    ret = usersolution.cancel(mTime, mID);
                    ans = sc.nextInt();
                    System.out.println(ret + " - " + ans);

                    if (ans != ret)
                        okay = false;
                    break;
                case CMD_GET_STATUS:
                    mTime = sc.nextInt();
                    mID = sc.nextInt();
                    ret = usersolution.getStatus(mTime, mID);
                    ans = sc.nextInt();
                    System.out.println(ret + " - " + ans);

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
        System.setIn(new java.io.FileInputStream("acm/pro164c/sample.in"));

        Scanner sc = new Scanner(System.in);

        int TC = sc.nextInt();
        int MARK = sc.nextInt();

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        sc.close();

    }
}

class UserSolution {
    static class Order {
        int id;
        int cnt;
        boolean isCancel = false;
        ArrayList<Integer> done = new ArrayList<>();

        Order(int id, int cnt) {
            this.id = id;
            this.cnt = cnt;
        }
    }

    static class Kitchen {
        int id;
        int cookingTime;
        int time; // 下一个 dish 完成的时间
        ArrayDeque<Order> orders = new ArrayDeque<>();

        public Kitchen(int id, int cookingTime) {
            this.id = id;
            this.cookingTime = cookingTime;
            this.time = -1;
        }
    }

    Kitchen[] kitchens;
    HashMap<Integer, Order> orders;
    int N;
    int orderCnt;

    void init(int N, int[] mCookingTimeList) {
        this.N = N;
        kitchens = new Kitchen[N + 1];
        orders = new HashMap<>();
        orderCnt = 0;
        for (int i = 1; i <= N; i++) {
            kitchens[i] = new Kitchen(i, mCookingTimeList[i - 1]);
        }
    }

    int order(int mTime, int mID, int M, int[] mDishes) {
        update(mTime);
        Order order = new Order(mID, M);
        orders.put(mID, order);

        for (int i = 0; i < M; i++) {
            Kitchen kitchen = kitchens[mDishes[i]];
            kitchen.orders.add(order);

            if (kitchen.time == -1) {
                kitchen.time = mTime + kitchen.cookingTime;
            }
        }

        orderCnt++;
        return orderCnt;
    }

    int cancel(int mTime, int mID) {
        update(mTime);
        Order order = orders.get(mID);
        order.isCancel = true;

        for (Integer idx : order.done) {
            Kitchen kitchen = kitchens[idx];
            // 做好的 dish 传递给下个订单
            while (!kitchen.orders.isEmpty()) {
                if (kitchen.orders.peek().isCancel) {
                    kitchen.orders.poll();
                    continue;
                }

                Order ord = kitchen.orders.poll();
                ord.done.add(idx);
                if (ord.cnt == ord.done.size()) {
                    orderCnt--;
                }
                break;
            }
            if (kitchen.orders.isEmpty()) {
                kitchen.time = -1;
            }
        }

        orderCnt--;
        return orderCnt;
    }

    int getStatus(int mTime, int mID) {
        update(mTime);
        Order order = orders.get(mID);
        return order.isCancel ? -1 : order.cnt - order.done.size();
    }

    void update(int mTime) {
        for (int i = 1; i <= N; i++) {
            Kitchen kitchen = kitchens[i];
            while (!kitchen.orders.isEmpty() && kitchen.time <= mTime) {
                Order order = kitchen.orders.poll();
                if (order.isCancel) {
                    continue;
                }

                order.done.add(i);
                kitchen.time += kitchen.cookingTime;
                if (order.cnt == order.done.size()) orderCnt--;
            }
            while (!kitchen.orders.isEmpty() && kitchen.orders.peek().isCancel) {
                kitchen.orders.poll();
            }
            if (kitchen.orders.isEmpty()) {
                kitchen.time = -1;
            }
        }
    }
}
