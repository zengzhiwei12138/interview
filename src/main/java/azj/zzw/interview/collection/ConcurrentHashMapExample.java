package azj.zzw.interview.collection;

import sun.misc.Unsafe;

import java.lang.reflect.Method;

/**
 * ConcurrentHashMap源码学习相关测试用例
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see java.util.concurrent.ConcurrentHashMap
 * @since 2019/8/6 0006-13:00
 */
public class ConcurrentHashMapExample {

    private static final Unsafe U;

    static {
        U = Unsafe.getUnsafe();
    }


    public static void main(String[] args) throws Exception{

        System.out.println(U);

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
}
