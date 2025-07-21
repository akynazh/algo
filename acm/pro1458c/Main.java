package pro1458c;

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
        System.setIn(new java.io.FileInputStream("acm/samples/pro1458c/sample.in"));

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

class UserSolution1 {
    int N;
    int[] mCookingTimeList;
    Map<Integer, Set<Integer>> orders;
    int orderLeftCount;
    Kitchen[] kitchens;
    int time;

    static class Kitchen {
        final int foodType;
        final int foodTime;
        int foodTimeLeft;
        Queue<Integer> currentOrders;
        Queue<Integer> pendingOrders;
        int readyCount;

        public Kitchen(int foodType, int foodTime) {
            this.foodType = foodType;
            this.foodTime = foodTime;
            this.foodTimeLeft = foodTime;
            this.currentOrders = new ArrayDeque<>();
            this.pendingOrders = new ArrayDeque<>();
            this.readyCount = 0;
        }
    }

    public void arrange(int mTime) {
        for (int i = 1; i <= N; i++) {
            Kitchen k = kitchens[i];
            int passTime = mTime - this.time;
            while (!k.currentOrders.isEmpty() && passTime > 0) {
                Integer currentOrderId = k.currentOrders.peek();

                // cancelled order
                if (orders.get(currentOrderId) == null) {
                    k.currentOrders.poll();
                    if (k.currentOrders.isEmpty()) {
                        // stop
                        k.foodTimeLeft = k.foodTime;
                    }
                    continue;
                }

                if (k.foodTimeLeft > passTime) {
                    k.foodTimeLeft -= passTime;
                    break;
                }
                passTime -= k.foodTimeLeft;
                k.foodTimeLeft = k.foodTime;
                k.currentOrders.poll();
                if (orders.get(currentOrderId) != null) {
                    orders.get(currentOrderId).remove(k.foodType);
                }
                k.pendingOrders.offer(currentOrderId);
                k.readyCount += 1;
            }
        }

        for (int i = 1; i <= N; i++) {
            Kitchen k = kitchens[i];
            while (!k.pendingOrders.isEmpty()) {
                Integer pendingOrderId = k.pendingOrders.peek();
                if (orders.get(pendingOrderId) == null) {
                    k.pendingOrders.poll();
                    if (k.currentOrders.isEmpty()) {
                        // drop
                        k.readyCount -= 1;
                    } else {
                        Integer id = k.currentOrders.poll();
                        if (orders.get(id) != null) {
                            orders.get(id).remove(k.foodType);
                        }
                        k.pendingOrders.offer(id);
                    }
                } else if (orders.get(pendingOrderId).isEmpty()) {
                    k.readyCount -= 1;
                    k.pendingOrders.poll();
                } else {
                    break;
                }
            }
        }

        int cnt = 0;
        for (Integer k : orders.keySet()) {
            if (orders.get(k) == null || orders.get(k).isEmpty()) {
                cnt += 1;
            }
        }
        orderLeftCount = orders.size() - cnt;

        this.time = mTime;
    }

    public void init(int N, int[] mCookingTimeList) {
        this.N = N;
        this.mCookingTimeList = mCookingTimeList;
        this.orders = new HashMap<>();
        this.orderLeftCount = 0;
        this.kitchens = new Kitchen[N + 1];
        for (int i = 1; i <= N; i++) {
            kitchens[i] = new Kitchen(i, mCookingTimeList[i - 1]);
        }
        this.time = 1;
    }

    public int order(int mTime, int mID, int M, int[] mDishes) {
        arrange(mTime);
        Set<Integer> dishes = new HashSet<>();
        for (int i = 0; i < M; i++) {
            int foodType = mDishes[i];
            dishes.add(foodType);
            kitchens[foodType].currentOrders.offer(mID);
        }
        orders.put(mID, dishes);
        orderLeftCount += 1;
        return orderLeftCount;
    }

    public int cancel(int mTime, int mID) {
        orders.put(mID, null);

        arrange(mTime);
        return orderLeftCount;
    }

    public int getStatus(int mTime, int mID) {
        arrange(mTime);
        Set<Integer> order = orders.get(mID);
        if (order == null) {
            return -1;
        } else if (order.isEmpty()) {
            return 0;
        } else {
            return order.size();
        }
    }
}

class UserSolution2 {
    int N;
    int[] mCookingTimeList;
    Map<Integer, Set<Integer>> orders;
    Set<Integer> doneOrders;
    Set<Integer> cancelledOrders;
    PriorityQueue<Event> eventQueue;
    Map<Integer, Integer> orderCancelTime;
    Map<Integer, Integer> orderStartTime;
    Kitchen[] kitchens;
    int time;

    static class Kitchen {
        final int foodType;
        final int foodTime;
        Queue<Integer> queue;
        boolean isCooking;

