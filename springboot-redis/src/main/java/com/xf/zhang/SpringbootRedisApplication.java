package com.xf.zhang;

import com.xf.zhang.init.MyStartListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringbootRedisApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(SpringbootRedisApplication.class);
        springApplication.addListeners(new MyStartListener());
        springApplication.run(args);

        //SpringApplication.run(SpringbootRedisApplication.class, args);
    }

}
