package pro.pro2512t;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    int N;
    Company[] comps;
    Map<Integer, Car> cars;

    static class Company {
        int capacity;
        TreeSet<Car> cars;
        int eventPeriods;

        public Company(int capacity) {
            this.capacity = capacity;
            this.cars = new TreeSet<>();
        }
    }

    static class Car implements Comparable<Car> {
        int id;
        int expDate;
        int compId;
        boolean towed;

        public Car(int id, int expDate, int compId) {
            this.id = id;
            this.expDate = expDate;
            this.compId = compId;
        }

        /**
         * 如果不比对 ID，当两个 car 的 expDate 一致，它们被视为相当，那么其中一个 car 不被插入！！
         */
        @Override
        public int compareTo(Car o) {
            if (expDate != o.expDate) return expDate - o.expDate;
            return id - o.id;
        }
    }

    public void init(int N, int mCapacity[]) {
        this.N = N;
        this.comps = new Company[N];
        for (int i = 0; i < N; i++) {
            this.comps[i] = new Company(mCapacity[i]);
        }
        this.cars = new HashMap<>();
    }

    public int park(int mDate, int mID, int mCompany, int mPeriod) {
        Company comp = comps[mCompany];
        if (comp.cars.size() == comp.capacity) {
            return -1;
        }
        Car car = new Car(mID, mDate + mPeriod - 1 - comp.eventPeriods, mCompany);
        cars.put(mID, car);
        comp.cars.add(car);
        return comp.cars.size();
    }

    public int retrieve(int mDate, int mID) {
        Car car = cars.remove(mID);
        if (car.towed) {
            return mDate - car.expDate;
        }
        Company comp = comps[car.compId];
        comp.cars.remove(car);
        int expirationDate = car.expDate + comp.eventPeriods;
        return expirationDate >= mDate ? expirationDate - mDate : mDate - expirationDate;
    }

    public void buy(int mID, int mPeriod) {
        Car car = cars.get(mID);
        if (car.towed) return;
        Company comp = comps[car.compId];
        comp.cars.remove(car);
        car.expDate += mPeriod;
        comp.cars.add(car);
    }

    public void event(int mCompany, int mPeriod) {
        comps[mCompany].eventPeriods += mPeriod;
    }

    public int inspect(int mDate, int mCompany) {
        Company comp = comps[mCompany];
        int c = 0;
        Iterator<Car> it = comp.cars.iterator();
        while (it.hasNext()) {
            Car car = it.next();
            if (car.expDate + comp.eventPeriods >= mDate) break;
            it.remove();
            car.towed = true;
            car.expDate += comp.eventPeriods;
            c++;
        }
        return c;
    }
}

class UserSolution_Ref {

    class Car implements Comparable<Car> {

        public int id;
        protected int company;
        protected int date;
        protected boolean towed = false;

        public Car(int id, int company, int date) {
            super();
            this.id = id;
            this.company = company;
            this.date = date;
        }

        @Override
        public int compareTo(Car car) {
            if (this.date == car.date) {
                return this.id - car.id;
            }
            return this.date - car.date;
        }
    }

    // Max amount of companies is 10
    HashMap<Integer, Car> cars;
    TreeSet<Car> parkings[];
    int events[];
    int cap[];

    public void init(int N, int mCapacity[]) {
        cars = new HashMap<>();
        parkings = new TreeSet[N];
        cap = new int[N];
        events = new int[N];

        for (int i = 0; i < N; i++) {
            events[i] = 0;
            cap[i] = mCapacity[i];
            parkings[i] = new TreeSet<>();
        }
    }

    public int park(int mDate, int mID, int mCompany, int mPeriod) {
        if (parkings[mCompany].size() >= cap[mCompany]) return -1;
        int endTime = mDate + mPeriod - events[mCompany] - 1;
        Car car = new Car(mID, mCompany, endTime);
        parkings[mCompany].add(car);
        cars.put(mID, car);
        return parkings[mCompany].size();
    }

    public int retrieve(int mDate, int mID) {
        int res = 0;
        Car car = this.cars.get(mID);
        if (car.towed) {
            res = mDate - (car.date);
        } else {
            this.parkings[car.company].remove(car);
            res = Math.abs(mDate - (car.date + this.events[car.company]));
        }
        return res;
    }

    public void buy(int mID, int mPeriod) {
        Car car = this.cars.get(mID);
        if (car.towed) return;

        parkings[car.company].remove(car);
        car.date += mPeriod;
        parkings[car.company].add(car);
    }

    public void event(int mCompany, int mPeriod) {
        this.events[mCompany] += mPeriod;
    }

    public int inspect(int mDate, int mCompany) {
        Iterator<Car> iter = this.parkings[mCompany].iterator();
        int res = 0;

        while (iter.hasNext()) {

            Car car = iter.next();
            if (car.date + this.events[mCompany] < mDate) {
                res++;
                iter.remove();
                car.towed = true;
                car.date += this.events[mCompany];
            } else break;
        }
        return res;
    }

}

class Solution {
    private static final int MAX_N = 10;
    private static final int CMD_INIT = 100;
    private static final int CMD_PARK = 200;
    private static final int CMD_RETRIEVE = 300;
    private static final int CMD_BUY = 400;
    private static final int CMD_EVENT = 500;
    private static final int CMD_INSPECT = 600;

    private static UserSolution usersolution = new UserSolution();

    private static boolean run(BufferedReader br) throws Exception {
        int Q;
        int N, mID, mDate, mCompany, mPeriod;
        int[] mCapacity = new int[MAX_N];

        int ret, ans;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(st.nextToken());

        boolean okay = false;

        for (int q = 0; q < Q; ++q) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case CMD_INIT:
                    N = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < N; ++i) {
                        mCapacity[i] = Integer.parseInt(st.nextToken());
                    }
                    usersolution.init(N, mCapacity);
                    okay = true;
                    break;
                case CMD_PARK:
                    mDate = Integer.parseInt(st.nextToken());
                    mID = Integer.parseInt(st.nextToken());
                    mCompany = Integer.parseInt(st.nextToken());
                    mPeriod = Integer.parseInt(st.nextToken());
                    ret = usersolution.park(mDate, mID, mCompany, mPeriod);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_RETRIEVE:
                    mDate = Integer.parseInt(st.nextToken());
                    mID = Integer.parseInt(st.nextToken());
                    ret = usersolution.retrieve(mDate, mID);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_BUY:
                    mID = Integer.parseInt(st.nextToken());
                    mPeriod = Integer.parseInt(st.nextToken());
                    usersolution.buy(mID, mPeriod);
                    break;
                case CMD_EVENT:
                    mCompany = Integer.parseInt(st.nextToken());
                    mPeriod = Integer.parseInt(st.nextToken());
                    usersolution.event(mCompany, mPeriod);
                    break;
                case CMD_INSPECT:
                    mDate = Integer.parseInt(st.nextToken());
                    mCompany = Integer.parseInt(st.nextToken());
                    ret = usersolution.inspect(mDate, mCompany);
                    ans = Integer.parseInt(st.nextToken());
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
        System.setIn(new java.io.FileInputStream("acm/pro2512t/sample_input.txt"));

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