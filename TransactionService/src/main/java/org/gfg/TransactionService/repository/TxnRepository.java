package org.gfg.TransactionService.repository;

import commons.models.TxnStatus;
import jakarta.transaction.Transactional;
import org.gfg.TransactionService.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TxnRepository  extends JpaRepository<Transaction,Integer> {

    @Modifying
    @Transactional
    @Query("update transaction t set t.txnStatus=:txnStatus, t.txnStatusMessage=:txnMessage where t.txnId=:txnId")
    void updateTxn(String txnId, TxnStatus txnStatus,String txnMessage);


    List<Transaction> findBySenderOrReceiver(String sender, String receiver);
}
