package azj.zzw.interview.distribute.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper连接单机和集群的session的demo
 * zookeeper创建节点以及监听的demo
 * zookeeper的权限控制
 * @author zzw zengzhiwei_hfut@163.com
 * @see ZooKeeper
 * @since 2019/4/22 0022-14:13
 */
@Slf4j
public class ZookeeperDemo implements Watcher{

    /**
     * 连接zookeeper集群或单机的主机和端口
     */
    private static final String CONNECT_STRING = "192.168.124.128:2181,192.168.124.129:2181,192.168.124.131:2181";

    private static ZooKeeper zooKeeper;

    private static Stat stat;

    /**
     * 计数器
     */
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        // 路径
        String path = "/zzw12149";

        // 创建zookeeper的session连接
        zooKeeper = new ZooKeeper(CONNECT_STRING, 5000, new ZookeeperDemo());
        // 等待 等到直到连接成功后
        countDownLatch.await();
        // 打印连接的结果
        log.info("the zookeeper state is {}", zooKeeper.getState());

        // 删除节点
        zooKeeper.delete(path + "/zzw1", -1);
        zooKeeper.delete(path, -1);

        // 创建节点  ZooDefs 节点的权限控制  CreateMode 节点的类型 持久化/临时
        String nodeName = zooKeeper.create(path, "789".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        // 创建子节点
        zooKeeper.create(path + "/zzw1", "zj".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 增加一个监听
        zooKeeper.getData(path,new ZookeeperDemo() , stat);
        // 增加监听  存在则创建监听
        zooKeeper.exists(path+"/zzw1", true);

        log.info("{}节点创建成功", nodeName);

        // 修改数据
        zooKeeper.setData(path, "love".getBytes(), -1);

        // 修改子路径数据
        zooKeeper.setData(path+"/zzw1", "zzw".getBytes(), -1);

        // 获取子节点
        List<String> children = zooKeeper.getChildren(path, true);
        log.info("children=={}",children);


        // 权限 acl read / delete / create /
        // 权限模式
        // schema 授权对象
        // digest 用户名密码
        // world 开放

        Thread.sleep(100000);

    }


    @Override
    public void process(WatchedEvent event) {
        // 打印type的类型
        log.info("state={},type={}",event.getState(),event.getType());
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {

            // 连接后监听内容
            switch (event.getType()) {
                case None:
                    countDownLatch.countDown();
                    log.info("the state is {}", event.getState());
                    break;
                case NodeCreated:
                    log.info("创建节点的路径是{}", event.getPath());
                    break;
                case NodeDeleted:
                    log.info("删除的路径为{}", event.getPath());
                    break;
                case NodeDataChanged:
                    try {
                        log.info("路径{}-->改变后的值:{}", event.getPath(), zooKeeper.getData(event.getPath(), true, stat));
                    } catch (Exception e) {
                        log.error("the excepton is",e);
                    }
                    break;
                case NodeChildrenChanged:
                    log.info("子节点的路径是{}", event.getPath());
                    break;
                default:
                    log.warn("the type of watch event is {}", event.getType());
                    break;
            }
        }
    }
}
