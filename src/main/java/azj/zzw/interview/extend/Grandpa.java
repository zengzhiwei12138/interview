package azj.zzw.interview.extend;

/**
 * 父类的父类
 * 模拟spring 加载 xml的大概流程
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @since 2019/8/7 0007-12:39
 */
public abstract class Grandpa {

    private String word = "I am your grandpa";

    private void say(){
        System.out.println(this.word);
    }

    protected void refresh(){
        System.out.println("I am the grandpa");
        loadBeanDefinition();

    }

    /**
     * 定义一个抽象方法 模拟spring ClassPathXmlApplicationContext 类加载xml文件 loadBeanDefinition的过程
     */
    protected abstract void loadBeanDefinition();
}
