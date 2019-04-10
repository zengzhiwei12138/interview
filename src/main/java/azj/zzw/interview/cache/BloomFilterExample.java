package azj.zzw.interview.cache;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * 本类是测试谷歌开源的布隆过滤器的效果 BloomFilter
 * <p>测试分两步：
 * <p>1. 往过滤器中添加一百万个数，然后验证这一百万个数能否通过过滤器。（坏人必须被抓）
 * <p>2. 找一百万不在此范围的数，验证布隆过滤器的错误率。（误伤好人的情况）
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see BloomFilter
 * @since 2019/4/9 0009-17:39
 */
@Slf4j
public class BloomFilterExample {

    /**
     * BloomFilter的初始化大小
     */
    private static final Integer SIZE = 1000000;

    /**
     * 增量
     */
    private static final Integer INCREMENT = 10000;

    /**
     * 初始化BloomFilter
     */
    private static BloomFilter bloomFilter = BloomFilter.create(Funnels.integerFunnel(), SIZE);

    public static void main(String[] args) {
        // 往BloomFilter存放一百万数据
        for (int i = 0; i < SIZE; i++) {
            bloomFilter.put(i);
        }

        // 验收第一步  过滤所有包含的元素
        for (int j = 0; j < SIZE; j++) {
            if (!bloomFilter.mightContain(j)) {
                log.warn("there are some data not filter by bloomFilter");
            }
        }

        // 验证第二步 测试错误率
        // 存放错误过滤的数据
        List<Integer> list = new LinkedList<>();
        for (int z = SIZE; z < SIZE + INCREMENT; z++) {
            if (bloomFilter.mightContain(z)) {
                list.add(z);
            }
        }
        // 打印总共错误过滤的数量
        log.info("the error filter data size is {}", list.size());

    }


}
