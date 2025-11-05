package org.gfg.NotificationService.controller;

import org.gfg.NotificationService.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
public class OTPController {

    @Autowired
    OTPService otpService;

    @PostMapping("/send/otp/{email}")
    public String createAndSendOTP(@PathVariable("email") String email){
        return otpService.sendOTP(email);
    }
}
