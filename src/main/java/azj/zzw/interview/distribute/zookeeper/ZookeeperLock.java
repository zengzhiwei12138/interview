package azj.zzw.interview.distribute.zookeeper;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.*;

/**
 * 通过zookeeper节点的特性实现分布式锁
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see LockWatcher
 * @since 2019/4/24 0024-17:08
 */

@Slf4j
public class ZookeeperLock {

    /**
     * 连接zookeeper集群或单机的主机和端口
     */
    private static final String CONNECT_STRING = "192.168.124.128:2181,192.168.124.129:2181,192.168.124.131:2181";

    /**
     * 根节点
     */
    private static final String ROOT_LOCKS = "/LOCKS";

    /**
     * zookeeper
     */
    private ZooKeeper zooKeeper;

    /**
     * 会话超时时间
     */
    private int sessionTimeout = 5000;

    /**
     * 记录锁节点的ID
     */
    private String lockId;

    /**
     * 子节点的数据
     */
    private static final byte[] data = {1, 2};

    /**
     * 信号枪
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 构造方法  初始化zookeeper连接
     */
    public ZookeeperLock() {
        // 获取zookeeper连接
        this.zooKeeper = getConnect();
    }

    /**
     * 获取分布式锁
     *
     * @return 获取成功返回true
     */
    public boolean zookeeperLock() {
        // 创建节点
        try {
            lockId = zooKeeper.create(ROOT_LOCKS + "/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("线程{}成功创建了一个lock节点{},开始竞争锁", Thread.currentThread().getName(), lockId);
            // 获取根节点下的所有子节点
            List<String> childrenList = zooKeeper.getChildren(ROOT_LOCKS, true);
            // 对子节点进行排序
            SortedSet<String> nodeSet = new TreeSet<>();
            // 循环中不要用+号  反编译就知道
            StringBuilder sb = new StringBuilder(ROOT_LOCKS).append("/");
            for (String str : childrenList) {
                nodeSet.add(sb.append(str).toString());
                // 清空str
                sb.delete(sb.length() - str.length(), sb.length());
            }
            // 取出第一个子节点  最小的
            String first = nodeSet.first();
            // 判断是否取到锁
            if (lockId.equals(first)) {
                log.info("线程{}成功获取到锁,lock节点为{}", Thread.currentThread().getName(), lockId);
                return true;
            }
            // 没有取到锁 获取比lockId小的列表
            SortedSet<String> lessThanSort = nodeSet.headSet(lockId);
            if (!lessThanSort.isEmpty()) {
                // 信号枪
                // CountDownLatch countDownLatch = new CountDownLatch(1);
                log.debug("the prev node is {}", lessThanSort.last());
                // 监听最后一个节点 即lockId上一个节点
                zooKeeper.exists(lessThanSort.last(), new LockWatcher(countDownLatch));
                // 会话超时或者节点锁删除
                countDownLatch.await(sessionTimeout, TimeUnit.MILLISECONDS);
                log.info("线程{}成功获取到锁,lockId={}", Thread.currentThread().getName(), lockId);
            }
            return true;
        } catch (KeeperException e) {
            log.error("KeeperException", e);
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @return 释放成功返回true
     */
    public boolean zookeeperUnLock() {
        log.info("线程{}开始释放锁,lockId={}", Thread.currentThread().getName(), lockId);
        // 删除lockId节点
        try {
            zooKeeper.delete(lockId, -1);
            log.info("节点{}成功被删除", lockId);
            return true;
        } catch (KeeperException e) {
            log.error("KeeperException", e);
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
        return false;
    }

    /**
     * 获取一个zookeeper连接
     *
     * @return
     * @author zengzhiwei
     */
    private ZooKeeper getConnect() {
        ZooKeeper zooKeeper = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper(CONNECT_STRING, sessionTimeout, (event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    // 连接成功
                    countDownLatch.countDown();
                }
            }));
            countDownLatch.await();
        } catch (IOException e) {
            log.error("zookeeper connect failed", e);
        } catch (InterruptedException e) {
            log.error("countDown has error some error", e);
        }
        return zooKeeper;
    }

    /**
     * 内部类 zookeeper分布式锁的监听
     *
     * @author zengzhiwei
     * @see org.apache.zookeeper.Watcher
     * @since 0.0.1
     */
    class LockWatcher implements Watcher {

        /**
         * 信号枪 又来监控节点删除 释放锁
         */
        private CountDownLatch countDownLatch;

        LockWatcher(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeDeleted) {
                countDownLatch.countDown();
            }
        }
    }

    /**
     * 测试zookeeperLock
     *
     * @param args
     */
    public static void main(String[] args) {
        // 定义线程的数量
        int threadCount = 5;
        // 100个线程竞争锁
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        // 线程命名
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("线程-%d").build();
        // 线程池
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < threadCount; i++) {
            // 获取锁
            executorService.execute(() -> {
                ZookeeperLock zookeeperLock = new ZookeeperLock();
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                    // 获取锁
                    zookeeperLock.zookeeperLock();
                    // 睡眠
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    log.error("InterruptedException--获取线程失败", e);
                } finally {
                    // 释放锁
                    zookeeperLock.zookeeperUnLock();
                }
            });
        }
    }
}
