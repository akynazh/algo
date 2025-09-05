package pro2516t;

import java.util.*;

class UserSolution {
    int N, maxLen;
    Map<Integer, Road> roadMap;
    HashSet<Integer>[] spots;

    static class Road {
        int id, a, b, len;

        public Road(int id, int a, int b, int len) {
            this.id = id;
            this.a = a;
            this.b = b;
            this.len = len;
        }
    }

    static class Path {
        int[] roads;
        int totalLen;

        public Path(int[] roads, int totalLen) {
            this.roads = roads;
            this.totalLen = totalLen;
        }

        boolean hasDupRoad(int[] otherRoads) {
            for (int i = 0; i < 4; i++) {
                for (int i1 = 0; i1 < 4; i1++) {
                    if (otherRoads[i1] == roads[i]) return true;
                }
            }
            return false;
        }
    }

    void init(int N) {
        this.N = N;
        this.roadMap = new HashMap<>();
        this.spots = new HashSet[N + 1];
        for (int i = 1; i <= N; i++) spots[i] = new HashSet<>();
    }

    void addRoad(int K, int mID[], int mSpotA[], int mSpotB[], int mLen[]) {
        for (int i = 0; i < K; i++) {
            Road road = new Road(mID[i], mSpotA[i], mSpotB[i], mLen[i]);
            roadMap.put(mID[i], road);
            spots[mSpotA[i]].add(mID[i]);
            spots[mSpotB[i]].add(mID[i]);
        }
    }

    void removeRoad(int mID) {
        Road road = roadMap.remove(mID);
        if (road != null) {
            spots[road.a].remove(road.id);
            spots[road.b].remove(road.id);
        }
    }

    int getLength(int mSpot) {
        maxLen = -1;
        List<Path>[] spotPaths = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            spotPaths[i] = new ArrayList<>();
        }
        getLength(spotPaths, new HashSet<>(), mSpot, 0, 0);
        return maxLen;
    }

    void getLength(List<Path>[] spotPaths, HashSet<Integer> visited, int curSpot, int curLen, int curRoadCount) {
        if (curRoadCount == 4) {
            int[] curRoads = new int[4];
            int i = 0;
            for (Integer id : visited) {
                curRoads[i] = id;
                i++;
            }
            for (Path path : spotPaths[curSpot]) {
                if (path.hasDupRoad(curRoads) || path.totalLen + curLen > 42195 || path.totalLen + curLen < maxLen) {
                    continue;
                }
                maxLen = path.totalLen + curLen;
            }
            spotPaths[curSpot].add(new Path(curRoads, curLen));
            return;
        }
        if (curLen > 42195) return;
        for (Integer roadId : spots[curSpot]) {
            if (!visited.contains(roadId)) {
                Road road = roadMap.get(roadId);
                int spot = road.a == curSpot ? road.b : road.a;
                visited.add(roadId);
                getLength(spotPaths, visited, spot, curLen + road.len, curRoadCount + 1);
                visited.remove(roadId);
            }
        }
    }
}

public class Solution {

    private static UserSolution usersolution = new UserSolution();

    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_GETLEN = 400;

    private static boolean run(Scanner sc) {
        int id[] = new int[10];
        int sa[] = new int[10];
        int sb[] = new int[10];
        int len[] = new int[10];
        String strTmp;

        boolean ok = false;

        int Q = sc.nextInt();

        for (int q = 0; q < Q; q++) {
            int cmd = sc.nextInt();

            if (cmd == CMD_INIT) {
                int N = sc.nextInt();
                usersolution.init(N);
                ok = true;
            } else if (cmd == CMD_ADD) {
                strTmp = sc.next();
                int K = sc.nextInt();
                for (int i = 0; i < K; i++) {
                    strTmp = sc.next();
                    id[i] = sc.nextInt();
                    strTmp = sc.next();
                    sa[i] = sc.nextInt();
                    strTmp = sc.next();
                    sb[i] = sc.nextInt();
                    strTmp = sc.next();
                    len[i] = sc.nextInt();
                }
                usersolution.addRoad(K, id, sa, sb, len);
            } else if (cmd == CMD_REMOVE) {
                strTmp = sc.next();
                id[0] = sc.nextInt();
                usersolution.removeRoad(id[0]);
            } else if (cmd == CMD_GETLEN) {
                strTmp = sc.next();
                id[0] = sc.nextInt();
                int ret = usersolution.getLength(id[0]);
                strTmp = sc.next();
                int ans = sc.nextInt();
                if (ret != ans) {
                    ok = false;
                }
            } else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("acm/pro2516t/sample_input.txt"));

        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();
        int MARK = sc.nextInt();

        long s = System.currentTimeMillis();
        for (int tc = 1; tc <= T; tc++) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        System.out.println(System.currentTimeMillis() - s);

        sc.close();
    }
}
