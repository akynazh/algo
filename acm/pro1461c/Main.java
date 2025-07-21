package pro1461c;

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

class UserSolution1 {
    int K;
    int L;
    Map<Integer, Point> points;

    static class Point {
        int id;
        int x;
        int y;
        int c;

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
        this.points = new HashMap<>(20000);
    }

    public void addSample(int mID, int mX, int mY, int mC) {
        points.put(mID, new Point(mID, mX, mY, mC));
    }

    public void deleteSample(int mID) {
        points.remove(mID);
    }

    public int predict(int mX, int mY) {
        PriorityQueue<Point> queue = new PriorityQueue<>((p1, p2) -> {
            int d1 = Math.abs(p1.x - mX) + Math.abs(p1.y - mY);
            int d2 = Math.abs(p2.x - mX) + Math.abs(p2.y - mY);
            if (d1 == d2) {
                if (p1.x == p2.x) {
                    return p2.y - p1.y;
                } else {
                    return p2.x - p1.x;
                }
            } else {
                return d2 - d1;
            }
        });
        for (Integer key : points.keySet()) {
            Point point = points.get(key);
            queue.offer(point);
            if (queue.size() > K) {
                queue.poll();
            }
        }
        Point peek = queue.peek();
        int d = Math.abs(peek.x - mX) + Math.abs(peek.y - mY);
        if (d > L) {
            return -1;
        } else {
            Map<Integer, Integer> cc = new HashMap<>();
            while (!queue.isEmpty()) {
                Point poll = queue.poll();
                if (cc.containsKey(poll.c)) {
                    cc.put(poll.c, cc.get(poll.c) + 1);
                } else {
                    cc.put(poll.c, 1);
                }
            }

            PriorityQueue<int[]> tq = new PriorityQueue<>((a, b) -> {
                if (a[1] == b[1]) {
                    return a[0] - b[0];
                } else {
                    return b[1] - a[1];
                }
            });

            for (Integer key : cc.keySet()) {
                tq.offer(new int[]{key, cc.get(key)});
            }
            return tq.peek()[0];
        }
    }
}


class UserSolution2 {
    private int K;
    private int L;
    private Map<Integer, Point> points;

    static class Point {
        int id;
        int x;
        int y;
        int c;

        Point(int id, int x, int y, int c) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.c = c;
        }
    }

    static class PointWithDistance {
        Point point;
        int distance;

        PointWithDistance(Point point, int distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    public void init(int K, int L) {
        this.K = K;
        this.L = L;
        this.points = new HashMap<>();
    }

    public void addSample(int mID, int mX, int mY, int mC) {
        points.put(mID, new Point(mID, mX, mY, mC));
    }

    public void deleteSample(int mID) {
        points.remove(mID);
    }

    public int predict(int mX, int mY) {
        if (points.isEmpty()) {
            return -1;
        }

        // 使用最小堆，按距离排序
        PriorityQueue<PointWithDistance> minHeap = new PriorityQueue<>((a, b) -> {
            if (a.distance != b.distance) {
                return Integer.compare(a.distance, b.distance);
            }
            if (a.point.x != b.point.x) {
                return Integer.compare(a.point.x, b.point.x);
            }
            return Integer.compare(b.point.y, a.point.y);
        });

        // 构建堆，计算距离
        for (Map.Entry<Integer, Point> entry : points.entrySet()) {
            Point p = entry.getValue();
            int dist = Math.abs(p.x - mX) + Math.abs(p.y - mY);
            minHeap.offer(new PointWithDistance(p, dist));
        }

        // 获取前 K 个最近点
        List<Point> kNearest = new ArrayList<>();
        while (kNearest.size() < K && !minHeap.isEmpty()) {
            kNearest.add(minHeap.poll().point);
        }

        // 判断最远点是否超过 L
        Point farthest = kNearest.get(kNearest.size() - 1);
        int maxDist = Math.abs(farthest.x - mX) + Math.abs(farthest.y - mY);
        if (maxDist > L) {
            return -1;
        }

        // 类别统计
        Map<Integer, Integer> categoryCount = new HashMap<>();
        for (Point p : kNearest) {
            categoryCount.put(p.c, categoryCount.getOrDefault(p.c, 0) + 1);
        }

        // 找出类别票数最多（相同则选数值小的）
        int maxCategory = -1;
        int maxCount = -1;
        for (Map.Entry<Integer, Integer> entry : categoryCount.entrySet()) {
            int category = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount || (count == maxCount && category < maxCategory)) {
                maxCategory = category;
                maxCount = count;
            }
        }

        return maxCategory;
    }
}


