package azj.zzw.interview.distribute.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see ZkClient
 * @since 2019/4/22 0022-19:21
 */
@Slf4j
public class ZkClientDemo {

    /**
     * 连接zookeeper集群或单机的主机和端口
     */
    private static final String CONNECT_STRING = "192.168.124.128:2181,192.168.124.129:2181,192.168.124.131:2181";

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(CONNECT_STRING, 4000);
        log.info("连接zookeeper成功,zkClient-->{}", zkClient);


        // 创建临时节点
        zkClient.createEphemeral("/zkClient");
        log.info("创建临时节点成功");

        // 递归创建父节点的功能
        zkClient.createPersistent("/zzw/zzw2/zzw3/zzw4", true);
        log.info("递归创建节点成功");


        // 获取子节点
        List<String> children = zkClient.getChildren("/zzw");
        log.info("子节点列表为children list = {}", children);

        // 订阅监听
        zkClient.subscribeDataChanges("/zzw", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                log.info("节点名称{},节点修改后的值{}", dataPath,data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        });

        // 修改数据
        zkClient.writeData("/zzw", "test zzw");
        log.info("修改数据成功");

        // 递归删除
        zkClient.deleteRecursive("/zzw");
        log.info("递归删除节点成功");
    }
}
