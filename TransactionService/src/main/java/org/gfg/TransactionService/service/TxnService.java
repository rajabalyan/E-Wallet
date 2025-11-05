package org.gfg.TransactionService.service;

import commons.constants.CommonConstants;
import commons.models.TxnStatus;
import org.gfg.TransactionService.model.ExchangeType;
import org.gfg.TransactionService.model.Transaction;
import org.gfg.TransactionService.repository.TxnRepository;
import org.gfg.TransactionService.request.TxnRequest;
import org.gfg.TransactionService.response.TransactionResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TxnService {

    @Autowired
    TxnRepository txnRepository;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public String initiateTransaction(String sender, TxnRequest txnRequest){
        Transaction transaction = Transaction.builder()
                .txnStatus(TxnStatus.INITIATED)
                .txnStatusMessage("Transaction initiated")
                .purpose(txnRequest.getPurpose())
                .amount(txnRequest.getAmount())
                .receiver(txnRequest.getReceiver())
                .sender(sender)
                .build();

        String txnId = UUID.randomUUID().toString();
        transaction.setTxnId(txnId);

        txnRepository.save(transaction);

        JSONObject txnData = new JSONObject();
        txnData.put(CommonConstants.SENDER_ID, sender);
        txnData.put(CommonConstants.RECEIVER_ID,txnRequest.getReceiver());
        txnData.put(CommonConstants.TXN_ID,txnId);
        txnData.put(CommonConstants.TXN_AMOUNT,txnRequest.getAmount());

        kafkaTemplate.send(CommonConstants.TXN_INITIATION_TOPIC,txnData.toString());

        System.out.println("txn data send to kafka");

        return txnId;
    }


    public List<TransactionResponse> getTransactionHistory(String sender){
        List<Transaction> transactions = txnRepository.findBySenderOrReceiver(sender,sender);
        List<TransactionResponse> ans = new ArrayList<>();

        for (Transaction t: transactions){
            TransactionResponse transactionResponse = new TransactionResponse();
            if (t.getSender().equals(sender)){
                transactionResponse.setExchangeType(ExchangeType.DEBIT);
            }else {
                transactionResponse.setExchangeType(ExchangeType.CREDIT);
            }
            transactionResponse.setTxnId(t.getTxnId());
            transactionResponse.setAmount(t.getAmount());
            transactionResponse.setExchangeNumber(t.getReceiver());
            transactionResponse.setTxnStatus(t.getTxnStatus());
            transactionResponse.setExchangeTime(t.getCreatedOn());
            ans.add(transactionResponse);
        }

        return ans;
    }
}
