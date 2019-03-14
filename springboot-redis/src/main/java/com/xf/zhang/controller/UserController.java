package com.xf.zhang.controller;

import com.xf.zhang.pojo.User;
import com.xf.zhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/manage")
public class UserController {

    @Autowired
    private UserService managerService;

    @GetMapping("/get")
    public User get(Integer id) {

        User user = managerService.findById(id);
        return user;
    }

    @GetMapping("/post")
    public String post( ) {
        for (int i=0;i<20;i++){
            User user=new User();
            user.setName(i+"ss"+i);
            user.setPass(i+"ppp"+i);
            managerService.insert(user);
        }
        return "SUCCESS";
    }

    @GetMapping("/put")
    public User put(User manager) {
        manager=managerService.update(manager);
        return manager;
    }

    @GetMapping("/delete")
    public User delete(Integer id) {
        User user=managerService.delete(id);
        return user;
    }

    @GetMapping("/getList")
    public List<User> get(Integer startIndex,Integer pageSize) {
        Map<String,Object> map=new HashMap<>();
        map.put("startIndex",startIndex);
        map.put("pageSize",pageSize);

        List<User> userList = managerService.getList(map);
        return userList;
    }

    @GetMapping("/getListByUser")
    public List<User> getListByUser(User user) {
        List<User> userList = managerService.getListByUser(user);
        return userList;
    }

}