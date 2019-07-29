package azj.zzw.interview.throwable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * 异常的链式调用
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Throwable
 * @since 2019/7/29 0029-16:59
 */
public class ExceptionExample2 {

    public static void main(String[] args) {
        System.out.println("请输入2个加数");
        int result;
        try {
            result = add();
            System.out.println("结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取输入的2个整数返回
     */
    private static List<Integer> getInputNumbers() {
        List<Integer> nums = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        try {
            int num1 = scan.nextInt();
            int num2 = scan.nextInt();
            nums.add(new Integer(num1));
            nums.add(new Integer(num2));
        } catch (InputMismatchException immExp) {
            throw immExp;
        } finally {
            scan.close();
        }
        return nums;
    }

    /**
     * 执行加法计算
     */
    private static int add() throws Exception {
        int result;
        try {
            List<Integer> nums = ExceptionExample2.getInputNumbers();
            result = nums.get(0) + nums.get(1);
        } catch (InputMismatchException immExp) {
            //链化:以一个异常对象为参数构造新的异常对象。
            throw new Exception("计算失败", immExp);
        }
        return result;
    }

    @Test
    public void testStringIndexOutOfBoundsException(){
        String test = "111";
        test.substring(0,4);
    }
}
