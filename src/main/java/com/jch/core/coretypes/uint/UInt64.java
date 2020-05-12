package com.jch.core.coretypes.uint;

import java.math.BigInteger;

import com.jch.core.fields.Field;
import com.jch.core.fields.Type;
import com.jch.core.fields.UInt64Field;
import com.jch.core.serialized.BytesSink;
import com.jch.core.serialized.TypeTranslator;

public class UInt64 extends UInt<UInt64> {
	public final static UInt64 ZERO = new UInt64(0);

	public static TypeTranslator<UInt64> translate = new UINTTranslator<UInt64>() {

		public UInt64 newInstance(BigInteger i) {
			return new UInt64(i);
		}

		public int byteWidth() {
			return 8;
		}
	};

	public UInt64(byte[] bytes) {
		super(bytes);
	}

	public UInt64(BigInteger value) {
		super(value);
	}

	public UInt64(Number s) {
		super(s);
	}

	public UInt64(String s) {
		super(s);
	}

	public UInt64(String s, int radix) {
		super(s, radix);
	}

	@Override
	public int getByteWidth() {
		return 8;
	}

	@Override
	public UInt64 instanceFrom(BigInteger n) {
		return new UInt64(n);
	}

	@Override
	public BigInteger value() {
		return bigInteger();
	}

	private UInt64() {
	}

	private static UInt64Field int64Field(final Field f) {
		return new UInt64Field() {
			public Field getField() {
				return f;
			}
		};
	}

	static public UInt64Field IndexNext = int64Field(Field.IndexNext);
	static public UInt64Field IndexPrevious = int64Field(Field.IndexPrevious);
	static public UInt64Field BookNode = int64Field(Field.BookNode);
	static public UInt64Field OwnerNode = int64Field(Field.OwnerNode);
	static public UInt64Field BaseFee = int64Field(Field.BaseFee);
	static public UInt64Field ExchangeRate = int64Field(Field.ExchangeRate);
	static public UInt64Field LowNode = int64Field(Field.LowNode);
	static public UInt64Field HighNode = int64Field(Field.HighNode);

	public Object toJSON() {
		return translate.toJSON(this);
	}

	public byte[] toBytes() {
		return translate.toBytes(this);
	}

	public String toHex() {
		return translate.toHex(this);
	}

	public void toBytesSink(BytesSink to) {
		translate.toBytesSink(this, to);
	}

	public Type type() {
		return Type.UInt64;
	}
}
