package pro170c;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    int N, M, L;
    PriorityQueue<String>[] zones;
    Map<String, Car> cars;
    TreeSet<Car> sortedParkedCars;
    Map<Integer, TreeSet<Car>> zCars;

    static class Car implements Comparable<Car> {
        String id;
        int xx, zzzz;
        char y;
        boolean parked;
        int parkedTime;
        int zoneId;
        String slot;

        public Car(String id) {
            this.id = id;
            this.xx = Integer.parseInt(id.substring(0, 2));
            this.y = id.charAt(2);
            this.zzzz = Integer.parseInt(id.substring(3));
        }

        @Override
        public int compareTo(Car o) {
            if (parked != o.parked) return parked ? -1 : 1;
            if (xx != o.xx) return xx - o.xx;
            return y - o.y;
        }
    }

    public void init(int N, int M, int L) {
        this.N = N;
        this.M = M;
        this.L = L;
        zones = new PriorityQueue[26];
        for (int i = 0; i < N; i++) {
            zones[i] = new PriorityQueue<>();
            for (int k = 0; k < M; k++) {
                zones[i].offer(String.format("%c%03d", (char) ('A' + i), k));
            }
        }
        cars = new HashMap<>();
        sortedParkedCars = new TreeSet<>((c1, c2) -> c1.parkedTime - c2.parkedTime);
        zCars = new HashMap<>();
    }

    public Main.RESULT_E enter(int mTime, String mCarNo) {
        update(mTime);
        Main.RESULT_E res_e = new Main.RESULT_E();
        Car car;
        if (cars.containsKey(mCarNo)) { // towed car
            car = cars.get(mCarNo);
            zCars.get(car.zzzz).remove(car);
        } else {
            car = new Car(mCarNo);
        }
        PriorityQueue<String> zone = zones[0];
        int zoneId = 0;
        for (int i = 1; i < N; i++) {
            if (zones[i].size() > zone.size()) {
                zone = zones[i];
                zoneId = i;
            }
        }
        if (zone.isEmpty()) {
            cars.remove(mCarNo);
            res_e.success = 0;
            return res_e;
        }
        car.parked = true;
        car.parkedTime = mTime;
        car.zoneId = zoneId;
        car.slot = zone.poll();
        cars.put(mCarNo, car);
        sortedParkedCars.add(car);
        zCars.computeIfAbsent(car.zzzz, k -> new TreeSet<>()).add(car);
        res_e.success = 1;
        res_e.locname = car.slot;
        return res_e;
    }

    public int pullout(int mTime, String mCarNo) {
        update(mTime);
        if (!cars.containsKey(mCarNo)) return -1;
        Car car = cars.remove(mCarNo);
        sortedParkedCars.remove(car);
        zCars.get(car.zzzz).remove(car);
        if (car.parked) {
            zones[car.zoneId].offer(car.slot);
            return mTime - car.parkedTime;
        } else {
            return (L + (mTime - (car.parkedTime + L)) * 5) * (-1);
        }
    }

    public Main.RESULT_S search(int mTime, String mStr) {
        update(mTime);
        Main.RESULT_S res_s = new Main.RESULT_S();
        TreeSet<Car> set = zCars.getOrDefault(Integer.parseInt(mStr), new TreeSet<>());
        int cnt = 0;
        for (Car car : set) {
            if (cnt == 5) break;
            res_s.carlist[cnt] = car.id;
            cnt++;
        }
        res_s.cnt = cnt;
        return res_s;
    }

    void update(int mTime) {
        while (!sortedParkedCars.isEmpty() && sortedParkedCars.first().parkedTime + L <= mTime) {
            Car car = sortedParkedCars.first();
            sortedParkedCars.remove(car);
            zCars.get(car.zzzz).remove(car);
            car.parked = false;
            zones[car.zoneId].offer(car.slot);
            zCars.get(car.zzzz).add(car);
        }
    }
}

class UserSolutionMLE {
    int N, M, L;
    TreeSet<Zone> zones;
    Map<String, Car> cars;
    TreeSet<Car> sortedParkedCars;
    Map<Integer, TreeSet<Car>> zCars;

    /**
     * Zone 和 Slot 是冗余结构，设计后，考虑下是否可以改为单一变量，比如 Zone + Slot -> PriorityQueue[]: i(zoneId) - slots(By Str)
     */

    static class Zone implements Comparable<Zone> {
        int id;
        TreeSet<Slot> slots;
        int c;

        public Zone(int id, int m) {
            this.id = id;
            this.slots = new TreeSet<>();
            this.c = m;
            for (int i = 0; i < m; i++) {
                this.slots.add(new Slot(i));
            }
        }

