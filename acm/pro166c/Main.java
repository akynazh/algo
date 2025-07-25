package pro166c;

import java.util.*;

public class Main {
    private static final int CMD_INIT = 100;
    private static final int CMD_ADD_SAMPLE = 200;
    private static final int CMD_DELETE_SAMPLE = 300;
    private static final int CMD_PREDICT = 400;

    private static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) throws Exception {
        int Q;
        int K, L;
        int mID, mX, mY, mC;

        int ret = -1, ans;

        Q = sc.nextInt();

        boolean okay = false;

        for (int q = 0; q < Q; ++q) {
            int cmd = sc.nextInt();

            switch (cmd) {
                case CMD_INIT:
                    K = sc.nextInt();
                    L = sc.nextInt();
                    usersolution.init(K, L);
                    okay = true;
                    break;
                case CMD_ADD_SAMPLE:
                    mID = sc.nextInt();
                    mX = sc.nextInt();
                    mY = sc.nextInt();
                    mC = sc.nextInt();
                    usersolution.addSample(mID, mX, mY, mC);
                    break;
                case CMD_DELETE_SAMPLE:
                    mID = sc.nextInt();
                    usersolution.deleteSample(mID);
                    break;
                case CMD_PREDICT:
                    mX = sc.nextInt();
                    mY = sc.nextInt();
                    ret = usersolution.predict(mX, mY);
                    ans = sc.nextInt();
                    System.out.println(ret + " " + ans);
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
        System.setIn(new java.io.FileInputStream("acm/samples/pro1461c/input"));

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
    int K;
    int L;
    int divSize = 100;
    int gridSize = 50;
    Map<Integer, Point> points;
    Set<Point>[][] grid;

    static class Point {
        int id, x, y, c;

        Point(int id, int x, int y, int c) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.c = c;
        }
    }

    public void init(int K, int L) {
        this.K = K;
        this.L = L;
        this.points = new HashMap<>();
        this.grid = new HashSet[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new HashSet<>();
            }
        }
    }

    public void addSample(int mID, int mX, int mY, int mC) {
        Point p = new Point(mID, mX, mY, mC);
        points.put(mID, p);
        int gx = coordToGrid(mX);
        int gy = coordToGrid(mY);
        grid[gx][gy].add(p);
    }

    public void deleteSample(int mID) {
        Point p = points.remove(mID);
        if (p != null) {
            int gx = coordToGrid(p.x);
            int gy = coordToGrid(p.y);
            grid[gx][gy].remove(p);
        }
    }

    public int predict(int mX, int mY) {
        if (points.isEmpty()) return -1;

        PriorityQueue<PointWithDistance> minHeap = new PriorityQueue<>((a, b) -> {
            if (a.distance != b.distance) return Integer.compare(a.distance, b.distance);
            if (a.point.x != b.point.x) return Integer.compare(a.point.x, b.point.x);
            return Integer.compare(a.point.y, b.point.y);
        });

        int cx = coordToGrid(mX);
        int cy = coordToGrid(mY);
        Set<String> visited = new HashSet<>();

        // If 4000 * 4000 square is evenly divided into 100 * 100 square.The maximum number of data with an arbitrary location in a 100 * 100 square is 500.
        // 4 <= L <= 100
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int gx = cx + dx;
                int gy = cy + dy;
                String key = gx + "," + gy;
                if (visited.contains(key)) continue;
                visited.add(key);

                if (gx < 0 || gx >= gridSize || gy < 0 || gy >= gridSize) {
                    continue;
                }

                for (Point p : grid[gx][gy]) {
                    int dist = Math.abs(p.x - mX) + Math.abs(p.y - mY);
                    minHeap.offer(new PointWithDistance(p, dist));
                }
            }
        }

        if (minHeap.size() < K) {
            return -1;
        }

        Map<Integer, Integer> categoryCount = new HashMap<>();
        for (int i = 0; i < K && !minHeap.isEmpty(); i++) {
            Point p = minHeap.poll().point;
            if (Math.abs(p.x - mX) + Math.abs(p.y - mY) > L) return -1;
            categoryCount.put(p.c, categoryCount.getOrDefault(p.c, 0) + 1);
        }

        int maxC = -1, maxCount = -1;
        for (Map.Entry<Integer, Integer> entry : categoryCount.entrySet()) {
            int c = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount || (count == maxCount && c < maxC)) {
                maxC = c;
                maxCount = count;
            }
        }

        return maxC;
    }

    private int coordToGrid(int a) {
        return a / divSize;
    }

    static class PointWithDistance {
        Point point;
        int distance;

        PointWithDistance(Point point, int distance) {
            this.point = point;
            this.distance = distance;
        }
    }
}


