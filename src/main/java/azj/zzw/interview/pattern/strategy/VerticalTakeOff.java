package azj.zzw.interview.pattern.strategy;

/**
 * 策略模式实现飞机系统
 * VerticalTakeOff
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Strategy
 * @since 2019/8/16 0016-10:21
 */
public class VerticalTakeOff implements TakeOffBehavior {


    @Override
    public void takeOff() {
        System.out.println("I'm vertical take off");
    }
}
