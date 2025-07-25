package pro168c;

import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeSet;

class UserSolution
{
    TreeSet<String>[] alps;


    public void init(int N, String[] mWordList)
    {
        alps = new TreeSet[26];
        for (int i = 0; i < 26; i++) {
            alps[i] = new TreeSet<>();
        }
        for (int i = 0; i < N; i++) {
            String s = mWordList[i];
            int k = s.charAt(0) - 'a';
            alps[k].add(s);
        }
    }

    public int add(String mWord)
    {
        int i = mWord.charAt(0) - 'a';
        if (alps[i].contains(mWord)) {
            return 0;
        }
        alps[i].add(mWord);
        return 1;
    }

    public int erase(String mWord)
    {
        int i = mWord.charAt(0) - 'a';
        if (alps[i].contains(mWord)) {
            alps[i].remove(mWord);
            return 1;
        }
        return 0;
    }

    public Main.RESULT find(char mInitial, int mIndex)
    {
        Main.RESULT res = new Main.RESULT();
        int i = mInitial - 'a';
        if (alps[i].size() < mIndex) {
            res.success = 0;
        } else {
            res.success = 1;
            Iterator<String> it = alps[i].iterator();
            String e = null;
            for (int k = 0; k < mIndex; k++) {
                e = it.next();
            }
            res.word = e;
        }
        return res;
    }

    public int getIndex(String mWord)
    {
        int i = mWord.charAt(0) - 'a';
        if (alps[i].contains(mWord)) {
            int x = 0;
            for (int j = 0; j < i; j++) {
                x += alps[j].size();
            }
            Iterator<String> it = alps[i].iterator();
            boolean y = true;
            while (y) {
                String next = it.next();
                if (Objects.equals(next, mWord)) {
                    y = false;
                }
                x += 1;
            }
            return x;
        }
        return 0;
    }
}

public class Main
{
    private static final int CMD_INIT			= 100;
    private static final int CMD_ADD			= 200;
    private static final int CMD_ERASE			= 300;
    private static final int CMD_FIND			= 400;
    private static final int CMD_GET_INDEX		= 500;

    private static final int MAX_N				= 30000;

    public static final class RESULT
    {
        int success;
        String word;

        RESULT()
        {
            success = -1;
            word = null;
        }
    }

    private static UserSolution usersolution = new UserSolution();
    private static String[] mWordList = new String[MAX_N];

    private static boolean run(Scanner sc) throws Exception
    {
        int Q, N, mIndex;
        int ret = -1, ans;

        RESULT res;

        String mWord;

        Q = sc.nextInt();

        boolean okay = false;

        for (int q = 0; q < Q; ++q)
        {
            int cmd = sc.nextInt();

            switch(cmd)
            {
                case CMD_INIT:
                    N = sc.nextInt();
                    for (int i = 0; i < N; ++i)
                        mWordList[i] = sc.next();
                    usersolution.init(N, mWordList);
                    okay = true;
                    break;
                case CMD_ADD:
                    mWord = sc.next();
                    ret = usersolution.add(mWord);
                    ans = sc.nextInt();
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_ERASE:
                    mWord = sc.next();
                    ret = usersolution.erase(mWord);
                    ans = sc.nextInt();
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_FIND:
                    mWord = sc.next();
                    mIndex = sc.nextInt();
                    res = usersolution.find(mWord.charAt(0), mIndex);
                    ans = sc.nextInt();
                    if (res.success != ans)
                        okay = false;
                    if (ans == 1)
                    {
                        mWord = sc.next();
                        if (!mWord.equals(res.word))
                            okay = false;
                    }
                    break;
                case CMD_GET_INDEX:
                    mWord = sc.next();
                    ret = usersolution.getIndex(mWord);
                    ans = sc.nextInt();
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

    public static void main(String[] args) throws Exception
    {
        System.setIn(new java.io.FileInputStream("acm/samples/pro1463c/input"));

        Scanner sc = new Scanner(System.in);

        int TC = sc.nextInt();
        int MARK = sc.nextInt();

        for (int testcase = 1; testcase <= TC; ++testcase)
        {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        sc.close();

    }
}