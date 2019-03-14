package com.xf.zhang.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ClassUtils;



import java.lang.reflect.Array;
import java.lang.reflect.Method;


@Configuration
@EnableCaching
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    // 缓存管理器
    @Bean
    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // 设置缓存过期时间,单位是秒
        cacheManager.setDefaultExpiration(10000);
        return cacheManager;

    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        setSerializer(template);// 设置序列化工具
        return template;
    }

//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate();
//        template.setConnectionFactory(factory);
//        setSerializer(template);// 设置序列化工具
//        return template;
//    }

    private void setSerializer(RedisTemplate<String, String> template) {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 设置value的序列化规则和 key的序列化规则
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
    }

    /**
     * @description 自定义的缓存key的生成策略
     *              若想使用这个key  只需要讲注解上keyGenerator的值设置为keyGenerator即可</br>
     * @return 自定义策略生成的key
     */
    @Bean
    public KeyGenerator keyGenerator() {

        return new KeyGenerator(){

           /* @Override
            public Object generate(Object target, Method method, Object... params) {

                StringBuilder sb = new StringBuilder();
                String[] value = new String[1];

                // sb.append(target.getClass().getName());
                // sb.append(":" + method.getName());
                Cacheable cacheable = method.getAnnotation(Cacheable.class);
                if (cacheable != null) {
                    value = cacheable.value();
                }
                CachePut cachePut = method.getAnnotation(CachePut.class);
                if (cachePut != null) {
                    value = cachePut.value();
                }
                CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
                if (cacheEvict != null) {
                    value = cacheEvict.value();
                }
                sb.append(value[0]);

                for (Object obj : params) {
                    sb.append(":" + obj.toString());
                }
                logger.info("------------->"+sb.toString());
                return sb.toString();

            }*/

			/*@Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for(Object obj:params){
                    sb.append(obj.toString());
                }
                return sb.toString();
            } */


            public Object generate(Object target, Method method, Object... params) {
                StringBuilder key = new StringBuilder();
                key.append(target.getClass().getSimpleName()).append(".").append(method.getName()).append(":");
                if (params.length == 0) {
                    return key.append(0).toString();
                } else {
                    int index = 0;
                    Object[] var6 = params;
                    int var7 = params.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        Object param = var6[var8];
                        if (index++ > 0) {
                            key.append(':');
                        }

                        if (param == null) {
                            logger.warn("input null param for Spring cache, use default key={}", "NULL");
                            key.append("NULL");
                        } else if (ClassUtils.isPrimitiveArray(param.getClass())) {
                            int length = Array.getLength(param);

                            for(int i = 0; i < length; ++i) {
                                key.append(Array.get(param, i));
                                key.append(',');
                            }
                        } else if (!ClassUtils.isPrimitiveOrWrapper(param.getClass()) && !(param instanceof String)) {
                            logger.warn("Using an object as a cache key may lead to unexpected results. Either use @Cacheable(key=..) or implement CacheKey. Method is " + target.getClass() + "#" + method.getName());
                            key.append(param.hashCode());
                        } else {
                            key.append(param);
                        }
                    }
                    logger.info("key----->"+key.toString());

                    return key.toString();
                }
            }
        };
    }
}