package azj.zzw.interview.collection;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * HashMap的相关例子
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see java.util.HashMap
 * @since 2019/8/4 0004-12:14
 */
public class HashMapExample {

    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>(16);
        map.put("zzw", "I am the most awesome");
        map.put("zzw1", "I am the most awesome");

        Class<?> clazz = map.getClass();
        Method capacity = clazz.getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        System.out.println("capacity = " + capacity.invoke(map));

        Field size = clazz.getDeclaredField("size");
        size.setAccessible(true);
        System.out.println("size = " + size.get(map));
        System.out.println("hashcode = " + map.hashCode());
    }

    @Test
    public void testCalc() {
        Map<Integer, String> map = new ConcurrentHashMap<>(16);
        for (int i = 0; i < 20; i++) {
            map.put(i, "test" + i);
        }

        System.out.println(map.size());
    }


    @Test
    public void testSemaphore() {

        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("pool-thread-%d").build();

        Executor executor = new ThreadPoolExecutor(10, 20, 1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(20), threadFactory, new ThreadPoolExecutor.AbortPolicy());

        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < 20; i++) {
            try {
                semaphore.acquire();
                executor.execute(new MyRunnable());
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                //semaphore.release();
            }
        }

    }


    class MyRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("this is a test");
        }
    }

}
