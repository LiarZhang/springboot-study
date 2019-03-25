package com.xf.zhang.init;

import com.xf.zhang.mapper.UserMapper;
import com.xf.zhang.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

public class MyStartListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger= LoggerFactory.getLogger(MyStartListener.class);


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        UserMapper userMapper= contextRefreshedEvent.getApplicationContext().getBean(UserMapper.class);

//        List<User> list= userMapper.selectAll();
//        logger.info("User size {} "+list.size());
//        list.forEach(action->{
//            System.out.println(action.toString());
//        });

    }
}
