package com.jch.core.types.known.tx.txns;

import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jccdex.rpc.api.JccConfig;
import com.jccdex.rpc.api.JccdexExchange;
import com.jccdex.rpc.base.JCallback;
import com.jccdex.rpc.url.JccdexUrl;
import com.jch.client.Token;
import com.jch.config.Config;
import com.jch.core.coretypes.Amount;
import com.jch.core.coretypes.uint.UInt32;
import com.jch.core.types.known.tx.signed.SignedTransaction;

public class Transaction {

	private String mConfigUrl = "uploadletsdex.swtc.top";

	private Transaction() {
	}

	public static Transaction getInstance() {
		return Singleton.INSTANCE.getInstance();
	}

	private static enum Singleton {
		INSTANCE;

		private Transaction singleton;

		private Singleton() {
			singleton = new Transaction();
		}

		public Transaction getInstance() {
			return singleton;
		}
	}

	public void setConfigUrl(String configUrl) {
		this.mConfigUrl = configUrl;
	}

	public void transfer(final Payment payment, final String secret, final ICallback callBack) {
		// 获取井畅RPC地址
		JccConfig config = JccConfig.getInstance();
		JccdexUrl jccUrl = new JccdexUrl(this.mConfigUrl, true);
		config.setmBaseUrl(jccUrl);
		config.requestConfig(new JCallback() {

			public void onFail(Exception arg0) {
				callBack.onFail(arg0);
			}

			public void onResponse(String arg0, String arg1) {
				JSONArray exHosts = JSONObject.parseObject(arg1).getJSONArray("exHosts");
				JccdexExchange exchange = JccdexExchange.getInstance();
				JccdexUrl jccUrl = new JccdexUrl(exHosts.getString(new Random().nextInt(exHosts.size())), true);
				exchange.setmBaseUrl(jccUrl);
				try {
					// 获取转账地址序列号
					int sequence = exchange.requestSequence(payment.account().address);
					payment.as(Amount.Fee, String.valueOf(Config.FEE));
					payment.sequence(new UInt32(sequence));
					payment.flags(new UInt32(0));
					SignedTransaction signedTx;
					// 本地签名
					signedTx = payment.sign(secret);
					// 发起转账交易
					exchange.transferToken(signedTx.tx_blob, new JCallback() {

						public void onFail(Exception arg0) {
							callBack.onFail(arg0);
						}

						public void onResponse(String arg0, String arg1) {
							if ("0".equals(arg0)) {
								callBack.onResponse(
										JSONObject.parseObject(arg1).getJSONObject("data").getString("hash"));
							} else {
								callBack.onFail(new Exception(JSONObject.parseObject(arg1).getString("msg")));
							}
						}

					});
				} catch (Exception e) {
					callBack.onFail(e);
				}

			}
		});
	}

	public void getBalance(final String address, final ICallback callBack) {
		// 获取井畅RPC地址
		JccConfig config = JccConfig.getInstance();
		JccdexUrl jccUrl = new JccdexUrl(this.mConfigUrl, true);
		config.setmBaseUrl(jccUrl);
		config.requestConfig(new JCallback() {

			public void onFail(Exception arg0) {
				callBack.onFail(arg0);
			}

			public void onResponse(String arg0, String arg1) {
				JSONArray exHosts = JSONObject.parseObject(arg1).getJSONArray("exHosts");
				JccdexExchange exchange = JccdexExchange.getInstance();
				JccdexUrl jccUrl = new JccdexUrl(exHosts.getString(new Random().nextInt(exHosts.size())), true);
				exchange.setmBaseUrl(jccUrl);
				try {
					// 获取转账地址序列号
					exchange.requestBalance(address, new JCallback() {

						public void onFail(Exception arg0) {
							callBack.onFail(arg0);

						}

						public void onResponse(String arg0, String arg1) {
							if ("0".equals(arg0)) {
								callBack.onResponse(JSONObject
										.parseArray(JSONObject.parseObject(arg1).getString("data"), Token.class));
							} else {
								callBack.onFail(new Exception(JSONObject.parseObject(arg1).getString("msg")));
							}
						}
					});
				} catch (Exception e) {
					callBack.onFail(e);
				}

			}
		});
	}

}