        public Kitchen(int foodType, int foodTime) {
            this.foodType = foodType;
            this.foodTime = foodTime;
            this.queue = new LinkedList<>();
            this.isCooking = false;
        }
    }

    static class Event implements Comparable<Event> {
        int time;
        int foodType;
        int orderId;

        public Event(int time, int foodType, int orderId) {
            this.time = time;
            this.foodType = foodType;
            this.orderId = orderId;
        }

        @Override
        public int compareTo(Event other) {
            return Integer.compare(this.time, other.time);
        }
    }

    public void init(int N, int[] mCookingTimeList) {
        this.N = N;
        this.mCookingTimeList = mCookingTimeList;
        this.orders = new HashMap<>();
        this.doneOrders = new HashSet<>();
        this.cancelledOrders = new HashSet<>();
        this.eventQueue = new PriorityQueue<>();
        this.orderCancelTime = new HashMap<>();
        this.orderStartTime = new HashMap<>();
        this.kitchens = new Kitchen[N + 1];
        for (int i = 1; i <= N; i++) {
            kitchens[i] = new Kitchen(i, mCookingTimeList[i - 1]);
        }
        this.time = 1;
    }

    private void advanceTime(int targetTime) {
        while (!eventQueue.isEmpty() && eventQueue.peek().time <= targetTime) {
            Event event = eventQueue.poll();
            handleEvent(event, targetTime);
        }
        this.time = targetTime;
    }

    // dish done
    private void handleEvent(Event event, int mTime) {
        int foodType = event.foodType;
        int orderId = event.orderId;
        Kitchen kitchen = kitchens[foodType];
        kitchen.isCooking = false;
        if (!kitchen.queue.isEmpty() && kitchen.queue.peek() == orderId) {
            kitchen.queue.poll();
        }
        if (cancelledOrders.contains(orderId)) {
            if (!kitchen.queue.isEmpty()) {
                Integer nextOrderId = kitchen.queue.peek();
                if (orderStartTime.get(nextOrderId) <= orderCancelTime.get(orderId)) {
                    eventQueue.offer(new Event(event.time, foodType, kitchen.queue.poll()));
                }
            }
        } else if (!doneOrders.contains(orderId)) {
            Set<Integer> order = orders.get(orderId);
            order.remove(foodType);
            if (order.isEmpty()) {
                doneOrders.add(orderId);
            } else {
                eventQueue.offer(new Event(event.time == mTime ? event.time + 1 : mTime, foodType, orderId));
            }
        }
        if (!kitchen.isCooking) {
            startCooking(foodType, event.time);
        }
    }

    private void startCooking(int foodType, int startTime) {
        Kitchen kitchen = kitchens[foodType];
        if (kitchen.queue.isEmpty()) return;

        while (!kitchen.queue.isEmpty()) {
            int id = kitchen.queue.peek();
            if (cancelledOrders.contains(id) || doneOrders.contains(id)) {
                kitchen.queue.poll();
            } else {
                break;
            }
        }

        if (kitchen.queue.isEmpty()) return;

        int orderId = kitchen.queue.peek();
        kitchen.isCooking = true;
        int finishTime = startTime + kitchen.foodTime;
        eventQueue.offer(new Event(finishTime, foodType, orderId));
    }

    public int order(int mTime, int mID, int M, int[] mDishes) {
        advanceTime(mTime);
        Set<Integer> dishSet = new HashSet<>();
        for (int i = 0; i < M; i++) {
            int foodType = mDishes[i];
            dishSet.add(foodType);
            kitchens[foodType].queue.offer(mID);
            if (!kitchens[foodType].isCooking) {
                startCooking(foodType, mTime);
            }
        }
        orderStartTime.put(mID, mTime);
        orders.put(mID, dishSet);
        return orders.size() - cancelledOrders.size() - doneOrders.size();
    }

    public int cancel(int mTime, int mID) {
        cancelledOrders.add(mID);
        orderCancelTime.put(mID, mTime);
        advanceTime(mTime);
        return orders.size() - cancelledOrders.size() - doneOrders.size();
    }

    public int getStatus(int mTime, int mID) {
        advanceTime(mTime);
        if (cancelledOrders.contains(mID)) return -1;
        Set<Integer> set = orders.get(mID);
        return set.isEmpty() ? 0 : set.size();
    }
}

class UserSolution3 {
    int N;
    int[] mCookingTimeList;
    Map<Integer, BitSet> orders;
    Set<Integer> doneOrders;
    Set<Integer> cancelledOrders;
    PriorityQueue<Long> eventQueue;
    Map<Integer, Integer> orderCancelTime;
    Map<Integer, Integer> orderStartTime;
    Kitchen[] kitchens;
    int time;

    static class Kitchen {
        final int foodType;
        final int foodTime;
        Queue<Integer> queue;
        boolean isCooking;

        public Kitchen(int foodType, int foodTime) {
            this.foodType = foodType;
            this.foodTime = foodTime;
            this.queue = new ArrayDeque<>();
            this.isCooking = false;
        }
    }

