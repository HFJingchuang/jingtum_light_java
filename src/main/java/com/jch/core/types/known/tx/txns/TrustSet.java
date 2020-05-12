package com.jch.core.types.known.tx.txns;

import com.jch.core.coretypes.Amount;
import com.jch.core.coretypes.uint.UInt32;
import com.jch.core.fields.Field;
import com.jch.core.serialized.enums.TransactionType;
import com.jch.core.types.known.tx.Transaction;

public class TrustSet extends Transaction {

	public TrustSet() {
		super(TransactionType.TrustSet);
	}

	public UInt32 qualityIn() {
		return get(UInt32.QualityIn);
	}

	public UInt32 qualityOut() {
		return get(UInt32.QualityOut);
	}

	public Amount limitAmount() {
		return get(Amount.LimitAmount);
	}

	public void qualityIn(UInt32 val) {
		put(Field.QualityIn, val);
	}

	public void qualityOut(UInt32 val) {
		put(Field.QualityOut, val);
	}

	public void limitAmount(Amount val) {
		put(Field.LimitAmount, val);
	}
}