package com.xf.zhang;

import com.alibaba.fastjson.JSONObject;
import com.xf.zhang.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {


    @Test
    public void test() {

        User user=new User();
        user.setName("xx");
        List<User> list=new ArrayList<>();
        for(int i=0;i<10;i++){
            User u=new User();
            u.setName("xx");
            u.setPass("yyy");
            u.setId(i);
            list.add(u);
        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("user",user);
        jsonObject.put("list",list);

        System.out.println(jsonObject.get("user"));
        System.out.println(jsonObject.get("list"));

    }

}

