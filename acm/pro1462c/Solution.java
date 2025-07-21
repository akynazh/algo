package pro1462c;

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
        System.setIn(new java.io.FileInputStream("acm/samples/pro1462c/input"));

        Scanner sc = new Scanner(System.in);

        T = sc.nextInt();
        MARK = sc.nextInt();

        for (int tc = 1; tc <= T; tc++) {
            long s = System.currentTimeMillis();
            int score = run(sc) ? MARK : 0;
            long e = System.currentTimeMillis();
            System.out.println("#" + tc + " " + score + " " + (e - s));
        }
        sc.close();
    }
}

class UserSolution {
    final int MAX_L = 500;
    final int MAX_N = MAX_L * 30;
    final int MAX_QUANTITY = 100;
    int L;
    int N;
    Map<Integer, Camp> camps; // campId - camp
    Map<Integer, Box> boxes; // campId(parentId) - box
    int blockSize;
    Map<String, Set<Integer>> blockMap;

//    int[][] island; // 0 <= i < N

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
        this.blockSize = L + 1;
        this.blockMap = new HashMap<>();

//        this.island = new int[MAX_N][MAX_N];
    }

    String getBlockKey(int row, int col) {
//        long br = row / blockSize;
//        long bc = col / blockSize;
//        return (br << 32) | bc;
        return String.valueOf(row) + col;
    }

    int addBaseCamp(int mID, int mRow, int mCol, int mQuantity) {
        Camp newCamp = new Camp(mID, mRow, mCol, mQuantity);
        camps.put(mID, newCamp);
        Set<Integer> campParentIds = new HashSet<>();
//        island[mRow][mCol] = mID;

//        for (int i = -L; i <= L; i++) {
//            for (int j = -L; j <= L; j++) {
//                if (Math.abs(i) + Math.abs(j) > L || (i == 0 && j == 0)) {
//                    continue;
//                }
//                int row = mRow + i, col = mCol + j;
//                if (0 <= row && row < N && 0 <= col && col < N && island[row][col] != 0) {
//                    campParentIds.add(find(island[row][col]));
//                }
//            }
//        }

        int minBlockRow = (mRow - L) / blockSize;
        int maxBlockRow = (mRow + L) / blockSize;
        int minBlockCol = (mCol - L) / blockSize;
        int maxBlockCol = (mCol + L) / blockSize;

        for (int br = minBlockRow; br <= maxBlockRow; br++) {
            for (int bc = minBlockCol; bc <= maxBlockCol; bc++) {
                String bKey = getBlockKey(br, bc);
                Set<Integer> nearby = blockMap.get(bKey);
                if (nearby == null) continue;
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