package com.jch.core.serialized;

import com.jch.core.fields.Type;

public interface SerializedType {
	Object toJSON();

	byte[] toBytes();

	String toHex();

	void toBytesSink(BytesSink to);

	Type type();
}
