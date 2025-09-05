package pro.pro2520t;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    HashSet<Worm> worms;
    Worm[][] map;
    int N, now;
    int[] dx = {-1, 0, 1, 0};
    int[] dy = {0, 1, 0, -1};

    public void init(int N) {
        this.N = N;
        now = 0;
        worms = new HashSet<>();
        map = new Worm[N][N];
    }

    private boolean inMap(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

    private void updateMap() {
        now++;
        List<Worm> dead = new ArrayList<>();
        for (Worm w : worms) {
            if (w.time < now)
                w.move();

            int x = w.head[0], y = w.head[1];
            if (!inMap(x, y)) {
                dead.add(w);
                continue;
            }

            Worm c = map[x][y];
            map[x][y] = w;
            if (c == null || !worms.contains(c))
                continue;

            if (c.time < now && c.eaten == 0 && Arrays.equals(w.head, c.tail))
                continue;
            if (c.time == now && Arrays.equals(w.head, c.head)) {
                dead.add(c);
                dead.add(w);
            } else {
                if (c.time < now)
                    c.move();
                c.eaten += w.len;
                map[x][y] = c;
                dead.add(w);
            }
        }

        for (Worm w : dead)
            worms.remove(w);
    }

    public void join(int mTime, int mID, int mX, int mY, int mLength) {
        while (now < mTime)
            updateMap();

        Worm w = new Worm(mID, mTime, mLength, mX, mY);
        for (int i = mY; i < mY + mLength; i++)
            map[i][mX] = w;
        worms.add(w);
    }

    public Solution.RESULT top5(int mTime) {
        Solution.RESULT res = new Solution.RESULT();
        while (now < mTime)
            updateMap();
        TreeSet<Worm> tree = new TreeSet<>(worms);

        res.cnt = 0;
        for (Worm w : tree) {
            res.IDs[res.cnt++] = w.id;
            if (res.cnt == 5)
                break;
        }
        return res;
    }

    class Worm implements Comparable<Worm> {
        int id, len, eaten;
        int dir = 1;
        int time;
        int[] head = new int[2];
        int[] tail = new int[2];

        public Worm(int id, int time, int len, int col, int row) {
            this.id = id;
            this.time = time;
            this.len = len;
            this.head[0] = row;
            this.tail[0] = row + len - 1;
            this.head[1] = this.tail[1] = col;
        }

        public void move() {
            time++;
            head[0] += dx[dir];
            head[1] += dy[dir];

            if (eaten != 0) {
                len++;
                eaten--;
            } else {
                map[tail[0]][tail[1]] = null;
                int back = (dir + 3) % 4;
                tail[0] += dx[back];
                tail[1] += dy[back];
                if (head[0] == tail[0] || head[1] == tail[1])
                    dir = (dir + 1) % 4;
            }
        }

        @Override
        public int compareTo(Worm o) {
            if (this.len != o.len)
                return o.len - this.len;
            return o.id - this.id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }
}

class UserSolutionTLE {
    int N, tk;
    //    TreeSet<Worm> worms;
    HashSet<Integer>[][] grid;
    Map<Integer, Worm> wormMap;

    static class Worm implements Comparable<Worm> {
        int id, hx, hy, dir, len, time, pot, lenDir, hxTail, hyTail;

        @Override
        public int compareTo(Worm o) {
            if (len != o.len) return o.len - len;
            return o.id - id;
        }

        public Worm(int id, int hx, int hy, int len, int time) {
            this.id = id;
            this.hx = hx;
            this.hy = hy;
            this.dir = 0;
            this.len = len;
            this.time = time;
            this.pot = 0;
            this.lenDir = len - 1;
            this.hxTail = hx;
            this.hyTail = hy + len - 1;
        }
    }

    public void init(int N) {
        this.N = N;
        this.tk = 0;
//        this.worms = new TreeSet<>();
        this.grid = new HashSet[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.grid[i][j] = new HashSet<>();
            }
        }
        this.wormMap = new HashMap<>();
    }

    public void join(int mTime, int mID, int mX, int mY, int mLength) {
        update(mTime);
        Worm worm = new Worm(mID, mX, mY, mLength, mTime);
//        worms.add(worm);
        for (int i = mY; i < mY + mLength; i++) grid[mX][i].add(mID);
        wormMap.put(mID, worm);
    }

    public Solution.RESULT top5(int mTime) {
        update(mTime);
        Solution.RESULT res = new Solution.RESULT();
        res.cnt = 0;
        TreeSet<Worm> worms = new TreeSet<>(wormMap.values());
        Iterator<Worm> it = worms.iterator();
        for (int i = 0; i < 5; i++) {
            if (it.hasNext()) {
                res.IDs[i] = it.next().id;
                res.cnt = i + 1;
            } else break;
        }
        return res;
    }

    void update(int mTime) {
        for (; tk < mTime; tk++) {
            // 更新虫子状态
            for (Map.Entry<Integer, Worm> entry : wormMap.entrySet()) {
                Worm worm = entry.getValue();
//                worms.remove(worm);
                // 调整方向
                if (worm.lenDir == worm.len - 1) {
                    worm.dir = (worm.dir + 1) % 4;
                    worm.lenDir = 1;
                } else {
                    worm.lenDir += 1;
                }
                // 向前蠕动
                if (worm.pot == 0 && worm.hxTail >= 0 && worm.hxTail < N && worm.hyTail >= 0 && worm.hyTail < N) {
                    grid[worm.hxTail][worm.hyTail].remove(worm.id);
                }
                if (worm.dir == 0) {
                    worm.hy -= 1;
                    if (worm.pot == 0) {
                        worm.hxTail -= 1;
                    }
                } else if (worm.dir == 1) {
                    worm.hx += 1;
                    if (worm.pot == 0) {
                        worm.hyTail -= 1;
                    }
                } else if (worm.dir == 2) {
                    worm.hy += 1;
                    if (worm.pot == 0) {
                        worm.hxTail += 1;
                    }
                } else {
                    worm.hx -= 1;
                    if (worm.pot == 0) {
                        worm.hyTail += 1;
                    }
                }
                if (worm.hx >= 0 && worm.hx < N && worm.hy >= 0 && worm.hy < N) {
                    grid[worm.hx][worm.hy].add(worm.id);
                }

                // 调整长度
                if (worm.pot != 0) {
                    worm.pot -= 1;
                    worm.len += 1;
                }
//                worms.add(worm);
            }
            // 检查碰撞
            HashSet<Integer> rmWormIds = new HashSet<>();
            for (Map.Entry<Integer, Worm> entry : wormMap.entrySet()) {
                Worm worm = entry.getValue();
                if (worm.hx < 0 || worm.hx >= N || worm.hy < 0 || worm.hy >= N) {
                    rmWormIds.add(worm.id);
                } else {
                    Iterator<Integer> it = grid[worm.hx][worm.hy].iterator();
                    boolean flag = false;
                    while (it.hasNext()) {
                        Integer id = it.next();
                        if (worm.id != id) {
                            // 惰性更新地图
                            if (!wormMap.containsKey(id)) {
                                it.remove();
                                continue;
                            }
                            flag = true;
                            Worm coWorm = wormMap.get(id);
                            if (coWorm.hx == worm.hx && coWorm.hy == worm.hy &&
                                    ((coWorm.dir == 0 && worm.dir == 2) || (coWorm.dir == 1 && worm.dir == 3))) {
                                rmWormIds.add(coWorm.id);
                            } else {
//                                worms.remove(coWorm);
                                coWorm.pot += worm.len;
//                                worms.add(coWorm);
                            }
                        }
                    }
                    if (flag) rmWormIds.add(worm.id);
                }
            }
            // 删除虫子
            for (Integer id : rmWormIds) {
                Worm worm = wormMap.remove(id);
//                worms.remove(worm);
            }
        }
    }
}

