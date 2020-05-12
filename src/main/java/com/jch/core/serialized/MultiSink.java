package com.jch.core.serialized;

public class MultiSink implements BytesSink {
	final private BytesSink[] sinks;

	public MultiSink(BytesSink... sinks) {
		this.sinks = sinks;
	}

	public void add(byte b) {
		for (BytesSink sink : sinks)
			sink.add(b);
	}

	public void add(byte[] b) {
		for (BytesSink sink : sinks)
			sink.add(b);
	}
}
