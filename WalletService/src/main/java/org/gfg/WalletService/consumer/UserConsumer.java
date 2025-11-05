package org.gfg.WalletService.consumer;

import commons.constants.CommonConstants;
import commons.models.UserIdentifier;
import org.gfg.WalletService.model.Wallet;
import org.gfg.WalletService.service.WalletService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    WalletService walletService;


    @KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC_KAFKA, groupId = "wallet-group")
    public void listenNewlyCreatedUser(String data){
        System.out.println("Data received: "+data);
        JSONObject jsonObject = new JSONObject(data);
        String name = jsonObject.optString(CommonConstants.USER_NAME);
        UserIdentifier userIdentifier = jsonObject.optEnum(UserIdentifier.class, CommonConstants.IDENTIFICATION_TYPE);
        String userIdentifierNumber = jsonObject.optString(CommonConstants.IDENTIFICATION_NUMBER);
        int userId = jsonObject.optInt(CommonConstants.USER_ID);
        String mobileNo = jsonObject.optString(CommonConstants.USER_MOBILE);

        Wallet wallet = Wallet.builder().
                name(name)
                .userId(userId)
                .mobileNo(mobileNo)
                .userIdentifier(userIdentifier)
                .userIdentifierValue(userIdentifierNumber)
                .build();

        walletService.createWalletAccount(wallet);

    }
}
