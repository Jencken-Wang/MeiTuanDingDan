import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.meituan.order.HelloWorldApplication;
import com.meituan.order.controller.TestConstructorInjection;
import com.meituan.order.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = HelloWorldApplication.class)
public class Test2 {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TestConstructorInjection testConstructorInjection;

    @Test
    public void testRedis() {


        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.flushDb();
        redisTemplate.opsForValue().set("user", "testvalue");
        System.out.println(redisTemplate.opsForValue().get("user"));
//        System.out.println(redisTemplate.keys("*"));
//        System.out.println(redisTemplate.opsForHyperLogLog().size("hyper"));
    }

    @Test
    void test2() {
        redisUtils.set("name", "xiaoming");
//        redisUtils.expire("name", 10);
        System.out.println(redisUtils.get("name"));

        redisUtils.flushdb();
    }

    @Test
    void test3() {
//        testConstructorInjection.test();
        String startTime = "2022-09-01 00:00:00";
        String  endTime = "2022-09-31 23:59:59";
        Date start = DateUtil.parse(startTime);
        Date end = DateUtil.parse(endTime);

        Date startIndex = start;
        Date endIndex = DateUtil.offsetDay(start, 10);
//        while (DateUtil.compare(endIndex, end) < 0) {
//            System.out.println(startIndex + " " + endIndex);
//            startIndex = DateUtil.offsetSecond(endIndex, 1);
//            endIndex = DateUtil.offsetDay(endIndex, 10);
//        }


        while (true) {
            if (DateUtil.compare(endIndex, end) < 0) {
                System.out.println(startIndex + " " + endIndex);
                startIndex = DateUtil.offsetSecond(endIndex, 1);
                endIndex = DateUtil.offsetDay(endIndex, 10);
            } else {
                System.out.println(startIndex + " " + end);
            }
        }
    }


    @Test
    void test4() {
        List<Integer> l1  =new ArrayList<>();
        List<Integer> l2  =new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            l1.add(i);
        }
        System.out.println(l1);
        l2.add(1);
        l2.add(2);
        l2.add(3);

        l2.addAll(l1);
        System.out.println(l2);
    }

}
