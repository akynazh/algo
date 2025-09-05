package pro.pro167c;

import java.util.*;

class Solution {

    private final static int CMD_INIT = 1;
    private final static int CMD_ADD = 2;
    private final static int CMD_DROP = 3;

    private final static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) throws Exception {

        int query_num = sc.nextInt();
        boolean ok = false;

        for (int q = 0; q < query_num; q++) {
            int query = sc.nextInt();

            if (query == CMD_INIT) {
                int L = sc.nextInt();
                int N = sc.nextInt();
                usersolution.init(L, N);
                ok = true;
            } else if (query == CMD_ADD) {
                int mID = sc.nextInt();
                int mRow = sc.nextInt();
                int mCol = sc.nextInt();
                int mQuantity = sc.nextInt();
                int ret = usersolution.addBaseCamp(mID, mRow, mCol, mQuantity);
                int ans = sc.nextInt();
                if (ans != ret) {
                    ok = false;
                }
            } else if (query == CMD_DROP) {
                int K = sc.nextInt();
                int ret = usersolution.findBaseCampForDropping(K);
                int ans = sc.nextInt();
                if (ans != ret) {
                    ok = false;
                }
            }
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;
        System.setIn(new java.io.FileInputStream("acm/pro167c/input"));

        Scanner sc = new Scanner(System.in);

        T = sc.nextInt();
        MARK = sc.nextInt();

        long s = System.currentTimeMillis();
        for (int tc = 1; tc <= T; tc++) {
            long a = System.currentTimeMillis();
            int score = run(sc) ? MARK : 0;
            long b = System.currentTimeMillis();
            System.out.println("#" + tc + " " + score + " " + (b - a));
        }
        System.out.println(System.currentTimeMillis() - s);
        sc.close();
    }
}

class UserSolution {
    int L, N, blockSize;
    Map<Integer, Camp> camps; // campId - camp
    Map<Integer, Box> boxes; // campId(parentId) - box
    Map<String, Set<Integer>> blockMap;

    static class Camp implements Comparable<Camp> {
        int id;
        int row;
        int col;
        int quan;
        int parent;

        Camp(int id, int row, int col, int quan) {
            this.id = id;
            this.row = row;
            this.col = col;
            this.quan = quan;
            this.parent = id;
        }

        @Override
        public int compareTo(Camp o) {
            if (this.quan != o.quan) return this.quan - o.quan;
            if (this.row != o.row) return this.row - o.row;
            return this.col - o.col;
        }
    }

    static class Box {
        int parentId;
        int total;
        Camp bestCamp;

        Box(int parentId, int total, Camp bestCamp) {
            this.parentId = parentId;
            this.total = total;
            this.bestCamp = bestCamp;
        }
    }

    int find(int id) {
        Camp camp = camps.get(id);
        if (camp.parent == id) {
            return id;
        }
        camp.parent = find(camp.parent);
        return camp.parent;
    }

    void union(int id1, int id2) {
        int pid1 = find(id1);
        int pid2 = find(id2);
        if (pid1 != pid2) {
            camps.get(pid2).parent = pid1;
        }
    }

    void init(int L, int N) {
        this.L = L;
        this.N = N;
        this.camps = new HashMap<>();
        this.boxes = new HashMap<>();
        this.blockSize = L;
        this.blockMap = new HashMap<>();
    }

    String getBlockKey(int row, int col) {
        return String.valueOf(row) + col;
    }

    int addBaseCamp(int mID, int mRow, int mCol, int mQuantity) {
        Camp newCamp = new Camp(mID, mRow, mCol, mQuantity);
        camps.put(mID, newCamp);
        Set<Integer> campParentIds = new HashSet<>();
        int minBlockRow = mRow - L < 0 ? 0 : (mRow - L) / blockSize;
        int maxBlockRow = mRow + L > N ? (N - 1) / blockSize : (mRow + L) / blockSize;
        int minBlockCol = mCol - L < 0 ? 0 : (mCol - L) / blockSize;
        int maxBlockCol = mCol + L > N ? (N - 1) / blockSize : (mCol + L) / blockSize;

        for (int br = minBlockRow; br <= maxBlockRow; br++) {
            for (int bc = minBlockCol; bc <= maxBlockCol; bc++) {
                String bKey = getBlockKey(br, bc);
                if (!blockMap.containsKey(bKey)) continue;
                Set<Integer> nearby = blockMap.get(bKey);
                for (int nearID : nearby) {
                    Camp other = camps.get(nearID);
                    int dist = Math.abs(other.row - mRow) + Math.abs(other.col - mCol);
                    if (dist <= L && dist != 0) {
                        campParentIds.add(find(nearID));
                    }
                }
            }
        }

        if (campParentIds.isEmpty()) {
            Box box = new Box(mID, mQuantity, newCamp);
            boxes.put(mID, box);
        } else {
            int parentId = campParentIds.iterator().next();
            Box parentBox = boxes.get(parentId);
            if (parentBox.bestCamp.compareTo(newCamp) > 0) {
                parentBox.bestCamp = newCamp;
            }
            parentBox.total += mQuantity;
            union(parentId, mID);
            campParentIds.remove(parentId);
            for (Integer id : campParentIds) {
                Box box = boxes.get(id);
                parentBox.total += box.total;
                if (parentBox.bestCamp.compareTo(box.bestCamp) > 0) {
                    parentBox.bestCamp = box.bestCamp;
                }
                union(parentId, id);
                boxes.remove(id);
            }
        }

        String key = getBlockKey(mRow / blockSize, mCol / blockSize);
        blockMap.computeIfAbsent(key, k -> new HashSet<>()).add(mID);

        return boxes.get(find(mID)).total;
    }

