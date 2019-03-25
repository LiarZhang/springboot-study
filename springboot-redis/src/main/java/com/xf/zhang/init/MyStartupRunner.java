package com.xf.zhang.init;

import com.xf.zhang.mapper.UserMapper;
import com.xf.zhang.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MyStartupRunner implements CommandLineRunner {

    private Logger logger= LoggerFactory.getLogger(MyStartListener.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserMapper userMapper;

    @Override
    public void run(String... strings) throws Exception {

        List<User> list= userMapper.selectAll();
//        list.forEach(action->{
//            redisTemplate.opsForZSet().add("userList",action,action.getId());
//            logger.info(action.toString());
//        });

    }
}
