package azj.zzw.interview.distributed.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see ZooKeeper
 * @since 2019/4/22 0022-14:13
 */
@Slf4j
public class CreateSessionDemp {

    /**
     * 连接zookeeper集群或单机的主机和端口
     */
    private static final String CONNECT_STRING = "192.168.124.128:2181,192.168.124.129:2181,192.168.124.131:2181";

    public static void main(String[] args) throws IOException, InterruptedException {

        // 计数器
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ZooKeeper zooKeeper = new ZooKeeper(CONNECT_STRING, 5000, new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                    log.info("the state is {}",event.getState());
                }
            }
        });

        // 等待 等到直到连接成功后
        countDownLatch.await();

        log.info("the zookeeper state is {}",zooKeeper.getState());

    }


}
