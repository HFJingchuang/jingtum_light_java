package com.jch.core.coretypes.hash;

import com.jch.core.coretypes.AccountID;
import com.jch.core.fields.Field;
import com.jch.core.fields.Hash160Field;
import com.jch.core.fields.Type;
import com.jch.core.serialized.BytesSink;

public class Hash160 extends Hash<Hash160> {
	public Hash160(byte[] bytes) {
		super(bytes, 20);
	}

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
		return Type.Hash160;
	}

	public static class Translator extends HashTranslator<Hash160> {
		public Hash160 newInstance(byte[] b) {
			return new Hash160(b);
		}

		public int byteWidth() {
			return 20;
		}

		public Hash160 fromString(String value) {
			if (value.startsWith("r")) {
				return newInstance(AccountID.fromAddress(value).bytes());
			}
			return super.fromString(value);
		}
	}

	public static Translator translate = new Translator();

	public static Hash160Field hash160Field(final Field f) {
		return new Hash160Field() {
			public Field getField() {
				return f;
			}
		};
	}

	static public Hash160Field TakerPaysIssuer = hash160Field(Field.TakerPaysIssuer);
	static public Hash160Field TakerGetsCurrency = hash160Field(Field.TakerGetsCurrency);
	static public Hash160Field TakerPaysCurrency = hash160Field(Field.TakerPaysCurrency);
	static public Hash160Field TakerGetsIssuer = hash160Field(Field.TakerGetsIssuer);
}
