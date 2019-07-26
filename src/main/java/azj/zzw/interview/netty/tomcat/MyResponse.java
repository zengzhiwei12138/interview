package azj.zzw.interview.netty.tomcat;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see ChannelHandlerContext
 * @since 2019/7/3 0003-15:01
 */
public class MyResponse {

    private ChannelHandlerContext ctx;

    private HttpRequest request;

    public MyResponse(ChannelHandlerContext ctx, HttpRequest r) {
        this.ctx = ctx;
        this.request = r;
    }

    public void write(String out) throws Exception{
        try {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(out.getBytes("UTF-8")));
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/json");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,response.content().readableBytes());
            response.headers().set(HttpHeaders.Names.EXPIRES,0);
            if (HttpHeaders.isKeepAlive(request)){
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(out);
        }catch (Exception e){

        }finally {
            ctx.flush();
        }

    }
}
