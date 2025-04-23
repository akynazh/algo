package jd;

/**
 * JD 一面：大数相加
 * 
 * @author akynazh@gmail.com
 * @date 2025/3/25
 * @description
 */
public class Test1 {
    public String add(String a, String b) {
        int c = 0;
        int i = a.length() - 1;
        int j = b.length() - 1;
        StringBuilder sbd = new StringBuilder();
        while (i >= 0 && j >= 0) {
            int x = Integer.parseInt(String.valueOf(a.charAt(i))),
                    y = Integer.parseInt(String.valueOf(b.charAt(j)));
            int z = (x + y + c) % 10;
            c = (x + y + c) / 10;
            sbd.append(z);
            i -= 1;
            j -= 1;
        }
        int k = i;
        if (j >= 0) {
            k = j;
            a = b;
        }
        while (k >= 0) {
            int x = Integer.parseInt(String.valueOf(a.charAt(k)));
            int z = (x + c) % 10;
            c = (x + c) / 10;
            sbd.append(z);
            k -= 1;
        }
        StringBuilder sbd1 = new StringBuilder();
        for (int t = sbd.length() - 1; t >= 0; t -= 1) {
            sbd1.append(sbd.charAt(t));
        }
        return sbd1.toString();
    }

    public static void main(String[] args) {
        System.out.println(new Test1().add("9123", "381"));
    }
}