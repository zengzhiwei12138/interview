package azj.zzw.interview.netty.tomcat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see 等待最帅的添加
 * @since 2019/7/3 0003-15:01
 */
public class MyRequest {

    private ChannelHandlerContext ctx;

    private HttpRequest request;

    public MyRequest(ChannelHandlerContext ctx, HttpRequest r) {
        this.ctx = ctx;
        this.request = r;
    }

    String getUri(){
        return request.getUri();
    }

    String getMethod(){
       return request.getMethod().name();
    }

    Map<String, List<String>> getParameters(){
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
        return decoder.parameters();
    }

    String getParameter(String name){
        Map<String, List<String>> parameters = getParameters();
        List<String> strings = parameters.get(name);
        if (strings == null){
            return null;
        } else {
            return strings.get(0);
        }
    }
}
