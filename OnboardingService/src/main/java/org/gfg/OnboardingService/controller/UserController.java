package org.gfg.OnboardingService.controller;

import commons.jwt.JWTUtil;
import jakarta.validation.Valid;
import org.gfg.OnboardingService.model.User;
import org.gfg.OnboardingService.request.LoginRequest;
import org.gfg.OnboardingService.request.OTPRequest;
import org.gfg.OnboardingService.request.UserCreationRequest;
import org.gfg.OnboardingService.response.UserCreationResponse;
import org.gfg.OnboardingService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding-service")
public class UserController {

    @Autowired
    UserService userService;

    JWTUtil jwtUtil = new JWTUtil();

    @PostMapping("/create/user")
    public ResponseEntity<UserCreationResponse> createUser(@Valid @RequestBody UserCreationRequest userCreationRequest){

       String code = userService.createUserAndCreateOTP(userCreationRequest);
       String message ;
       if ("success".equalsIgnoreCase(code)){
           message = "OTP successfully sent to user";
       }else {
           message  = "OTP not sent to user";
       }

       UserCreationResponse userCreationResponse = new UserCreationResponse();
       userCreationResponse.setEmail(userCreationRequest.getEmail());
       userCreationResponse.setCode(code);
       userCreationResponse.setMessage(message);

       return new ResponseEntity<>(userCreationResponse, HttpStatus.OK);
    }


    @PostMapping("/validate/otp")
    public ResponseEntity<User> validateOTP(@RequestBody OTPRequest otpRequest){
        User user = userService.validateAndSaveUser(otpRequest);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public String userLogin(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (userService.validateCredentials(username,password)){
            return jwtUtil.createToken(username,"NORMAL");
        }else {
            return "Invalid Credentials";
        }
    }

    @GetMapping("/validate/user/{username}")
    public String validateUser(@PathVariable("username") String username){
        return userService.validateUser(username);
    }
}
