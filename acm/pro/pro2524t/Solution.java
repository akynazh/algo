package pro.pro2524t;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    GroupManager manager;
    Server[] servers;
    Map<Integer, User> users;

    public void init(int N) {
        users = new HashMap<>();
        manager = new GroupManager();
        servers = new Server[N];
        for (int id = 0; id < servers.length; id++) {
            servers[id] = new Server(id);
        }
    }

    public int tryAccess(int K, int[] mIDs) {
        Server server = servers[0];
        for (int id = 1; id < servers.length; id++) {
            if (servers[id].size < server.size) {
                server = servers[id];
            }
        }

        Group group = new Group();
        group.server = server;
        for (int i = 0; i < K; i++) {
            User user = new User(mIDs[i]);
            users.put(mIDs[i], user);
            user.group = group;
            group.add(user);
        }
        server.groups.add(group);
        manager.add(group);
        group.size = K;
        server.size += K;

        return server.id;
    }

    public void deAccess(int K, int[] mIDs) {
        for (int i = 0; i < K; i++) {
            User user = users.remove(mIDs[i]);
            Group group = user.group;
            Server server = group.server;
            group.size -= 1;
            server.size -= 1;
            user.prev.next = user.next;
            user.next.prev = user.prev;
            if (group.size == 0) {
                server.groups.remove(group);
                manager.remove(group);
            }
        }
    }

    public void logIn(int mCnt) {
        while (mCnt > 0) {
            Group group = manager.head.next;
            int count = Math.min(mCnt, group.size);
            mCnt -= count;
            group.size -= count;
            group.server.size -= count;
            User user = group.head;
            for (int i = 0; i < count; i++) {
                user = user.next;
                users.remove(user.id);
            }
            group.head.next = user.next;
            user.next.prev = group.head;
            if (group.size == 0) {
                group.server.groups.remove(group);
                manager.remove(group);
            }
        }
    }

    public int reOrder(int mServerID) {
        Server server = servers[mServerID - 1];
        for (Group group : server.groups) {
            manager.remove(group);
            manager.add(group);
        }
        return server.size;
    }

    public int waitOrder(int mID) {
        User target = users.get(mID);
        if (target == null)
            return 0;
        int result = 1;
        for (Group group = manager.head.next; true; group = group.next) {
            if (group != target.group) {
                result += group.size;
                continue;
            }
            for (User user = group.head.next; user != target; user = user.next) {
                result += 1;
            }
            return result;
        }
    }
}

class User {
    int id;
    User next, prev;
    Group group;

    User(int id) {
        this.id = id;
    }

    User() {
    }
}

class Group {
    User head, tail;
    Group next, prev;
    Server server;
    int size;

    Group() {
        head = new User();
        tail = new User();
        head.next = tail;
        tail.prev = head;
    }

    void add(User user) {
        user.next = tail;
        user.prev = tail.prev;
        user.prev.next = user;
        user.next.prev = user;
    }
}

class Server {
    List<Group> groups;
    int size, id;

    Server(int id) {
        this.groups = new ArrayList<Group>();
        this.id = id + 1;
    }
}

class GroupManager {
    Group head, tail;

    GroupManager() {
        head = new Group();
        tail = new Group();
        head.next = tail;
        tail.prev = head;
    }

    void add(Group group) {
        group.next = tail;
        group.prev = tail.prev;
        group.next.prev = group;
        group.prev.next = group;
    }

    void remove(Group group) {
        group.next.prev = group.prev;
        group.prev.next = group.next;
    }
}

class UserSolutionFail {
    int N;
    Queue<Integer> users;
    Queue<Integer>[] servers;
    Map<Integer, Integer> userServerMap;

    void init(int N) {
        this.N = N;
        this.users = new ArrayDeque<>();
        this.servers = new ArrayDeque[N + 1];
        for (int i = 1; i <= N; i++) {
            this.servers[i] = new ArrayDeque<>();
        }
        this.userServerMap = new HashMap<>();
    }

