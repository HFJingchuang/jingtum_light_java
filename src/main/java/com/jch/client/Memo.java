package com.jch.client;

import com.jch.utils.Utils;

public class Memo {
	private String memoData;
	private String memoType;

	public Memo() {
	}

	public String getMemoData() {
		return memoData;
	}

	public void setMemoData(String memoData) {
		this.memoData = Utils.hexStrToStr(memoData);
		;
	}

	public String getMemoType() {
		return memoType;
	}

	public void setMemoType(String memoType) {
		this.memoType = memoType;
	}
}
