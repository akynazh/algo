package test;

import java.util.*;

public class Test1 {
    static class Obj {
        int a;
        Obj(int a) {
            this.a = a;
        }
    }
    public static void main(String[] args) {
//        TreeSet<Obj> set = new TreeSet<>(Comparator.comparingInt(o -> o.a));
//        set.add(new Obj(4));
//        set.add(new Obj(2));
//        set.add(new Obj(3));
//        Obj first = set.first();
//        System.out.println(first.a);
//        first.a = 23;
//        set.remove(first);
//        System.out.println(set.first().a);

//        System.out.println('a' - 'b');

        Queue<Integer> queue = new ArrayDeque<>();
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 999);
        queue.offer(3);
        queue.offer(6);
        queue.offer(999);
        queue.remove(map.get(1));
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
}
