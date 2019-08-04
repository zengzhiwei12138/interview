package azj.zzw.interview.collection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
}
