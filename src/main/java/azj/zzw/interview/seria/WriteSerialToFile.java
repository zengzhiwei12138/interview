package azj.zzw.interview.seria;

import azj.zzw.interview.domain.UserCenter;
import org.junit.Test;

import java.io.*;

/**
 * 将对象序列化到文件里
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see java.io.Serializable
 * @since 2019/7/24 0024-14:44
 */
public class WriteSerialToFile {

    public static void main(String[] args) throws Exception{
        // 将Java对象序列化对文件中
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File("E:/serial.txt")));
        outputStream.writeObject(new UserCenter("1","the serial name"));
        outputStream.close();
    }

    /**
     * 从文件中读取
     */
    @Test
    public void readObjectFromFile() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File("E:/serial.txt")));
        UserCenter userCenter = (UserCenter) inputStream.readObject();
        System.out.println(userCenter);
        inputStream.close();
    }
}
