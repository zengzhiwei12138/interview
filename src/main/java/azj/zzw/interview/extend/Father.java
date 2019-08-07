package azj.zzw.interview.extend;

/**
 * 父类
 * 该类是测试重写之后 调用的是子类的重写方法 而不是父类定义的方法
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @since 2019/8/7 0007-11:23
 */
public class Father extends Grandpa {

    private String word = "I am your father";

    protected String say(){
        return null;
    }

    @Override
    protected void loadBeanDefinition() {
        System.out.println("my grandpa is lazy , so this method is implements by me");
        System.out.println(this);
        System.out.println("I guess this word is my son to print"+say());
    }

}
