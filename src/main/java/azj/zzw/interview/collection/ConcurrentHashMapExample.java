package azj.zzw.interview.collection;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap源码学习相关测试用例
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see java.util.concurrent.ConcurrentHashMap
 * @since 2019/8/6 0006-13:00
 */
public class ConcurrentHashMapExample<K,V> {


    /**
     * 设置循环条件的最大值
     */
    private static final Integer MAX_SIZE = 10000;

    public static void main(String[] args) throws Exception{

        // debug the method initTable of concurrentHashMap
        Map<Integer,String> map = new ConcurrentHashMap<>(16);

        for (int i = 0; i < MAX_SIZE; i++) {
            map.put(i, "test" + i);
        }

        // 获取ConcurrentHashMap的内部类Node
        Class<?> clazz = Class.forName("java.util.concurrent.ConcurrentHashMap");
        // 获取方法
        Method tableSizeFor = clazz.getDeclaredMethod("tableSizeFor",int.class);
        tableSizeFor.setAccessible(true);

        // 调用方法
        Integer result = (Integer) tableSizeFor.invoke(null, 128);
        // 打印结果集
        System.out.println(result);
        // 0000 0000 0000 0000 0000 0000 0000 1100
        // 0000 0000 0000 0000 0000 0000 0000 0110
        int a = 12 >>> 1;

        int b = 6;
        b |= 2;
    }


    /***
     * 测试 ConcurrentHashMap 解决hash冲突
     */
    @Test
    public void testHash() throws Exception{
        Node<K,V> e3 = new Node(3, 3, 3, null);
        Node<K,V> e2 = new Node(2, 2, 2, e3);
        Node<K,V> e1 = new Node(1, 1, 1, e2);


        for (Node<K,V> e = e1;;){
            Node<K,V> pred = e;
            if ((e = e.next) == null){
                System.out.println(pred);
                pred.next = new Node(4, 4, 4, null);
                System.out.println("e3"+e3);
                System.out.println("next"+e3.next);
                break;
            }
            System.out.println("test");
        }
    }


    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K,V> next;

        Node(int hash, K key, V val,Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = next;
        }

        @Override
        public final K getKey()       { return key; }

        @Override
        public final V getValue()     { return val; }

        @Override
        public final int hashCode()   { return key.hashCode() ^ val.hashCode(); }

        @Override
        public final String toString(){ return key + "=" + val; }

        @Override
        public final V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public final boolean equals(Object o) {
            Object k, v, u; Map.Entry<?,?> e;
            return ((o instanceof Map.Entry) &&
                    (k = (e = (Map.Entry<?,?>)o).getKey()) != null &&
                    (v = e.getValue()) != null &&
                    (k == key || k.equals(key)) &&
                    (v == (u = val) || v.equals(u)));
        }

        /**
         * Virtualized support for map.get(); overridden in subclasses.
         */
        Node<K,V> find(int h, Object k) {
            Node<K,V> e = this;
            if (k != null) {
                do {
                    K ek;
                    if (e.hash == h &&((ek = e.key) == k || (ek != null && k.equals(ek)))){
                        return e;
                    }
                } while ((e = e.next) != null);
            }
            return null;
        }
    }
}
