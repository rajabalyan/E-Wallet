package org.gfg.WalletService.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

public class CustomUserDetailService implements UserDetailsService {

  // @Autowired
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // logic to call onboarding api to verify the username
        String response = restTemplate.getForObject("http://localhost:8081/onboarding-service/validate/user/"+username,String.class);
        System.out.println("API Response: "+response);

        if ("VALID".equals(response)){
            UserDetails userDetails = User.builder().username(username).password("").build();
            return userDetails;
        }
        return null;
    }
}
