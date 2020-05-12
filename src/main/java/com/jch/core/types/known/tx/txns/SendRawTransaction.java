package com.jch.core.types.known.tx.txns;

import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jccdex.rpc.api.JccConfig;
import com.jccdex.rpc.api.JccdexExchange;
import com.jccdex.rpc.base.JCallback;
import com.jccdex.rpc.url.JccdexUrl;
import com.jch.config.Config;
import com.jch.core.coretypes.Amount;
import com.jch.core.coretypes.uint.UInt32;
import com.jch.core.types.known.tx.signed.SignedTransaction;

public class SendRawTransaction {

	private String mConfigUrl = "app.weidex.vip";

	private SendRawTransaction() {
	}

	public static SendRawTransaction getInstance() {
		return Singleton.INSTANCE.getInstance();
	}

	private static enum Singleton {
		INSTANCE;

		private SendRawTransaction singleton;

		private Singleton() {
			singleton = new SendRawTransaction();
		}

		public SendRawTransaction getInstance() {
			return singleton;
		}
	}

	public void setConfigUrl(String configUrl) {
		this.mConfigUrl = configUrl;
	}

	public void transfer(final Payment payment, final String secret, final JCallback callBack) {
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
					exchange.transferToken(signedTx.tx_blob, callBack);
				} catch (Exception e) {
					callBack.onFail(e);
				}
				

			}
		});
	}

}
