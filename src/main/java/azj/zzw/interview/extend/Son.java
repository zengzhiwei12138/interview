package azj.zzw.interview.extend;

import org.junit.Test;

/**
 * 子类
 * 该类是学习spring源码时   AbstractXmlApplicationContext类调用getConfigResources方法时
 * 走的是子类ClassPathXmlApplicationContext getConfigResources的方法
 *
 * 该补补基础啦
 * @author zzw zengzhiwei_hfut@163.com
 * @since 2019/8/7 0007-11:24
 */
public class Son extends Father{


    private String word = "I am your son";


    @Override
    protected String say(){
        return this.word;
    }

    @Test
    public void testSay(){
        refresh();
    }

}
