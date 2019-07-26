package azj.zzw.interview.netty.tomcat;

import lombok.extern.slf4j.Slf4j;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see MyServlet
 * @since 2019/7/3 0003-15:03
 */
@Slf4j
public class Servlet1 extends MyServlet{

    @Override
    public void doGet(MyRequest request, MyResponse response) {
        try {
            String name = request.getParameter("name");
            log.info("name=[{}]",name);
            response.write(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) {
        super.doPost(request, response);
    }
}
