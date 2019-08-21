package azj.zzw.interview.pattern.strategy;

/**
 * 策略模式的学习
 *
 *  Sunny软件公司欲开发一款飞机模拟系统，该系统主要模拟不同种类飞机的飞行特征与起飞特
 * 征，需要模拟的飞机种类及其特征如表24-1所示：
 * 表24-1 飞机种类及特征一览表
 * 飞机种类 起飞特征 飞行特征
 * 直升机(Helicopter) 垂直起飞(VerticalTakeOff) 亚音速飞行(SubSonicFly)
 * 客机(AirPlane) 长距离起飞(LongDistanceTakeOff) 亚音速飞行(SubSonicFly)
 * 歼击机(Fighter) 长距离起飞(LongDistanceTakeOff) 超音速飞行(SuperSonicFly)
 * 鹞式战斗机(Harrier) 垂直起飞(VerticalTakeOff) 超音速飞行(SuperSonicFly)
 * 为将来能够模拟更多种类的飞机，试采用策略模式设计该飞机模拟系统。
 *
 * 策略模式的角色
 * 1. 环境角色  环境角色中定义一些抽象策略
 * 2. 抽象策略角色  定义一些算法的抽象方法  由实现策略角色实现
 * 3. 实现策略角色
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @since 2019/8/15 0015-17:27
 */
public class Strategy {

    public static void main(String[] args) {
        Plane plane = new AirPlane();
        plane.setFlyBehavior(new SubSonicFly());
        plane.setTakeOffBehavior(new LongDistanceTakeOff());
        plane.performTakeOff();
        plane.performFly();
    }

}
