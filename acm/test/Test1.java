package test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

public class Test1 {
    static class Obj {
        int a;
        Obj(int a) {
            this.a = a;
        }
    }
    public static void main(String[] args) {
        TreeSet<Obj> set = new TreeSet<>(Comparator.comparingInt(o -> o.a));
        set.add(new Obj(4));
        set.add(new Obj(2));
        set.add(new Obj(3));
        Obj first = set.first();
        System.out.println(first.a);
        first.a = 23;
        set.remove(first);
        System.out.println(set.first().a);
    }
}
