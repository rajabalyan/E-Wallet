package org.gfg.TransactionService.consumer;

import commons.constants.CommonConstants;
import commons.models.TxnStatus;
import org.gfg.TransactionService.repository.TxnRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
public class TransactionConsumer {

    @Autowired
    TxnRepository txnRepository;

    @KafkaListener(topics = CommonConstants.TXN_UPDATE_KAFKA_TOPIC, groupId = "txn-update-group")
    public void listenUpdatedTransaction(String data){
        System.out.println("Updated Txn Received: "+data);
        JSONObject jsonObject = new JSONObject(data);

        TxnStatus txnStatus = jsonObject.getEnum(TxnStatus.class,CommonConstants.TXN_STATUS);
        String txnStatusMessage = jsonObject.optString(CommonConstants.TXN_STATUS_MESSAGE);
        String txnId = jsonObject.optString(CommonConstants.TXN_ID);

        txnRepository.updateTxn(txnId,txnStatus,txnStatusMessage);

        System.out.println("Final txn status upadted");
    }


}
