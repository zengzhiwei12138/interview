package azj.zzw.interview.pattern.strategy;

/**
 * 策略模式实现飞机系统
 * 飞机的飞行行为
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Strategy
 * @since 2019/8/16 0016-9:56
 */
public interface FlyBehavior {

    /***
     * 飞机的飞行特征
     */
    void fly();
}
