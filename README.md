# jingtum_light_java

[![](https://jitpack.io/v/HFJingchuang/jingtum_light_java.svg)](https://jitpack.io/#HFJingchuang/jingtum_light_java)

轻量井通java库，包含钱包创建、keystore、余额查询、本地签名、转账

## 引入

### Step 1. Add the JitPack repository to your build file
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
### Step 2. Add the dependency
```xml
<dependency>
  <groupId>com.github.HFJingchuang</groupId>
  <artifactId>jingtum_light_java</artifactId>
  <version>1.0.2</version>
</dependency>
```

## 使用

### 钱包类
```java
    // 创建钱包
    Wallet wallet = Wallet.generate();
    String address = wallet.getAddress();
    String secret = wallet.getSecret();
    System.out.println("address:" + address);
    System.out.println("secret:" + secret);

    // 钱包地址、密钥校验
    boolean isValidAddress = Wallet.isValidAddress(address);
    boolean isValidSecret = Wallet.isValidSecret(secret);
    System.out.println("isValidAddress:" + isValidAddress);
    System.out.println("isValidSecret:" + isValidSecret);

    // 根据密钥生成钱包地址
    Wallet wallet2 = Wallet.fromSecret(secret);
    String address2 = wallet2.getAddress();
    Assert.assertEquals(address, address2);
```

### KeyStore类

```java
    // keystore密码
    String password = "Test123567";
    Wallet wallet = Wallet.generate();
    String address = wallet.getAddress();
    String secret = wallet.getSecret();
    try {
      // 生成的keystore文件
      KeyStoreFile keyStoreFile = KeyStore.createLight(password, wallet);
      String keyStore = keyStoreFile.toString();
      System.out.println("keyStore:" + keyStore);
      
      // 解密keystore文件获取钱包密钥
      Wallet walletDec = KeyStore.decrypt(password, keyStoreFile);

      String addressDec = walletDec.getAddress();
      String secretDec = walletDec.getSecret();
```

### 余额查询
```java
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
```

### 本地签名

```java
    String account = "j3UcBBbes7HFgmTLmGkEQQShM2jdHbdGAe";
    String secret = "ssWiEpky7Bgj5GFrexxpKexYkeuUv";
    String to = "jNn89aY84G23onFXupUd7bkMode6aKYMt8";
    AmountInfo amountInfo = new AmountInfo();
    amountInfo.setCurrency("SWT");// 转出代币简称
    amountInfo.setValue("0.001");// 转出代币数量
    amountInfo.setIssuer("");// 转出代币银关

    Payment payment = new Payment();
    payment.as(AccountID.Account, account);
    payment.as(AccountID.Destination, to);
    payment.setAmountInfo(amountInfo);
    payment.as(Amount.Fee, String.valueOf(Config.FEE));// 交易燃料费
    payment.sequence(new UInt32(1));// 转出地址序列号
    payment.flags(new UInt32(0));
    List<String> memos = new ArrayList<String>();// 交易备注
    memos.add("SWT转账");
    memos.add("测试数据1");
    memos.add("测试数据2");
    payment.addMemo(memos);
    SignedTransaction signedTx = payment.sign(secret);// 签名
    System.out.println("tx_blob:" + signedTx.tx_blob);
```

### 转账
```java
    // 发起方地址
    String account = "jBvrdYc6G437hipoCiEpTwrWSRBS2ahXN6";
    // 发起方地址密钥
    String secret = "snBPyRRpE56ea4QGCpTMVTQWoirT2";
    // 接受方地址
    String to = "jKBCwv4EcyvYtD4PafP17PLpnnZ16szQsC";
    // 转账代币实例
    AmountInfo amountInfo = new AmountInfo();
    amountInfo.setCurrency("SWT");// 转出代币简称
    amountInfo.setValue("0.001");// 转出代币数量
    amountInfo.setIssuer("");// 转出代币银关
    
    // 转账交易实例
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
```
