package azj.zzw.interview.pattern.strategy;

import lombok.Setter;

/**
 * 策略模式实现飞机系统
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Strategy
 * @since 2019/8/16 0016-9:54
 */
public class Plane {

    /**
     * 起飞特征
     */
    @Setter
    TakeOffBehavior takeOffBehavior;

    /**
     * 飞行特征
     */
    @Setter
    FlyBehavior flyBehavior;


    public void performTakeOff(){
        takeOffBehavior.takeOff();
    }

    public void performFly(){
        flyBehavior.fly();
    }


}
