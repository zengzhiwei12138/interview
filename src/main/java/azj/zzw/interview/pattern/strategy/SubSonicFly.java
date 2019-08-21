package azj.zzw.interview.pattern.strategy;

/**
 * 策略模式实现飞机系统
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Strategy
 * @since 2019/8/16 0016-10:23
 */
public class SubSonicFly implements FlyBehavior {


    @Override
    public void fly() {
        System.out.println("I'm sub sonic fly");
    }
}
