package org.gfg.TransactionService.response;

import commons.models.TxnStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gfg.TransactionService.model.ExchangeType;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionResponse {

    String txnId;
    double amount;
    String exchangeNumber;
    ExchangeType exchangeType;
    TxnStatus txnStatus;
    Date exchangeTime;
}
