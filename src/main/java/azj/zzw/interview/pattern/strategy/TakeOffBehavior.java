package azj.zzw.interview.pattern.strategy;

/**
 * 策略模式实现飞行系统
 * 飞机的起飞特征
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Strategy
 * @since 2019/8/16 0016-9:56
 */
public interface TakeOffBehavior {

    /**
     * 飞机的起飞行为
     */
    void takeOff();
}