class UserSolution3 {
    int K;
    int L;
    int gridSize = 10;
    Map<Integer, Point> points;
    Map<Integer, Map<Integer, List<Point>>> grid; // xGrid -> yGrid -> List<Point>

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
        this.grid = new HashMap<>();
    }

    public void addSample(int mID, int mX, int mY, int mC) {
        Point p = new Point(mID, mX, mY, mC);
        points.put(mID, p);

        int gx = coordToGrid(mX);
        int gy = coordToGrid(mY);
        grid.computeIfAbsent(gx, k -> new HashMap<>()).computeIfAbsent(gy, k -> new ArrayList<>()).add(p);
    }

    public void deleteSample(int mID) {
        Point p = points.remove(mID);
        if (p != null) {
            int gx = coordToGrid(p.x);
            int gy = coordToGrid(p.y);
            Map<Integer, List<Point>> yMap = grid.get(gx);
            if (yMap != null) {
                List<Point> list = yMap.get(gy);
                if (list != null) {
                    list.remove(p);
                    if (list.isEmpty()) {
                        yMap.remove(gy);
                    }
                }
            }
        }
    }

    public int predict(int mX, int mY) {
        if (points.isEmpty()) return -1;

        // 使用最小堆查找最近点
        PriorityQueue<PointWithDistance> minHeap = new PriorityQueue<>((a, b) -> {
            if (a.distance != b.distance) return Integer.compare(b.distance, a.distance);
            if (a.point.x != b.point.x) return Integer.compare(b.point.x, a.point.x);
            return Integer.compare(b.point.y, a.point.y);
        });

        // 从中心格子开始，向外扩展搜索
        int cx = coordToGrid(mX);
        int cy = coordToGrid(mY);
        Set<String> visited = new HashSet<>();

        int maxDis = 0;
        for (int r = 0; minHeap.size() < K && r < 2000; r++) {
            if (maxDis > L) {
                break;
            }
            for (int dx = -r; dx <= r && minHeap.size() < K; dx++) {
                for (int dy = -r; dy <= r && minHeap.size() < K; dy++) {
                    if (Math.max(Math.abs(dx), Math.abs(dy)) != r) continue;
                    int gx = cx + dx;
                    int gy = cy + dy;
                    String key = gx + "," + gy;
                    if (visited.contains(key)) continue;
                    visited.add(key);

                    Map<Integer, List<Point>> yMap = grid.get(gx);
                    if (yMap != null) {
                        List<Point> list = yMap.get(gy);
                        if (list != null) {
                            for (Point p : list) {
                                int dist = Math.abs(p.x - mX) + Math.abs(p.y - mY);
                                maxDis = Math.max(maxDis, dist);
                                minHeap.offer(new PointWithDistance(p, dist));
                                if (minHeap.size() > K) {
                                    minHeap.poll();
                                }
                            }
                        }
                    }
                }
            }
        }

        if (minHeap.size() < K) {
            return -1;
        }

        Point farthest = minHeap.peek().point;
        int maxDist = Math.abs(farthest.x - mX) + Math.abs(farthest.y - mY);
        if (maxDist > L) return -1;

        // 统计类别
        Map<Integer, Integer> categoryCount = new HashMap<>();
        while (!minHeap.isEmpty()) {
            Point p = minHeap.poll().point;
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

    // 坐标转格子编号
    private int coordToGrid(int a) {
        return a / gridSize;
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


