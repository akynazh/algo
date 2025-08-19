package pro171c;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    static class Product {
        int pId;
        int mLine;
        int mTimeProcess;
        int equipId;
        int status;
        int endTime;

        public Product(int pId, int mLine, int mTime, int equipId) {
            this.pId = pId;
            this.mLine = mLine;
            this.mTimeProcess = mTime;
            this.equipId = equipId;
            status = 1;
        }
    }

    static class Line {
        int idLine;
        int idProduct;
        boolean producing;
        LinkedList<Product> listProduct;

        public Line(int idLine) {
            this.idLine = idLine;
            listProduct = new LinkedList<>();
        }

    }

    Line[] listLine;
    boolean[] isUsedEquipment;
    HashMap<Integer, Product> hashProduct;
    PriorityQueue<Product> heapProduction;
    int L, M;

    void init(int L, int M) {

        this.L = L;
        this.M = M;
        listLine = new Line[L];
        for (int i = 0; i < L; i++) {
            listLine[i] = new Line(i);
        }
        isUsedEquipment = new boolean[M];
        hashProduct = new HashMap<>();
        heapProduction = new PriorityQueue<>((o1, o2) -> {
            if (o1.endTime == o2.endTime) {
                return o1.mLine - o2.mLine;
            }
            return o1.endTime - o2.endTime;
        });
    }

    public void syncTime(int time) {
        while (!heapProduction.isEmpty() && heapProduction.peek().endTime <= time) {
            Product product = heapProduction.peek();
            int curTime = product.endTime;
            // 根据某条生产线完成的时间来进行更新
            while (!heapProduction.isEmpty() && heapProduction.peek().endTime == curTime) {
                product = heapProduction.poll();
                product.status = 3;
                listLine[product.mLine].producing = false;
                isUsedEquipment[product.equipId] = false;
            }
            // 上次提交的时间 <= 现在任何生产线完成生产的时间
            for (int i = 0; i < L; i++) {
                updateLine(listLine[i], curTime);
            }
        }
    }

    public void updateLine(Line line, int end) {
        if (!line.listProduct.isEmpty() && !line.producing && !isUsedEquipment[line.listProduct.peek().equipId]) {
            Product product = line.listProduct.poll();
            product.endTime = product.mTimeProcess + end;
            product.status = 2;
            heapProduction.add(product);
            line.producing = true;
            isUsedEquipment[product.equipId] = true;
            line.idProduct = product.pId;
        }
    }

    int request(int tStamp, int pId, int mLine, int eId, int mTime) {
        // 每次提交前，更新所有生产线的状态
        syncTime(tStamp - 1);
        Product product = new Product(pId, mLine, mTime, eId);
        hashProduct.put(pId, product);
        Line line = listLine[mLine];
        line.listProduct.add(product);
        syncTime(tStamp);
        updateLine(line, tStamp); // tStamp 下没有事件完成
        return line.producing ? line.idProduct : -1;
    }

    int status(int tStamp, int pId) {
        Product product = hashProduct.get(pId);
        if (product == null)
            return 0;
        syncTime(tStamp);
        return product.status;
    }
}

class UserSolutionMine {
    int L, M, tk;
    Map<Integer, Product> products;
    Queue<Product>[] lines;
    boolean[] equipment;

    static class Product {
        int reqTime, id, line, eId, time, endTime;
        boolean waiting, finished;

        public Product(int reqTime, int id, int line, int eId, int time) {
            this.reqTime = reqTime;
            this.id = id;
            this.line = line;
            this.eId = eId;
            this.time = time;
            this.waiting = true;
        }
    }

    void init(int L, int M) {
        this.L = L;
        this.M = M;
        this.tk = 1;
        this.products = new HashMap<>();
        this.lines = new Queue[L];
        for (int i = 0; i < L; i++) {
            this.lines[i] = new ArrayDeque();
        }
        this.equipment = new boolean[M];
        Arrays.fill(this.equipment, true);
    }

    int request(int tStamp, int pId, int mLine, int eId, int mTime) {
        Product product = new Product(tStamp, pId, mLine, eId, mTime);
        products.put(pId, product);
        lines[mLine].offer(product);
        update(tStamp);
        Product pk = lines[mLine].peek();
        return pk.waiting ? -1 : pk.id;
    }

    int status(int tStamp, int pId) {
        update(tStamp);
        if (!products.containsKey(pId)) return 0;
        Product p = products.get(pId);
        if (p.finished) return 3;
        if (p.waiting) return 1;
        return 2;
    }

    void update(int time) {
        for (; tk <= time; tk++) {
            // release
            for (int i = 0; i < L; i++) {
                Queue<Product> line = lines[i];
                if (line.isEmpty()) continue;
                Product pk = line.peek();
                if (!pk.waiting && pk.endTime == tk) {
                    Product po = line.poll();
                    po.finished = true;
                    equipment[po.eId] = true;
                }
            }
            // idle
            for (int i = 0; i < L; i++) {
                Queue<Product> line = lines[i];
                if (line.isEmpty() || !line.peek().waiting) continue;
                Product pk = line.peek();
                if (equipment[pk.eId] && pk.reqTime <= tk) {
                    equipment[pk.eId] = false;
                    pk.waiting = false;
                    pk.endTime = tk + pk.time;
                }
            }
        }
    }
}

class Main {
    private final static int CMD_INIT = 1;
    private final static int CMD_REQUEST = 2;
    private final static int CMD_STATUS = 3;

    private final static UserSolution usersolution = new UserSolution();

    private static boolean run(BufferedReader br) throws Exception {
        int q = Integer.parseInt(br.readLine());

        int l, m, timestamp, pid, mline, eid, mtime;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());
            switch (cmd) {
                case CMD_INIT:
                    l = Integer.parseInt(st.nextToken());
                    m = Integer.parseInt(st.nextToken());
                    usersolution.init(l, m);
                    okay = true;
                    break;
                case CMD_REQUEST:
                    timestamp = Integer.parseInt(st.nextToken());
                    pid = Integer.parseInt(st.nextToken());
                    mline = Integer.parseInt(st.nextToken());
                    eid = Integer.parseInt(st.nextToken());
                    mtime = Integer.parseInt(st.nextToken());
                    ans = Integer.parseInt(st.nextToken());
                    ret = usersolution.request(timestamp, pid, mline, eid, mtime);
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_STATUS:
                    timestamp = Integer.parseInt(st.nextToken());
                    pid = Integer.parseInt(st.nextToken());
                    ans = Integer.parseInt(st.nextToken());
                    ret = usersolution.status(timestamp, pid);
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
        int TC, MARK;

        System.setIn(new java.io.FileInputStream("acm/pro171c/test25.in"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        TC = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        long s = System.currentTimeMillis();
        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }
        System.out.println(System.currentTimeMillis() - s);

        br.close();
    }
}