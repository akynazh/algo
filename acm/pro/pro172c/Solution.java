package pro.pro172c;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class UserSolution {
    int N;
    List<Line> lineY;
    List<Line> lineX;
    List<Line> lineDia;

    static class Line {
        int id, x, y, v, t;

        public Line(int id, int x, int y, int type) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.t = type;
            if (type == 0) v = y;
            else if (type == 1) v = x;
            else v = y - x;
        }
    }

    List<Line> getTwoLines(int v, List<Line> arr) {
        List<Line> ret = new ArrayList<>();
        if (arr.isEmpty()) return new ArrayList<>();
        int l = 0, r = arr.size();
        while (l < r) {
            int m = l + (r - l) / 2;
            if (arr.get(m).v < v) l = m + 1;
            else r = m;
        }
        if (l < arr.size()) {
            ret.add(arr.get(l));
        }
        if (l != 0) {
            ret.add(arr.get(l - 1));
        }
        return ret;
    }

    public void init(int N, int mId[], int mType[], int mX[], int mY[]) {
        this.N = N;
        lineY = new ArrayList<>();
        lineX = new ArrayList<>();
        lineDia = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Line line = new Line(mId[i], mX[i], mY[i], mType[i]);
            if (mType[i] == 0) {
                lineX.add(line);
            } else if (mType[i] == 1) {
                lineY.add(line);
            } else {
                lineDia.add(line);
            }
        }
        lineX.add(new Line(0, 0, 200000001, 0));
        lineX.add(new Line(0, 0, -200000001, 0));
        lineY.add(new Line(0, 200000001, 0, 1));
        lineY.add(new Line(0, -200000001, 0, 1));
        lineDia.add(new Line(0, 0, 200000001, 2));
        lineDia.add(new Line(0, 0, -200000001, 2));
        lineX.sort(Comparator.comparingInt(l -> l.v));
        lineY.sort(Comparator.comparingInt(l -> l.v));
        lineDia.sort(Comparator.comparingInt(l -> l.v));
    }

    public int findPiece(int x, int y) {
        List<Line> lineXs = getTwoLines(y, lineX);
        List<Line> lineYs = getTwoLines(x, lineY);
        List<Line> lineDias = getTwoLines(y - x, lineDia);
        Line lineTop = lineXs.get(0), lineBottom = lineXs.get(1);
        Line lineLeft = lineYs.get(1), lineRight = lineYs.get(0);
        Line lineTopLeft = lineDias.get(0), lineBottomRight = lineDias.get(1);
        int ret = 0;
        if (lineTop.v - lineLeft.v > lineTopLeft.v) ret += lineTopLeft.id;
        if (lineTop.v - lineRight.v > lineBottomRight.v) ret += lineRight.id;
        if (lineTop.v - lineRight.v < lineTopLeft.v) ret += lineTop.id;
        if (lineBottom.v - lineRight.v < lineBottomRight.v) ret += lineBottomRight.id;
        if (lineBottom.v - lineLeft.v < lineTopLeft.v) ret += lineLeft.id;
        if (lineBottom.v - lineLeft.v > lineBottomRight.v) ret += lineBottom.id;
        return ret;
    }
}

class Solution {
    private final static int CMD_INIT = 1;
    private final static int CMD_FIND_PIECE = 2;

    private final static UserSolution usersolution = new UserSolution();

    private static int[] mId = new int[100000];
    private static int[] mType = new int[100000];
    private static int[] mX = new int[100000];
    private static int[] mY = new int[100000];

    private static boolean run(BufferedReader br) throws Exception {
        StringTokenizer st;

        int numQuery;
        int N, mInteriorX, mInteriorY;
        int userAns, ans;

        boolean isCorrect = false;

        numQuery = Integer.parseInt(br.readLine());

        for (int q = 0; q < numQuery; q++) {
            st = new StringTokenizer(br.readLine(), " ");

            int cmd;
            cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case CMD_INIT:
                    N = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < N; i++)
                        mId[i] = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < N; i++)
                        mType[i] = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < N; i++)
                        mX[i] = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < N; i++)
                        mY[i] = Integer.parseInt(st.nextToken());
                    usersolution.init(N, mId, mType, mX, mY);
                    isCorrect = true;
                    break;
                case CMD_FIND_PIECE:
                    mInteriorX = Integer.parseInt(st.nextToken());
                    mInteriorY = Integer.parseInt(st.nextToken());
                    userAns = usersolution.findPiece(mInteriorX, mInteriorY);
                    ans = Integer.parseInt(st.nextToken());
                    if (userAns != ans) {
                        isCorrect = false;
                    }
                    break;
                default:
                    isCorrect = false;
                    break;
            }
        }
        return isCorrect;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

        System.setIn(new java.io.FileInputStream("acm/pro172c/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        T = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}