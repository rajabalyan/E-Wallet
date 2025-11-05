package org.gfg.NotificationService.consumer;

import commons.constants.CommonConstants;
import commons.models.UserIdentifier;
import org.gfg.NotificationService.Worker.EmailNotificationWorker;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    EmailNotificationWorker emailNotificationWorker;

    @KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC_KAFKA, groupId = "email-group")
    public void listenNewlyCreatedUser(String data){
        System.out.println("data consumed: "+data);
        JSONObject jsonObject = new JSONObject(data);
        String name = jsonObject.optString(CommonConstants.USER_NAME);
        String email = jsonObject.optString(CommonConstants.USER_EMAIL);
        UserIdentifier userIdentifier = jsonObject.optEnum(UserIdentifier.class, CommonConstants.IDENTIFICATION_TYPE);
        String userIdentifierNumber = jsonObject.optString(CommonConstants.IDENTIFICATION_NUMBER);

        emailNotificationWorker.sendEmailNotification(name,email,userIdentifierNumber,userIdentifier.name());
    }
}
