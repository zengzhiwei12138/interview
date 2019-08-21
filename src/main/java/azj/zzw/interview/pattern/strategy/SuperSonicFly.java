package azj.zzw.interview.pattern.strategy;

/**
 * 策略模式实现飞机系统
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Strategy
 * @since 2019/8/16 0016-10:24
 */
public class SuperSonicFly implements FlyBehavior {


    @Override
    public void fly() {
        System.out.println("I'm super sonic fly");
    }
}
