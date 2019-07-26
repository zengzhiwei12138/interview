package azj.zzw.interview.netty.tomcat;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see ServerBootstrap
 * @since 2019/7/3 0003-10:41
 */

@Slf4j
public class MyTomcat {

    /**
     * 启动tomcat
     *
     * @param port
     */
    public void start(Integer port) {
        try {
            // netty 主从
            EventLoopGroup parent = new NioEventLoopGroup();
            EventLoopGroup child = new NioEventLoopGroup();


            new Bootstrap().connect("127.0.0.1",8080);

            // netty服务
            ServerBootstrap server = new ServerBootstrap();
            server.group(parent, child)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            // 无锁化串行编程
                            // 编码器
                            client.pipeline().addLast(new HttpResponseDecoder());
                            // 解码器
                            client.pipeline().addLast(new HttpRequestEncoder());
                            // 业务处理
                            client.pipeline().addLast(new MyTomcatHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = server.bind(port).sync();
            log.info("my tomcat is started success , the port is [{}]", port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MyTomcat().start(8088);
    }


}
