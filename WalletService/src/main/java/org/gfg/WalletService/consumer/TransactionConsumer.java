package org.gfg.WalletService.consumer;

import commons.constants.CommonConstants;
import org.gfg.WalletService.service.WalletService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    @Autowired
    WalletService walletService;


    @KafkaListener(topics = CommonConstants.TXN_INITIATION_TOPIC, groupId = "txn-init-group")
    public void receiveTransaction(String data){
        System.out.println("Data received: "+data);
        JSONObject jsonObject = new JSONObject(data);
        String sender = jsonObject.optString(CommonConstants.SENDER_ID);
        String receiver = jsonObject.optString(CommonConstants.RECEIVER_ID);
        double amount = jsonObject.optDouble(CommonConstants.TXN_AMOUNT);
        String txnId = jsonObject.optString(CommonConstants.TXN_ID);

        walletService.updateTxn(sender,receiver,amount,txnId);

    }
}