    int tryAccess(int K, int mIDs[]) {
        int id = 1, c = Integer.MAX_VALUE;
        for (int i = 1; i <= N; i++) {
            if (servers[i].size() < c) {
                id = i;
                c = servers[i].size();
            }
        }
        for (int i = 0; i < K; i++) {
            servers[id].offer(mIDs[i]);
            users.offer(mIDs[i]);
            userServerMap.put(mIDs[i], id);
        }
        return id;
    }

    void deAccess(int K, int mIDs[]) {
        for (int i = 0; i < K; i++) {
            Integer sid = userServerMap.remove(mIDs[i]);
            servers[sid].remove(mIDs[i]);
            users.remove(mIDs[i]);
        }
    }

    void logIn(int mCnt) {
        for (int i = 0; i < mCnt; i++) {
            Integer uid = users.poll();
            Integer sid = userServerMap.remove(uid);
            servers[sid].remove(uid);
        }
    }

    int reOrder(int mServerID) {
        int s = servers[mServerID].size();
        int[] ids = new int[s];
        for (int i = 0; i < s; i++) {
            Integer uid = servers[mServerID].poll();
            users.remove(uid);
            ids[i] = uid;
            userServerMap.remove(uid);
        }
        tryAccess(s, ids);
        return servers[mServerID].size();
    }

    int waitOrder(int mID) {
        if (!userServerMap.containsKey(mID)) return 0;
        int i = 1;
        for (Integer uid : users) {
            if (uid == mID) {
                return i;
            }
            i++;
        }
        return 0;
    }
}

public class Solution {

    private static UserSolution usersolution = new UserSolution();

    private final static int CMD_INIT = 100;
    private final static int CMD_TRYACCESS = 200;
    private final static int CMD_DEACCESS = 300;
    private final static int CMD_LOGIN = 400;
    private final static int CMD_REORDER = 500;
    private final static int CMD_WAITORDER = 600;

    private static int mIDs[] = new int[30];

    private static boolean run(BufferedReader br) throws Exception {
        int N, K, ID, cnt, ret, ans;
        boolean ok = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int Q = Integer.parseInt(st.nextToken());
        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                N = Integer.parseInt(st.nextToken());
                usersolution.init(N);
                ok = true;
            } else if (cmd == CMD_TRYACCESS) {
                K = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                for (int i = 0; i < K; i++) {
                    mIDs[i] = Integer.parseInt(st.nextToken());
                }
                ret = usersolution.tryAccess(K, mIDs);
                if (ret != ans) {
                    ok = false;
                }
            } else if (cmd == CMD_DEACCESS) {
                K = Integer.parseInt(st.nextToken());
                for (int i = 0; i < K; i++) {
                    mIDs[i] = Integer.parseInt(st.nextToken());
                }
                usersolution.deAccess(K, mIDs);
            } else if (cmd == CMD_LOGIN) {
                cnt = Integer.parseInt(st.nextToken());
                usersolution.logIn(cnt);
            } else if (cmd == CMD_REORDER) {
                ID = Integer.parseInt(st.nextToken());
                ret = usersolution.reOrder(ID);
                ans = Integer.parseInt(st.nextToken());
                if (ret != ans) {
                    ok = false;
                }
            } else if (cmd == CMD_WAITORDER) {
                ID = Integer.parseInt(st.nextToken());
                ret = usersolution.waitOrder(ID);
                ans = Integer.parseInt(st.nextToken());
                if (ret != ans) {
                    ok = false;
                }
            } else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("acm/pro2524t/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line = new StringTokenizer(br.readLine(), " ");

        int T = Integer.parseInt(line.nextToken());
        int MARK = Integer.parseInt(line.nextToken());

        long s = System.currentTimeMillis();
        for (int tc = 1; tc <= T; tc++) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        System.out.println(System.currentTimeMillis() - s);

        br.close();
    }
}
