package com.xf.zhang;

import com.xf.zhang.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    private Logger logger= LoggerFactory.getLogger(TestRedis.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("zhangsan", "张三");
        String str=(String)redisTemplate.opsForValue().get("zhangsan");
        logger.info(str);
    }

    @Test
    public void testRedisObject() {
        User user = new User();
        user.setName("xx");
        user.setPass("yyyy");
        user.setId(1);
        redisTemplate.opsForValue().set("user", user);

        User user1=(User)redisTemplate.opsForValue().get("user");
        logger.info(user1.toString());
    }

    @Test
    public void testRedisList() {
        List<User> list=new ArrayList<>();
//        for(int i=0;i<9;i++){
//            User u=new User();
//            u.setName("xx");
//            u.setPass("yyy");
//            u.setId(i);
//            list.add(u);
//            redisTemplate.opsForList().leftPush("list",u);
//            //System.out.println("----->"+l);
//        }

       // list.forEach(action->System.out.println(action.toString()));
        //redisTemplate.opsForList().rightPushAll()
        System.out.println(redisTemplate.opsForList().index("list",0));
        System.out.println(redisTemplate.opsForList().size("list"));
        System.out.println(redisTemplate.opsForList().range("list",0,8));
    }

    @Test
    public void testRedisZSet() {
        System.out.println(redisTemplate.opsForZSet().range("userList",0,10));
        User u=new User();
        u.setName("Ronin");
        u.setPass("vampire");
        u.setId(4);
        redisTemplate.opsForZSet().removeRangeByScore("userList",u.getId(),u.getId());//删除数据
        System.out.println(redisTemplate.opsForZSet().range("userList",0,10));
        redisTemplate.opsForZSet().add("userList",u,u.getId());// 新增
        System.out.println(redisTemplate.opsForZSet().range("userList",0,10));

    }


}
