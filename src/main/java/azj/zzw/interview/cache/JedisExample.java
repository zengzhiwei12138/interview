package azj.zzw.interview.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see redis.clients.jedis.Jedis
 * @since 2019/7/27 0027-23:00
 */
public class JedisExample {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.set("", "", "NX", "PX", 2000);
        jedis.set("", "");
        new JedisPool();
        // new JedisSentinelPool();
    }
}