public class Solution {
    private static final int CMD_INIT = 100;
    private static final int CMD_JOIN = 200;
    private static final int CMD_TOP5 = 300;

    private static UserSolution usersolution = new UserSolution();

    public static class RESULT {
        int cnt;
        int[] IDs = new int[5];

        RESULT() {
            cnt = -1;
        }
    }

    private static boolean run(BufferedReader br) throws Exception {
        int Q;
        int N, mTime, mID, mX, mY, mLength;
        int cnt, ans;


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
                case CMD_JOIN:
                    mTime = Integer.parseInt(st.nextToken());
                    mID = Integer.parseInt(st.nextToken());
                    mX = Integer.parseInt(st.nextToken());
                    mY = Integer.parseInt(st.nextToken());
                    mLength = Integer.parseInt(st.nextToken());
                    usersolution.join(mTime, mID, mX, mY, mLength);
                    break;
                case CMD_TOP5:
                    mTime = Integer.parseInt(st.nextToken());
                    RESULT ret = usersolution.top5(mTime);
                    cnt = Integer.parseInt(st.nextToken());
                    if (cnt != ret.cnt)
                        okay = false;
                    for (int i = 0; i < cnt; ++i) {
                        ans = Integer.parseInt(st.nextToken());
                        if (ans != ret.IDs[i])
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
        System.setIn(new java.io.FileInputStream("acm/pro2520t/sample_input.txt"));

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