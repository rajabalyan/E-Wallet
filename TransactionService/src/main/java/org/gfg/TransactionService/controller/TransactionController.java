package org.gfg.TransactionService.controller;

import org.gfg.TransactionService.request.TxnRequest;
import org.gfg.TransactionService.response.TransactionResponse;
import org.gfg.TransactionService.service.TxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/txn-service")
public class TransactionController {

    @Autowired
    TxnService txnService;


    @PostMapping("/initiate/transaction")
    public String initiateTransaction(@RequestBody TxnRequest txnRequest){
       UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       String sender = userDetails.getUsername();

       return txnService.initiateTransaction(sender,txnRequest);
    }

    @GetMapping("/get/transaction/history")
    public List<TransactionResponse> transactionHistory(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = userDetails.getUsername();

        return txnService.getTransactionHistory(sender);

    }
}
