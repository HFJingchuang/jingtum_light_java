package com.jch.core.types.known.tx.txns;

public interface ICallback {

	void onResponse(Object response);

	void onFail(Exception e);
}
