package azj.zzw.interview.nio;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Selector
 * @since 2019/7/2 0002-13:51
 */
@Slf4j
public class NioServer {

    /**
     * 端口
     */
    private Integer port;

    /**
     * 地址
     */
    private InetSocketAddress address;

    /**
     * 选择号
     */
    private Selector selector;
    private ByteBuffer buffer;

    /**
     * 构造方法  初始化address
     *
     * @param port
     */
    public NioServer(Integer port) {
        try {
            this.port = port;
            this.address = new InetSocketAddress(this.port);
            // 开启服务
            ServerSocketChannel server = ServerSocketChannel.open();
            // 绑定地址
            server.bind(address);
            // 设置为非阻塞  默认为阻塞
            server.configureBlocking(false);

            // 开启选择
            selector = Selector.open();

            // 注册  等待连接
            server.register(selector, SelectionKey.OP_ACCEPT);

            log.info("服务器准备就绪,监听的端口是[{}]", this.port);
        } catch (Exception e) {
            log.info("the NioServer constructor has occur some exception", e);
        }
    }


    public void listen() {
        try {
            while (true) {
                // 多少等待
                int wait = selector.select();
                if (wait == 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                // 迭代器
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    // 处理
                    process(key);
                    iterator.remove();
                }
            }

        } catch (Exception e) {

        }
    }

    private void process(SelectionKey key) throws Exception{

        buffer = ByteBuffer.allocate(1024);

        if (key.isAcceptable()){
            // 是否连接
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()){
            // 可读
            SocketChannel client = (SocketChannel) key.channel();
            // 读取数据
            int len = client.read(buffer);
            if (len > 0){
                buffer.flip();
            }
        } else if (key.isWritable()){

        }
    }

}
