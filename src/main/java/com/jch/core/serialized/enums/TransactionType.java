package com.jch.core.serialized.enums;

import java.util.TreeMap;

import com.jch.core.fields.Type;
import com.jch.core.serialized.BinaryParser;
import com.jch.core.serialized.BytesSink;
import com.jch.core.serialized.SerializedType;
import com.jch.core.serialized.TypeTranslator;
import com.jch.encoding.common.B16;

public enum TransactionType implements SerializedType {
	Invalid(-1), Payment(0), SuspendedPaymentCreate(1), // open
	SuspendedPaymentFinish(2), AccountSet(3), SuspendedPaymentCancel(4), // open
	SetRegularKey(5), NickNameSet(6), // open
	OfferCreate(7), OfferCancel(8), unused(9), TicketCreate(10), TicketCancel(11), SignerListSet(12), TrustSet(20),RelationSet(30), EnableAmendment(
	        100), SetFee(101);
	public int asInteger() {
		return ord;
	}
	
	final int ord;
	
	TransactionType(int i) {
		ord = i;
	}
	
	public Type type() {
		return Type.UInt16;
	}
	
	static private TreeMap<Integer, TransactionType> byCode = new TreeMap<Integer, TransactionType>();
	static {
		for (Object a : TransactionType.values()) {
			TransactionType f = (TransactionType) a;
			byCode.put(f.ord, f);
		}
	}
	
	static public TransactionType fromNumber(Number i) {
		return byCode.get(i.intValue());
	}
	
	// SeralizedType interface
	public byte[] toBytes() {
		// TODO: bytes
		return new byte[] { (byte) (ord >> 8), (byte) (ord & 0xFF) };
	}
	
	public Object toJSON() {
		return toString();
	}
	
	public String toHex() {
		return B16.toString(toBytes());
	}
	
	public void toBytesSink(BytesSink to) {
		to.add(toBytes());
	}
	
	public static class Translator extends TypeTranslator<TransactionType> {
		@Override
		public TransactionType fromParser(BinaryParser parser, Integer hint) {
			byte[] read = parser.read(2);
			return fromNumber((read[0] << 8) | read[1]);
		}
		
		@Override
		public TransactionType fromInteger(int integer) {
			return fromNumber(integer);
		}
		
		@Override
		public TransactionType fromString(String value) {
			return TransactionType.valueOf(value);
		}
	}
	
	public static Translator translate = new Translator();
}
