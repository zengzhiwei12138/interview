package azj.zzw.interview.distribute.zookeeper;

import azj.zzw.interview.domain.UserCenter;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper选主master的实现
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see ZkClient
 * @since 2019/4/28 0028-9:40
 */
@Slf4j
public class MasterChoose {

    /**
     * 连接zookeeper集群或单机的主机和端口
     */
    private static final String CONNECT_STRING = "192.168.124.128:2181,192.168.124.129:2181,192.168.124.131:2181";

    /**
     * zkClient  连接zookeeper
     */
    private ZkClient zkClient;

    /**
     * 需要争抢的节点
     */
    private static final String MASTER_PATH = "/master";

    /**
     * 节点的监听
     */
    private IZkDataListener listener;

    /**
     * 其他机器
     */
    private UserCenter server;

    /**
     * 主节点
     */
    private UserCenter master;

    /**
     * 是否启动
     */
    private boolean running;

    /**
     * 手动创建线程池更好哦
     * 定时任务线程池
     */
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public MasterChoose(UserCenter server) {
        this.server = server;
        this.listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                // 节点删除的监听
            }
        };
    }

    /**
     * 启动服务
     */
    public void start() {
        if (!running){
            running = true;
            // 注册监听事件
            zkClient.subscribeDataChanges(MASTER_PATH, listener);
            // 选主
            chooseMaster();
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        if (running){
            running = false;
            // 取消注册监听
            zkClient.unsubscribeDataChanges(MASTER_PATH, listener);
            // 释放主
            releaseMaster();
        }
    }

    /**
     * 检测是不是master节点
     *
     * @return  是master返回true 否则返回false
     */
    private boolean checkIsMaster() {
        UserCenter userCenter = zkClient.readData(MASTER_PATH, true);
        if (userCenter.getName().equals(server.getName())){
            master = userCenter;
            return true;
        }
        return false;
    }

    /**
     * 选举主节点
     */
    private void chooseMaster() {
        if (!running) {
            log.warn("当前服务没有启动");
            return;
        }
        try {
            // 创建节点
            zkClient.createEphemeral(MASTER_PATH, server);
            // 节点创建成功
            master = server;
            log.info("{}我现在是master,你们都要听我的", master.getName());

            // 定时器  定时发生故障
            scheduledExecutorService.schedule(() -> releaseMaster(), 5, TimeUnit.SECONDS);
        } catch (ZkNodeExistsException e) {
            // 节点已经存在
            // 获取节点
            UserCenter userCenter = zkClient.readData(MASTER_PATH, true);
            if (userCenter == null) {
                // 重新选主
                chooseMaster();
            } else {
                master = userCenter;
            }

        }
    }

    /**
     * 释放主节点
     */
    private void releaseMaster() {
        // 判断当前是不是mater
        if (checkIsMaster()) {
            // 是master 删除节点
            zkClient.delete(MASTER_PATH, -1);
        }
    }

}
