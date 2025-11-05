package org.gfg.NotificationService.Worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class OTPWorker {

    @Autowired
    JavaMailSender javaMailSender;

    public boolean sendOTP(String email, String otp){
        boolean isOTPSent = true;
         try {
             System.out.println("Going to send email");
             SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
             simpleMailMessage.setSubject("OTP Verification");
             simpleMailMessage.setText("Your one time OTP for account creation is "+otp+" It is valid for 5 minutes");
             simpleMailMessage.setFrom("walletgfg@gmail.com");
             simpleMailMessage.setTo(email);
             javaMailSender.send(simpleMailMessage);
             System.out.println("OTP sent to user");
         }
         catch (Exception e){
             System.out.println(e);
             isOTPSent = false;
         }
         return isOTPSent;
    }
}
