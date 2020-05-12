package com.jch.core.coretypes.hash;

import com.jch.core.fields.Field;
import com.jch.core.fields.Hash128Field;
import com.jch.core.fields.Type;
import com.jch.core.serialized.BytesSink;

public class Hash128 extends Hash<Hash128> {
	public Hash128(byte[] bytes) {
		super(bytes, 16);
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
		return Type.Hash128;
	}

	public static class Translator extends HashTranslator<Hash128> {
		@Override
		public Hash128 newInstance(byte[] b) {
			return new Hash128(b);
		}

		@Override
		public int byteWidth() {
			return 16;
		}
	}

	public static Translator translate = new Translator();

	public static Hash128Field hash128Field(final Field f) {
		return new Hash128Field() {
			public Field getField() {
				return f;
			}
		};
	}

	static public Hash128Field EmailHash = hash128Field(Field.EmailHash);

}
