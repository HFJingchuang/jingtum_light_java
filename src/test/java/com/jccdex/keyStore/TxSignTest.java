package com.jccdex.keyStore;

import java.util.ArrayList;
import java.util.List;

import com.jch.client.AmountInfo;
import com.jch.client.Token;
import com.jch.config.Config;
import com.jch.core.coretypes.AccountID;
import com.jch.core.coretypes.Amount;
import com.jch.core.coretypes.uint.UInt32;
import com.jch.core.types.known.tx.signed.SignedTransaction;
import com.jch.core.types.known.tx.txns.ICallback;
import com.jch.core.types.known.tx.txns.Payment;
import com.jch.core.types.known.tx.txns.Transaction;

public class TxSignTest {

	public static void main(String[] args) {
//		Sign();
		Transfer();
//		getBalance();
	}

	public static void Sign() throws Exception {
		String account = "j3UcBBbes7HFgmTLmGkEQQShM2jdHbdGAe";
		String to = "jNn89aY84G23onFXupUd7bkMode6aKYMt8";
		String secret = "ssWiEpky7Bgj5GFrexxpKexYkeuUv";
		AmountInfo amountInfo = new AmountInfo();
		amountInfo.setCurrency("SWT");
		amountInfo.setValue("0.01");
		amountInfo.setIssuer("");

		Payment payment = new Payment();
		payment.as(AccountID.Account, account);
		payment.as(AccountID.Destination, to);
		payment.setAmountInfo(amountInfo);
		payment.as(Amount.Fee, String.valueOf(Config.FEE));
		payment.sequence(new UInt32(1));
		payment.flags(new UInt32(0));
		List<String> memos = new ArrayList<String>();
		memos.add("SWT转账");
		memos.add("测试数据1");
		memos.add("测试数据2");
		payment.addMemo(memos);
		SignedTransaction signedTx = payment.sign(secret);
		System.out.println("tx_blob:" + signedTx.tx_blob);

		// 交易CNY
		amountInfo = new AmountInfo();
		amountInfo.setCurrency("CNY");
		amountInfo.setValue("0.01");
		amountInfo.setIssuer("jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or");

		payment = new Payment();
		payment.as(AccountID.Account, account);
		payment.as(AccountID.Destination, to);
		payment.setAmountInfo(amountInfo);
		payment.as(Amount.Fee, String.valueOf(Config.FEE));
		payment.sequence(new UInt32(1));
		payment.flags(new UInt32(0));
		payment.addMemo(memos);
		signedTx = payment.sign(secret);
		System.out.println("tx_blob:" + signedTx.tx_blob);
	}

	public static void Transfer() {
		String account = "jBvrdYc6G437hipoCiEpTwrWSRBS2ahXN6";
		String to = "jKBCwv4EcyvYtD4PafP17PLpnnZ16szQsC";
		String secret = "snBPyRRpE56ea4QGCpTMVTQWoirT2";
		AmountInfo amountInfo = new AmountInfo();
		amountInfo.setCurrency("SWT");
		amountInfo.setValue("0.001");
		amountInfo.setIssuer("");

		Payment payment = new Payment();
		payment.as(AccountID.Account, account);
		payment.as(AccountID.Destination, to);
		payment.setAmountInfo(amountInfo);
		List<String> memos = new ArrayList<String>();
		memos.add("测试SWT转账");
		payment.addMemo(memos);
		Transaction.getInstance().transfer(payment, secret, new ICallback() {

			public void onFail(Exception arg0) {
				System.out.println("转账失败，失败原因:" + arg0.getMessage());
			}

			public void onResponse(Object response) {
				System.out.println("转账成功，转账哈希:" + (String) response);
			}
		});
	}

	public static void getBalance() {
		String address = "jMCPG9cCGU8wj93xMhKbLNbfvQZNJdEmem";
		Transaction.getInstance().getBalance(address, new ICallback() {

			public void onFail(Exception arg0) {
				System.out.println("转账失败，失败原因:" + arg0.getMessage());

			}

			public void onResponse(Object response) {
				if (response != null) {
					List<Token> tokens = (List) response;
					for (int i = 0; i < tokens.size(); i++) {
						System.out.println("代币名称:" + tokens.get(i).getCurrency());
						System.out.println("代币银关:" + tokens.get(i).getIssuer());
						System.out.println("总余额:" + tokens.get(i).getValue());
						System.out.println("冻结余额:" + tokens.get(i).getreezed());
						System.out.println("=====================================");
					}
				}
			}
		});
	}
}
