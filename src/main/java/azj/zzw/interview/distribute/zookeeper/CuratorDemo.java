package azj.zzw.interview.distribute.zookeeper;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.concurrent.*;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see CuratorFrameworkFactory
 * @since 2019/4/22 0022-20:01
 */
@Slf4j
public class CuratorDemo {

    /**
     * 连接zookeeper集群或单机的主机和端口
     */
    private static final String CONNECT_STRING = "192.168.124.128:2181,192.168.124.129:2181,192.168.124.131:2181";

    public static void main(String[] args) throws Exception {
        // *******创建会话 风格1*******
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECT_STRING, 5000, 5000,
                new ExponentialBackoffRetry(1000, 3));

        // 启动一个连接
        curatorFramework.start();

        // ******创建一个会话  风格2******
        CuratorFramework curatorFramework2 = CuratorFrameworkFactory.builder().connectString(CONNECT_STRING)
                .sessionTimeoutMs(5000).connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).namespace("curator").build();
        // 启动一个连接
        curatorFramework2.start();

        log.info("成功{}--->{}", curatorFramework, curatorFramework2);

        // fluent风格

        // ******创建节点******
        String nodePath = curatorFramework.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT).forPath("/curator/curator2/curator3", "love zj".getBytes());
        log.info("节点创建成功,节点路径名称{}", nodePath);

        // ******查询******
        Stat stat = new Stat();
        byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath("/curator");
        log.info("查询到的数据为{}", new String(bytes));

        // *******更改*******
        Stat stat1 = curatorFramework.setData().forPath("/curator", "love zzw".getBytes());
        log.info("数据修改成功--->{}", stat1);

        // ******删除节点 默认version为-1******
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/curator");

        // ******异步操作******

        // 初始化线程池
        // 不要使用executors创建线程池 防止OOM 以及更好的明确线程池的运行规则 规避资源耗尽的风险
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("curator异步线程").build();
        ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        // 信号枪
        CountDownLatch countDownLatch = new CountDownLatch(1);
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).inBackground((client, event) -> {
            //打印当前线程编号
            log.info("线程{}，resultCode={},resultType={}", Thread.currentThread().getName(), event.getResultCode(), event.getType());
            countDownLatch.countDown();
        }, executorService).forPath("/zzw", "123".getBytes());

        // 等待
        countDownLatch.await();
        executorService.shutdown();

        // ******事务操作 curator独有******
        Collection<CuratorTransactionResult> curatorTransactionResults = curatorFramework.inTransaction().create().forPath("/transaction1", "transaction1".getBytes())
                .and().setData().forPath("/zzw", "transaction2".getBytes()).and().commit();

        for (CuratorTransactionResult curatorTransactionResult : curatorTransactionResults) {
            log.info("path{}--->type{}", curatorTransactionResult.getForPath(), curatorTransactionResult.getType());
        }
        // 删除zzw 和 transaction
        curatorFramework.inTransaction().delete().forPath("/zzw").and()
                .delete().forPath("/transaction1").and().commit();
        log.info("删除zzw和transaction1成功");

        // ******监听******
        // PathChildCache 监听一个路径下子节点的创建,删除,节点数据更新
        // NodeCache 监听一个节点的创建,更新,删除
        // TreeCache 上面两种监听的合体
        // 缓存路径下的所有子节点的数据

        // ****** PathChildCache
        String pathChildrenCachePath = "/pathChildrenCache";
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, pathChildrenCachePath, true);
        // NORMAL  初始化为空
        // BUILD_INITIAL_CACHE  start返回之前调用build方法
        // POST_INITIALIZED_EVENT cache初始化后发送一个event事件
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        pathChildrenCache.getListenable().addListener((client, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    log.info("pathChildrenCache增加子节点");
                    break;
                case CHILD_REMOVED:
                    log.info("pathChildrenCache删除子节点");
                    break;
                case CHILD_UPDATED:
                    log.info("pathChildrenCache更新子节点");
                    break;
                default:
                    log.info("type--{}", event.getType());
                    break;
            }
        });

        // 增加父节点
        curatorFramework.create().forPath(pathChildrenCachePath);
        // 增加子节点
        curatorFramework.create().forPath(pathChildrenCachePath + "/path1", "path_add".getBytes());
        TimeUnit.SECONDS.sleep(3);
        // 更新子节点
        curatorFramework.setData().forPath(pathChildrenCachePath + "/path1","path_update".getBytes());
        TimeUnit.SECONDS.sleep(3);
        // 删除子节点
        curatorFramework.delete().deletingChildrenIfNeeded().forPath(pathChildrenCachePath + "/path1");
        TimeUnit.SECONDS.sleep(3);


        // *******NodeCache
        NodeCache nodeCache = new NodeCache(curatorFramework, "/nodeCache", false);
        nodeCache.start(true);

        // 增加监听事件
        nodeCache.getListenable().addListener(() -> {
            log.info("节点数据发生变化,变化后的结果是{}", nodeCache.getCurrentData().getData());
        });

        // 增加节点
        curatorFramework.create().forPath("/nodeCache","nodeCache".getBytes());

        curatorFramework.setData().forPath("/nodeCache", "I love you".getBytes());
        TimeUnit.SECONDS.sleep(3);

        // 删除nodeCache和pathChildrenCache
        curatorFramework.inTransaction().delete().forPath("/nodeCache")
                .and().delete().forPath(pathChildrenCachePath).and().commit();
        log.info("删除nodeCache和pathChildrenCache成功");
    }
}
