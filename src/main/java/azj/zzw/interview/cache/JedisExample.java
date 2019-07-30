package azj.zzw.interview.cache;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see redis.clients.jedis.Jedis
 * @since 2019/7/27 0027-23:00
 */
public class JedisExample {

    public static void main(String[] args) throws Exception {
        Jedis jedis = new Jedis();
        jedis.set("", "", "NX", "PX", 2000);
        jedis.set("", "");
        new JedisPool();
        // new JedisSentinelPool();
        RedissonClient redisson = Redisson.create();
        RLock redlock = redisson.getLock("test-lock");
        redlock.tryLock();
        redlock.tryLock(500,3000, TimeUnit.SECONDS);


    }
}
