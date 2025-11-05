package org.gfg.WalletService.service;

import commons.constants.CommonConstants;
import commons.models.TxnStatus;
import jakarta.transaction.Transactional;
import org.gfg.WalletService.model.Wallet;
import org.gfg.WalletService.model.WalletStatus;
import org.gfg.WalletService.repository.WalletRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Value("${wallet.initial.amount}")
    private String walletBalance;

    TxnStatus txnStatus;
    String txnStatusMessage;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public void createWalletAccount(Wallet wallet){
        wallet.setWalletStatus(WalletStatus.ACTIVE);
        wallet.setBalance(Double.parseDouble(walletBalance));

        walletRepository.save(wallet);

        System.out.println("Wallet account created");
    }


    public void updateTxn(String sender,String receiver,double amount, String txnId){
        Wallet senderWallet = walletRepository.findByMobileNo(sender);
        Wallet receiverWallet = walletRepository.findByMobileNo(receiver);


        if (senderWallet==null || !senderWallet.getWalletStatus().equals(WalletStatus.ACTIVE)){
            txnStatus = TxnStatus.FAILED;
            txnStatusMessage = "Sender wallet does not exist";
        }else if (receiverWallet==null || !receiverWallet.getWalletStatus().equals(WalletStatus.ACTIVE)){
            txnStatus = TxnStatus.FAILED;
            txnStatusMessage = "Receiver waller does not exist";
        }
        else {
            if (senderWallet.getBalance()>=amount){
                boolean txnProcessed = processTransaction(senderWallet.getMobileNo(),receiverWallet.getMobileNo(),amount);
                if (txnProcessed){
                    txnStatus = TxnStatus.SUCCESS;
                    txnStatusMessage = "Transaction is successful";
                }else {
                    txnStatus = TxnStatus.PENDING;
                    txnStatusMessage = "Transaction is pending";
                }
            }else {
                txnStatus = TxnStatus.FAILED;
                txnStatusMessage = "Insufficient Balance";
            }
        }

        // send the updated transaction to the transaction service
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.TXN_ID,txnId);
        jsonObject.put(CommonConstants.TXN_STATUS,txnStatus);
        jsonObject.put(CommonConstants.TXN_STATUS_MESSAGE,txnStatusMessage);

        kafkaTemplate.send(CommonConstants.TXN_UPDATE_KAFKA_TOPIC,jsonObject.toString());

        System.out.println("Updated details sent to kafka");
    }

    @Transactional
    public boolean processTransaction(String sender,String receiver, double amount){
        boolean txnProcessed = true;
        try {
            walletRepository.updateUserBalance(sender,-amount);
            walletRepository.updateUserBalance(receiver,amount);
        }
        catch (Exception e){
            txnProcessed = false;
        }
        return txnProcessed;
    }


    public String getWalletBalance(String sender){
       Wallet wallet = walletRepository.findByMobileNo(sender);
       return Double.toString(wallet.getBalance());
    }
}
