package org.gfg.OnboardingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "NOTIFICATION", url = "http://localhost:8082/otp")
public interface NotificationFeign {

    @PostMapping("/send/otp/{email}")
     String sendOTP(@PathVariable("email") String email);
}
