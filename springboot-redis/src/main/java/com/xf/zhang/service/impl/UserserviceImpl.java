package com.xf.zhang.service.impl;

import com.xf.zhang.mapper.UserMapper;
import com.xf.zhang.pojo.User;
import com.xf.zhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CacheConfig(cacheNames = "Manager")
@Service
public class UserserviceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    //使用Redis缓存
    //@Cacheable(value="Manager",key="#id")
    @Cacheable(value="Manager", keyGenerator = "keyGenerator")
    public User findById(Integer id) {
        System.out.println("---查数据库DB-----");
        userMapper.deleteByPrimaryKey(id);
        return userMapper.selectByPrimaryKey(id);
    }


    //@CachePut(value="Manager",key="#root.caches[0].name + ':' + #manager.getId()")
    //#root.caches[0].name:当前被调用方法所使用的Cache, 即"Manager"
    //@CachePut(value="Manager", keyGenerator = "keyGenerator")
    //@CachePut指定了key属性之后，则不会再调用keygenerator的方法
    //@CachePut(value="Manager",key="#root.caches[0].name + ':' +#manager.getId().toString()")
    @CacheEvict(value="Manager",allEntries = true)//清除Manager所有缓存数据
    public User update(User manager) {
        userMapper.updateByPrimaryKeySelective(manager);
        return userMapper.selectByPrimaryKey(manager.getId());
    }

    @CacheEvict(value="Manager",allEntries=true)//清除Manager所有缓存数据
    public User insert(User manager) {
        userMapper.insertSelective(manager);
        return manager;
    }

    @CacheEvict(value="Manager",allEntries=true, beforeInvocation=true)//清除id为3的数据
    public User delete(Integer id) {
        userMapper.deleteByPrimaryKey(id);
        return null;
    }

    //@Cacheable(value="Manager",key="#map")
    @Cacheable(value="Manager",keyGenerator = "keyGenerator")
    @Override
    public List<User> getList(Map<String, Object> map) {
        System.out.println("from DB");
        List<User> list=userMapper.getList(map);
        return list;
    }

    @Cacheable(value="Manager",key="'getListByUser'+'.user:'+#user.toString()")
    @Override
    public List<User> getListByUser(User user) {
        Map<String, Object> map=new HashMap<>();

        map.put("name",user.getName());
        map.put("pass",user.getPass());

        System.out.println("from DB");
        List<User> list=userMapper.getList(map);
        return list;
    }
}
