package azj.zzw.interview.domain;

import java.io.Serializable;

/**
 * 本类用于实现zookeeper选master的机器信息
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Serializable
 * @since 2019/4/28 0028-9:37
 */
public class UserCenter implements Serializable {


    private static final long serialVersionUID = 2776006273076357192L;
    /**
     * 机器id
     */
    private String id;

    /**
     * 机器name
     */
    private String name;

    public UserCenter() {
    }

    public UserCenter(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserCenter{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
