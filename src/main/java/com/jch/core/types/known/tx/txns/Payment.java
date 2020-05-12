package com.jch.core.types.known.tx.txns;

import java.math.BigDecimal;

import com.jch.client.AmountInfo;
import com.jch.core.coretypes.AccountID;
import com.jch.core.coretypes.Amount;
import com.jch.core.coretypes.Currency;
import com.jch.core.coretypes.PathSet;
import com.jch.core.coretypes.hash.Hash256;
import com.jch.core.coretypes.uint.UInt32;
import com.jch.core.fields.Field;
import com.jch.core.runtime.Value;
import com.jch.core.serialized.enums.TransactionType;
import com.jch.core.types.known.tx.Transaction;
import com.jch.utils.Utils;

public class Payment extends Transaction {
	public Payment() {
		super(TransactionType.Payment);
	}

	public UInt32 destinationTag() {
		return get(UInt32.DestinationTag);
	}

	public Hash256 invoiceID() {
		return get(Hash256.InvoiceID);
	}

	public Amount amount() {
		return get(Amount.Amount);
	}

	public Amount sendMax() {
		return get(Amount.SendMax);
	}

	public AccountID destination() {
		return get(AccountID.Destination);
	}

	public PathSet paths() {
		return get(PathSet.Paths);
	}

	public void destinationTag(UInt32 val) {
		put(Field.DestinationTag, val);
	}

	public void invoiceID(Hash256 val) {
		put(Field.InvoiceID, val);
	}

	public void amount(Amount val) {
		put(Field.Amount, val);
	}

	public void sendMax(Amount val) {
		put(Field.SendMax, val);
	}

	public void destination(AccountID val) {
		put(Field.Destination, val);
	}

	public void paths(PathSet val) {
		put(Field.Paths, val);
	}

	public void setAmountInfo(AmountInfo amountInfo) {
		Object o;
		try {
			o = Utils.toAmount(amountInfo);
			if (Value.typeOf(o) == Value.STRING) {
				this.as(Amount.Amount, o);
			} else {
				BigDecimal temp = new BigDecimal(amountInfo.getValue());
				Amount amount = new Amount(temp, Currency.fromString(amountInfo.getCurrency()),
						AccountID.fromAddress(amountInfo.getIssuer()));
				this.as(Amount.Amount, amount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
