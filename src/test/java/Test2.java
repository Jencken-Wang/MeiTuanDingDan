import com.meituan.order.HelloWorldApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = HelloWorldApplication.class)
public class Test2 {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testRedis() {

        redisTemplate.opsForValue().set("testkey", "testvalue");
        System.out.println(redisTemplate.opsForValue().get("testkey"));
    }

}