    static class Event implements Comparable<Event> {
        int time;
        int foodType;
        int orderId;

        public Event(int time, int foodType, int orderId) {
            this.time = time;
            this.foodType = foodType;
            this.orderId = orderId;
        }

        @Override
        public int compareTo(Event other) {
            return Integer.compare(this.time, other.time);
        }
    }

    private long encodeEvent(int time, int foodType, int orderId) {
        return ((long) time << 32) | ((long) foodType << 28) | (orderId & 0xFFFFFFF);
    }

    private int[] decodeEvent(long encoded) {
        int time = (int) (encoded >>> 32);
        int foodType = (int) ((encoded >> 28) & 0xF);
        int orderId = (int) (encoded & 0xFFFFFFF);
        return new int[]{time, foodType, orderId};
    }

    public void init(int N, int[] mCookingTimeList) {
        this.N = N;
        this.mCookingTimeList = mCookingTimeList;
        this.orders = new HashMap<>();
        this.doneOrders = new HashSet<>();
        this.cancelledOrders = new HashSet<>();
//        this.eventQueue = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        this.eventQueue = new PriorityQueue<>();
        this.orderCancelTime = new HashMap<>();
        this.orderStartTime = new HashMap<>();
        this.kitchens = new Kitchen[N + 1];
        for (int i = 1; i <= N; i++) {
            kitchens[i] = new Kitchen(i, mCookingTimeList[i - 1]);
        }
        this.time = 1;
    }

    private void advanceTime(int targetTime) {
        while (!eventQueue.isEmpty() && decodeEvent(eventQueue.peek())[0] <= targetTime) {
            int[] event = decodeEvent(eventQueue.poll());
            handleEvent(event, targetTime);
        }
        this.time = targetTime;
    }

    // dish done
    private void handleEvent(int[] event, int mTime) {
        int foodType = event[1];
        int orderId = event[2];
        Kitchen kitchen = kitchens[foodType];
        kitchen.isCooking = false;
        if (!kitchen.queue.isEmpty() && kitchen.queue.peek() == orderId) {
            kitchen.queue.poll();
        }
        if (cancelledOrders.contains(orderId)) {
            if (!kitchen.queue.isEmpty()) {
                Integer nextOrderId = kitchen.queue.peek();
                if (nextOrderId != null && orderStartTime.get(nextOrderId) <= orderCancelTime.get(orderId)) {
                    eventQueue.offer(encodeEvent(event[0], foodType, kitchen.queue.poll()));
                }
            }
        } else if (!doneOrders.contains(orderId)) {
//            Set<Integer> order = orders.get(orderId);
            BitSet order = orders.get(orderId);
//            order.remove(foodType);
            order.clear(foodType);
            if (order.isEmpty()) {
                doneOrders.add(orderId);
            } else {
                eventQueue.offer(encodeEvent(mTime + 1, foodType, orderId));
            }
        }
        if (!kitchen.isCooking) {
            startCooking(foodType, event[0]);
        }
    }

    private void startCooking(int foodType, int startTime) {
        Kitchen kitchen = kitchens[foodType];
        if (kitchen.queue.isEmpty()) return;

        while (!kitchen.queue.isEmpty()) {
            int id = kitchen.queue.peek();
            if (cancelledOrders.contains(id) || doneOrders.contains(id)) {
                kitchen.queue.poll();
            } else {
                break;
            }
        }

        if (kitchen.queue.isEmpty()) return;

        int orderId = kitchen.queue.peek();
        kitchen.isCooking = true;
        int finishTime = startTime + kitchen.foodTime;
        eventQueue.offer(encodeEvent(finishTime, foodType, orderId));
    }

    public int order(int mTime, int mID, int M, int[] mDishes) {
        advanceTime(mTime);
//        Set<Integer> dishSet = new HashSet<>();
        BitSet dishSet = new BitSet();
        for (int i = 0; i < M; i++) {
            int foodType = mDishes[i];
            dishSet.set(foodType);
//            dishSet.add(foodType);
            kitchens[foodType].queue.offer(mID);
            if (!kitchens[foodType].isCooking) {
                startCooking(foodType, mTime);
            }
        }
        orderStartTime.put(mID, mTime);
        orders.put(mID, dishSet);
        return orders.size() - cancelledOrders.size() - doneOrders.size();
    }

    public int cancel(int mTime, int mID) {
        cancelledOrders.add(mID);
        orderCancelTime.put(mID, mTime);
        advanceTime(mTime);
        return orders.size() - cancelledOrders.size() - doneOrders.size();
    }

    public int getStatus(int mTime, int mID) {
        advanceTime(mTime);
        if (cancelledOrders.contains(mID)) return -1;
//        Set<Integer> set = orders.get(mID);
        BitSet set = orders.get(mID);
        return set.isEmpty() ? 0 : set.cardinality();
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
