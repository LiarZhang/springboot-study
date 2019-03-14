package com.xf.zhang.service;

import com.xf.zhang.pojo.User;

import java.util.List;
import java.util.Map;


public interface UserService {

    User findById(Integer id);

    User delete(Integer id);

    User insert(User manager);

    User update(User manager) ;

    List<User> getList(Map<String,Object> map);

    List<User> getListByUser(User user);
}
