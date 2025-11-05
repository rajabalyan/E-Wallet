package org.gfg.TransactionService.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxnRequest {
    double amount;
    String purpose;
    String receiver;
}
