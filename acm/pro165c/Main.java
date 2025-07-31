package pro165c;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class Main
{
    private final static int CMD_INIT       = 100;
    private final static int CMD_INSERT     = 200;
    private final static int CMD_MOVECURSOR = 300;
    private final static int CMD_COUNT      = 400;

    private final static UserSolution usersolution = new UserSolution();

    private static void String2Char(char[] buf, String str, int maxLen)
    {
        for (int k = 0; k < str.length(); k++)
            buf[k] = str.charAt(k);

        for (int k = str.length(); k <= maxLen; k++)
            buf[k] = '\0';
    }

    private static char[] mStr = new char[90001];

    private static boolean run(BufferedReader br) throws Exception
    {
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int queryCnt = Integer.parseInt(st.nextToken());
        boolean correct = false;

        for (int q = 0; q < queryCnt; q++)
        {
            st = new StringTokenizer(br.readLine(), " ");

            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT)
            {
                int H = Integer.parseInt(st.nextToken());
                int W = Integer.parseInt(st.nextToken());

                String2Char(mStr, st.nextToken(), 90000);

                usersolution.init(H, W, mStr);
                correct = true;
            }
            else if (cmd == CMD_INSERT)
            {
                char mChar = st.nextToken().charAt(0);

                usersolution.insert(mChar);
            }
            else if (cmd == CMD_MOVECURSOR)
            {
                int mRow = Integer.parseInt(st.nextToken());
                int mCol = Integer.parseInt(st.nextToken());

                char ret = usersolution.moveCursor(mRow, mCol);

                char ans = st.nextToken().charAt(0);
                if (ret != ans)
                {
                    correct = false;
                }
            }
            else if (cmd == CMD_COUNT)
            {
                char mChar = st.nextToken().charAt(0);

                int ret = usersolution.countCharacter(mChar);

                int ans = Integer.parseInt(st.nextToken());

                if (ret != ans)
                {
                    correct = false;
                }
            }
        }
        return correct;
    }

    public static void main(String[] args) throws Exception
    {
        int TC, MARK;

        System.setIn(new java.io.FileInputStream("acm/pro165c/sample.in"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        TC = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        long s = System.currentTimeMillis();
        for (int testcase = 1; testcase <= TC; ++testcase)
        {
            long a = System.currentTimeMillis();
            int score = run(br) ? MARK : 0;
            long b = System.currentTimeMillis();

            System.out.println("#" + testcase + " " + score + " " + (b - a));
        }
        System.out.println(System.currentTimeMillis() - s);

        br.close();
    }
}

class UserSolution1
{
    int H;
    int W;
    LinkedList<Character> pad;
    int[] cursor;
    boolean end;

    void init(int H, int W, char mStr[])
    {
        this.H = H;
        this.W = W;
        this.pad = new LinkedList<>();
        this.cursor = new int[]{1, 1};
        this.end = false;
        for (char c : mStr) {
            if (c == '\0') {
                break;
            }
            pad.addLast(c);
        }
    }

    void insert(char mChar)
    {
        if (end) {
            pad.addLast(mChar);
        } else {
            int idx = (cursor[0] - 1) * W + cursor[1] - 1;
            pad.add(idx, mChar);

            if (cursor[1] + 1 <= W) {
                cursor[1] += 1;
            } else {
                cursor[1] = 1;
                cursor[0] += 1;
            }
        }
    }

    char moveCursor(int mRow, int mCol)
    {
        cursor[0] = mRow;
        cursor[1] = mCol;
        int idx = (cursor[0] - 1) * W + cursor[1] - 1;
        if (idx >= pad.size()) {
            end = true;
            return '$';
        }
        end = false;
        return pad.get(idx);
    }

    int countCharacter(char mChar)
    {
        int cnt = 0;
        int idx = (cursor[0] - 1) * W + cursor[1] - 1;
        if (end || idx < 0 || idx > pad.size()) {
            return 0;
        }
        ListIterator<Character> it = pad.listIterator(idx);
        while(it.hasNext()) {
            if (it.next() == mChar) cnt++;
        }
        return cnt;
    }
}

class UserSolution {
    int H;
    int W;
    char[] buffer;      // 用char数组模拟文本缓冲区
    int size;           // 当前缓冲区中字符数量
    int capacity;       // 缓冲区最大容量
    int[] cursor;       // 光标位置，格式是[row, col]
    boolean end;        // 是否位于末尾之后

    void init(int H, int W, char mStr[]) {
        this.H = H;
        this.W = W;
        this.cursor = new int[]{1, 1};
        this.end = false;

        this.capacity = H * W;
        this.buffer = new char[capacity];
        this.size = 0;

        for (char c : mStr) {
            if (c == '\0') break;
            buffer[size++] = c;
        }
    }

    void insert(char mChar) {
        int idx = (cursor[0] - 1) * W + cursor[1] - 1;

        if (end || idx >= size) {
            // 在末尾插入
            buffer[size++] = mChar;
        } else {
            // 在中间或开头插入，整体后移
            for (int i = size; i > idx; i--) {
                buffer[i] = buffer[i - 1];
            }
            buffer[idx] = mChar;
            size++;
        }

        // 更新光标位置
        if (cursor[1] + 1 <= W) {
            cursor[1] += 1;
        } else {
            cursor[1] = 1;
            cursor[0] += 1;
        }

    }

    char moveCursor(int mRow, int mCol) {
        cursor[0] = mRow;
        cursor[1] = mCol;

        int idx = (cursor[0] - 1) * W + cursor[1] - 1;

        if (idx >= size) {
            end = true;
            return '$';
        } else {
            end = false;
            return buffer[idx];
        }
    }

    int countCharacter(char mChar) {
        int idx = (cursor[0] - 1) * W + cursor[1] - 1;
        if (end || idx < 0 || idx >= size) {
            return 0;
        }

        int cnt = 0;
        for (int i = idx; i < size; i++) {
            if (buffer[i] == mChar) {
                cnt++;
            }
        }
        return cnt;
    }
}