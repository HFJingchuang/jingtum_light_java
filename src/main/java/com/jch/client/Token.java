package com.jch.client;

/**
 * 代币余额信息
 */
public class Token {
	private String issuer;// 发行人
	private String value;// 总余额
	private String currency;// 货币种类
	private String freezed;// 冻结余额

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setfreezed(String balance) {
		this.freezed = balance;
	}

	public String getreezed() {
		return this.freezed;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIssuer() {
		return this.issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

}
