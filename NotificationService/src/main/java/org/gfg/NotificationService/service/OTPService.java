package org.gfg.NotificationService.service;

import org.gfg.NotificationService.Worker.OTPWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private static final int OTP_LENGTH=6;

    @Autowired
    OTPWorker otpWorker;

    @Autowired
    @Qualifier("otpRedisTemplate")
    RedisTemplate<String,String> redisTemplate;

    public String sendOTP(String email){
        String otp = createOTP();
        redisTemplate.opsForValue().set(email+"-email",otp);
        System.out.println("data set in redis");
        boolean otpSent = otpWorker.sendOTP(email,otp);
        if (otpSent){
            return "SUCCESS";
        }
        return "FAILED";
    }

    public String createOTP(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=1;i<=OTP_LENGTH;i++){
            int digit = (int) (Math.random()*10);
            stringBuilder.append(digit);
        }
        return stringBuilder.toString();
    }
}
