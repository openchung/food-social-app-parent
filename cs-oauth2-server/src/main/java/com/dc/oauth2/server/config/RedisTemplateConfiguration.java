package com.dc.oauth2.server.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisTemplateConfiguration {
    /*Cluster*/
    //    @Bean
//    public RedisConnectionFactory RedisConnectionFactory () {
//        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
//        // 節點
//        List<RedisNode> redisNodeList = new ArrayList<>();
//        redisNodeList.add(new RedisNode("192.168.5.139", 6371));
//        redisNodeList.add(new RedisNode("192.168.5.139", 6372));
//        redisNodeList.add(new RedisNode("192.168.5.144", 6373));
//        redisNodeList.add(new RedisNode("192.168.5.144", 6374));
//        redisNodeList.add(new RedisNode("192.168.5.147", 6375));
//        redisNodeList.add(new RedisNode("192.168.5.147", 6376));
//
//        clusterConfiguration.setClusterNodes(redisNodeList);
//
//        //Redis 命令執行時最多轉發次數
//        clusterConfiguration.setMaxRedirects(5);
//        //Password
//        clusterConfiguration.setPassword("123456");
//        return new LettuceConnectionFactory(clusterConfiguration);
//    }

    /*Sentinal*/
//    @Bean
//    public RedisConnectionFactory RedisConnectionFactory () {
//        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration()
//                .master("mymaster")
//                .sentinel("192.168.5.139", 26379)
//                .sentinel("192.168.5.144", 26379)
//                .sentinel("192.168.5.147", 26379);
//        sentinelConfiguration.setDatabase(1);
//        sentinelConfiguration.setPassword("123456");
//        return new LettuceConnectionFactory(sentinelConfiguration);
//    }
     /**
     * redisTemplate 序列化使用的jdkSerializeable, 存儲二進制字節碼, 所以自定義序列化類
     * @param redisConnectionFactory
     * @return
     */
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        // 使用Jackson2JsonRedisSerialize 替換默認序列化
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//        // 設置key和value的序列化規則
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

}