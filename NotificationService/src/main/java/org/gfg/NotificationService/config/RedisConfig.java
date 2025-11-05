package org.gfg.NotificationService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> otpRedisTemplate(){
        RedisTemplate<String,String> otpRedisTemplate = new RedisTemplate<>();
        otpRedisTemplate.setKeySerializer(new StringRedisSerializer());
        otpRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        otpRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return otpRedisTemplate;
    }

    public RedisConnectionFactory redisConnectionFactory(){
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.setHostName("redis-15111.c264.ap-south-1-1.ec2.redns.redis-cloud.com");
        lettuceConnectionFactory.setPort(15111);
        lettuceConnectionFactory.setPassword("vgpt2ghQF5GwnvFdEuOAOeAOry5JcLVB");
        lettuceConnectionFactory.start();
        return lettuceConnectionFactory;
    }
}