    int findBaseCampForDropping(int K) {
        Camp bestCamp = null;
        for (Map.Entry<Integer, Box> entry : boxes.entrySet()) {
            Box box = entry.getValue();
            if (box.total < K) {
                continue;
            }
            if (bestCamp == null || bestCamp.compareTo(box.bestCamp) > 0) {
                bestCamp = box.bestCamp;
            }
        }
        return bestCamp == null ? -1 : bestCamp.id;
    }
}

class UserSolutionBetter {

    class BaseCamp implements Comparable<BaseCamp> {
        int id;
        int row;
        int col;
        int quantity;
        int total_quantity;
        BaseCamp root;
        public BaseCamp(int id, int row, int col, int quantity) {
            this.id = id;
            this.row = row;
            this.col = col;
            this.quantity = quantity;
            total_quantity = quantity;
            root = null;
        }
        @Override
        public int compareTo(BaseCamp o) {
            if (quantity == o.quantity) {
                if (row == o.row)
                    return col - o.col;
                return row - o.row;
            }
            return quantity - o.quantity;
        }

        // check row, col in the basecamp
        public boolean isGroup (int r, int c) {
            if (Math.abs(row - r) + Math.abs(col - c) <= l) return true;
            return false;
        }

        // find root
        public BaseCamp findRoot() {
            if (this.root == null) return this;
            BaseCamp cur = root;
            while (cur.root != null)
                cur = cur.root;
            return cur;
        }
    }

    int l, n;
    ArrayList<BaseCamp>[][] list_base_camps;
    TreeSet<BaseCamp> all_base_camps;

    void init (int L, int N) {
        l = L;
        n = N;
        int size = N / l + 1;
        list_base_camps = new ArrayList[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                list_base_camps[i][j] = new ArrayList<>();
            }
        }
        all_base_camps = new TreeSet<>();
    }

    int addBaseCamp (int mID, int mRow, int mCol, int mQuantity) {
        // create new base camp
        BaseCamp bc = new BaseCamp(mID, mRow, mCol, mQuantity);
        BaseCamp cur = bc;
        //list_base_camps[mRow / l][mCol / l].add(root);
        int id = mID;
        // find all base camp which can group
        int sr = (mRow - l) < 0 ? 0 : (mRow - l) / l;
        int er = (mRow + l) > n ? (n - 1) / l : (mRow + l) / l;
        int sc = (mCol - l) < 0 ? 0 : (mCol - l) / l;
        int ec = (mCol + l) > n ? (n - 1) / l : (mCol + l) / l;
        for (int i = sr; i <= er; i++) {
            for (int j = sc; j <= ec; j++) {
                for (BaseCamp b : list_base_camps[i][j]) {
                    if (!b.isGroup(mRow, mCol)) continue;
                    // if b and camp in a group -> find root of b
                    BaseCamp tmp = b.findRoot();
                    if (tmp.id == id) continue;
                    all_base_camps.remove(tmp);
                    // choose root between tmp and root -> prio: row small -> col small
                    if ((cur.quantity < tmp.quantity) || (cur.quantity == tmp.quantity && cur.row < tmp.row) ||
                            (cur.quantity == tmp.quantity && cur.row == tmp.row && cur.col < tmp.col)) {
                        // keep root: tmp is child
                        id = cur.id;
                        cur.total_quantity += tmp.total_quantity;
                        tmp.root = cur;
                    } else { // pick tmp is root, root is child
                        id = tmp.id;
                        tmp.total_quantity += cur.total_quantity;
                        cur.root = tmp;
                        cur = tmp;
                    }
                }
            }
        }
        list_base_camps[mRow / l][mCol / l].add(bc);
        all_base_camps.add(cur);
        return cur.total_quantity;
    }

    int findBaseCampForDropping (int K) {
        for (BaseCamp bc : all_base_camps) {
            if (bc.total_quantity >= K)
                return bc.id;
        }
        return -1;
    }
}