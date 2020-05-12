package com.jch.core.types.known.tx.txns;

import com.jch.core.coretypes.uint.UInt32;
import com.jch.core.fields.Field;
import com.jch.core.serialized.enums.TransactionType;
import com.jch.core.types.known.tx.Transaction;

public class OfferCancel extends Transaction {
    public OfferCancel() {
        super(TransactionType.OfferCancel);
    }
    
    public UInt32 offerSequence() {return get(UInt32.OfferSequence);}
    public void offerSequence(UInt32 val) {put(Field.OfferSequence, val);}
}
