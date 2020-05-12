package com.jch.core.types.known.tx.txns;

import com.jch.core.coretypes.Amount;
import com.jch.core.fields.Field;
import com.jch.core.serialized.enums.TransactionType;
import com.jch.core.types.known.tx.Transaction;

public class RelationSet extends Transaction {
	
    public RelationSet() {
        super(TransactionType.RelationSet);
    }
    

    public Amount limitAmount() {return get(Amount.LimitAmount);}
    public void limitAmount(Amount val) {put(Field.LimitAmount, val);}
    
}