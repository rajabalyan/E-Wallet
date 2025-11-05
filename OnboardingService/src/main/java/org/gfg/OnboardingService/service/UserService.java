package org.gfg.OnboardingService.service;

import commons.constants.CommonConstants;
import org.gfg.OnboardingService.feign.NotificationFeign;
import org.gfg.OnboardingService.model.User;
import org.gfg.OnboardingService.model.UserStatus;
import org.gfg.OnboardingService.repository.UserRepository;
import org.gfg.OnboardingService.request.OTPRequest;
import org.gfg.OnboardingService.request.UserCreationRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class UserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    NotificationFeign notificationFeign;

    @Autowired
    RedisTemplate<String, User> userRedis;

    @Autowired
    @Qualifier("otpRedisTemplate")
    RedisTemplate<String,String> otpRedis;


    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;


    public String createUserAndCreateOTP(UserCreationRequest userCreationRequest){

        User user = User.builder().name(userCreationRequest.getName())
                .email(userCreationRequest.getEmail())
                .dob(userCreationRequest.getDob())
                .userIdentifier(userCreationRequest.getUserIdentifier())
                .userIdentifierValue(userCreationRequest.getUserIdentifierValue())
                .mobileNo(userCreationRequest.getMobileNo())
                .userStatus(UserStatus.ACTIVE).build();

        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        String message = notificationFeign.sendOTP(user.getEmail());
        String code;
        if ("success".equalsIgnoreCase(message)){
            userRedis.opsForValue().set(user.getEmail()+"-USER",user);
            System.out.println("User data saved in redis");
            code = "SUCCESS";
        }else {
            System.out.println("Notification not sent");
            code = "FAILED";
        }

        return code;
    }


    public User validateAndSaveUser(OTPRequest otpRequest){
        String email = otpRequest.getEmail();
        String code = otpRequest.getOtp();

       String savedOTP = otpRedis.opsForValue().get(email+"-email");
       String first = "\"";
       code = first+code+ "\"";
        System.out.println("code: "+code);
        if (code.equalsIgnoreCase(savedOTP)){
            System.out.println("OTP validation is successfull");
            User savedUser = userRedis.opsForValue().get(email+"-USER");
            User dbuser = userRepository.save(savedUser);
            System.out.println("data saved in database");

            // logic to send data in kafka

            String userDataForKafka = dataToSendInKafka(dbuser);

            ExecutorService executorService = new ThreadPoolExecutor(5,10,20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));

            executorService.submit(()->{
                kafkaTemplate.send(CommonConstants.USER_CREATION_TOPIC_KAFKA,userDataForKafka);
            });
            return dbuser;
        }else {
            System.out.println("OTP is incorrect");
            return null;
        }
    }

    public String dataToSendInKafka(User user){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_ID, user.getId());
        jsonObject.put(CommonConstants.USER_NAME,user.getName());
        jsonObject.put(CommonConstants.USER_EMAIL,user.getEmail());
        jsonObject.put(CommonConstants.USER_MOBILE,user.getMobileNo());
        jsonObject.put(CommonConstants.IDENTIFICATION_NUMBER,user.getUserIdentifierValue());
        jsonObject.put(CommonConstants.IDENTIFICATION_TYPE,user.getUserIdentifier());

        return jsonObject.toString();
    }


    public boolean validateCredentials(String username, String password){
       User dbuser = userRepository.findByMobileNo(username);
       if (passwordEncoder.matches(password, dbuser.getPassword())){
           System.out.println("Password match !! Login successfull");
           return true;
       }else {
           System.out.println("Invalid Credentials !!");
           return false;
       }
    }


    public String validateUser(String username){
        User dbUser = userRepository.findByMobileNo(username);
        if (dbUser!=null){
            return "VALID";
        }
        return "INVALID";
    }
}