        @Override
        public int compareTo(Zone o) {
            if (c != o.c) return o.c - c;
            return id - o.id;
        }
    }

    static class Slot implements Comparable<Slot> {
        int id;
        boolean hasCar;

        public Slot(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Slot o) {
            if (hasCar != o.hasCar) return hasCar ? 1 : -1;
            return id - o.id;
        }
    }

    static class Car implements Comparable<Car> {
        String id;
        int xx, zzzz;
        char y;
        boolean parked;
        int parkedTime;
        Zone zone;
        Slot slot;

        public Car(String id) {
            this.id = id;
            this.xx = Integer.parseInt(id.substring(0, 2));
            this.y = id.charAt(2);
            this.zzzz = Integer.parseInt(id.substring(3));
        }

        @Override
        public int compareTo(Car o) {
            if (parked != o.parked) return parked ? -1 : 1;
            if (xx != o.xx) return xx - o.xx;
            return y - o.y;
        }
    }

    public void init(int N, int M, int L) {
        this.N = N;
        this.M = M;
        this.L = L;
        zones = new TreeSet<>();
        for (int i = 0; i < N; i++) {
            zones.add(new Zone(i, M));
        }
        cars = new HashMap<>();
        sortedParkedCars = new TreeSet<>((c1, c2) -> c1.parkedTime - c2.parkedTime);
        zCars = new HashMap<>();
    }

    public Main.RESULT_E enter(int mTime, String mCarNo) {
        update(mTime);
        Main.RESULT_E res_e = new Main.RESULT_E();
        Car car;
        if (cars.containsKey(mCarNo)) { // towed car
            car = cars.get(mCarNo);
            zCars.get(car.zzzz).remove(car);
        } else {
            car = new Car(mCarNo);
        }
        Zone zone = zones.first();
        if (zone.c == 0) {
            cars.remove(mCarNo);
            res_e.success = 0;
            return res_e;
        }
        zones.remove(zone);
        Slot slot = zone.slots.first();
        zone.slots.remove(slot);
        zone.c -= 1;
        slot.hasCar = true;
        car.parked = true;
        car.parkedTime = mTime;
        car.zone = zone;
        car.slot = slot;
        cars.put(mCarNo, car);
        sortedParkedCars.add(car);
        zCars.computeIfAbsent(car.zzzz, k -> new TreeSet<>()).add(car);
        zone.slots.add(slot);
        zones.add(zone);
        res_e.success = 1;
        res_e.locname = String.format("%c%03d", (char) ('A' + zone.id), slot.id);
        return res_e;
    }

    public int pullout(int mTime, String mCarNo) {
        update(mTime);
        if (!cars.containsKey(mCarNo)) return -1;
        Car car = cars.remove(mCarNo);
        sortedParkedCars.remove(car);
        zCars.get(car.zzzz).remove(car);
        if (car.parked) {
            Zone zone = car.zone;
            Slot slot = car.slot;
            zones.remove(zone);
            zone.slots.remove(slot);
            zone.c += 1;
            slot.hasCar = false;
            zone.slots.add(slot);
            zones.add(zone);
            return mTime - car.parkedTime;
        } else {
            return (L + (mTime - (car.parkedTime + L)) * 5) * (-1);
        }
    }

    public Main.RESULT_S search(int mTime, String mStr) {
        update(mTime);
        Main.RESULT_S res_s = new Main.RESULT_S();
        TreeSet<Car> set = zCars.getOrDefault(Integer.parseInt(mStr), new TreeSet<>());
        int cnt = 0;
        for (Car car : set) {
            if (cnt == 5) break;
            res_s.carlist[cnt] = car.id;
            cnt++;
        }
        res_s.cnt = cnt;
        return res_s;
    }

    void update(int mTime) {
        while (!sortedParkedCars.isEmpty() && sortedParkedCars.first().parkedTime + L <= mTime) {
            Car car = sortedParkedCars.first();
            sortedParkedCars.remove(car);
            zCars.get(car.zzzz).remove(car);
            car.parked = false;
            Zone zone = car.zone;
            Slot slot = car.slot;
            zones.remove(zone);
            zone.slots.remove(slot);
            zone.c += 1;
            slot.hasCar = false;
            zone.slots.add(slot);
            zones.add(zone);
            zCars.get(car.zzzz).add(car);
        }
    }
}

class Main {
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
//            if (okay == false) {
//                System.out.println(cmd);
//                return okay;
//            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception {
        System.setIn(new java.io.FileInputStream("acm/pro170c/sample_input.txt"));

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