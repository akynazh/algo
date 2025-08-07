package pro0725e;

import java.util.*;


public class Solution {
}


class UserSolution {
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
