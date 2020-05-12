package com.jch.core.types.known.tx.txns;

import com.jch.core.serialized.enums.TransactionType;
import com.jch.core.types.known.tx.Transaction;

public class TicketCreate extends Transaction {
    public TicketCreate() {
        super(TransactionType.TicketCreate);
    }
}
