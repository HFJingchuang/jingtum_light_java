package com.jch.core.types.known.tx.txns;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jch.client.Token;
import com.jch.config.Config;
import com.jch.core.coretypes.Amount;
import com.jch.core.coretypes.uint.UInt32;
import com.jch.core.types.known.tx.signed.SignedTransaction;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Transaction {
    private String balance_url = "https://swtcscan.jccdex.cn/wallet/balance/";
    private String rpc_url = "https://gateway.swtc.top/rpcservice";
    private List<String> nodes = new ArrayList<String>();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .cookieJar(new CookieJar() {
                private final Map<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

                public List<Cookie> loadForRequest(HttpUrl arg0) {

                    List<Cookie> cookies = cookieStore.get(arg0.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }

                public void saveFromResponse(HttpUrl arg0, List<Cookie> arg1) {
                    cookieStore.put(arg0.host(), arg1);

                }

            }).addInterceptor(new Interceptor() {
                private int retryNum = 0;
                private final int MAX_RETRIES = 10;

                @NotNull
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request();
                    String oldUrl = request.url().host();
                    Response response = new Response.Builder().code(400).request(request).protocol(Protocol.HTTP_1_0).message("").build();
                    try {
                        response = chain.proceed(request);
                    } catch (SocketTimeoutException e) {
                        if (retryNum >= MAX_RETRIES) {
                            throw e;
                        }
                        System.err.println(e.getMessage());
                    }
                    while (!response.isSuccessful() && retryNum < MAX_RETRIES) {
                        retryNum++;
                        if (!balance_url.contains(oldUrl) || !rpc_url.contains(oldUrl)) {
                            request = request.newBuilder().url(getNodeUrl()).build();
                        }
                        response = chain.proceed(request);
                    }
                    return response;
                }
            }).build();

    private Transaction() {
    }

    public static Transaction getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;

        private final Transaction singleton;

        private Singleton() {
            singleton = new Transaction();
        }

        public Transaction getInstance() {
            return singleton;
        }
    }

    public void setConfigUrl(List<String> nodes) {
        this.nodes = nodes;
    }

    public String getNodeUrl() {
        nodes = getJintumRPC();
        int r = new Random().nextInt(nodes.size());
        return nodes.get(r);
    }

    public void setBalance_url(String balance_url) {
        this.balance_url = balance_url;
    }

    public void setRpc_url(String rpc_url) {
        this.rpc_url = rpc_url;
    }

    /**
     * get sequence with jingtum address
     *
     * @param address {hex string}
     */
    public int getSequence(String address) {
        RequestBody body = RequestBody.create(
                "{\"method\": \"account_info\",\"params\": [{\"account\": \"" + address + "\"}]}", JSON);
        Request request = new Request.Builder().url(getNodeUrl()).post(body)
                .addHeader("Content-Type", "application/json").build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String res = responseBody.string();
                responseBody.close();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualObj = mapper.readTree(res);
                return actualObj.get("result").get("account_data").get("Sequence").asInt();
            }
            return 0;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    public void transfer(final Payment payment, final String secret, final ICallback callBack) {
        try {
            // 获取转账地址序列号
            int sequence = getSequence(payment.account().address);
            if (sequence == 0) {
                callBack.onFail(new Exception("转账地址序列号获取失败"));
            }
            payment.as(Amount.Fee, String.valueOf(Config.FEE));
            payment.sequence(new UInt32(sequence));
            payment.flags(new UInt32(0));
            SignedTransaction signedTx;
            // 本地签名
            signedTx = payment.sign(secret);
            RequestBody body = RequestBody.create(
                    "{\"method\": \"submit\",\"params\": [{\"tx_blob\": \"" + signedTx.tx_blob + "\"}]}", JSON);
            Request request = new Request.Builder().url(getNodeUrl()).post(body)
                    .addHeader("Content-Type", "application/json").build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String res = responseBody.string();
                responseBody.close();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualObj = mapper.readTree(res);
                callBack.onResponse(
                        actualObj.get("result").get("tx_json").get("hash").asText());
            } else {
                callBack.onFail(new Exception(response.message()));
            }
        } catch (Exception e) {
            callBack.onFail(e);
        }
    }

    /**
     * 获取余额
     *
     * @param address  地址
     * @param callBack 回调
     */
    public void getBalance(final String address, final ICallback callBack) {
        Request request = new Request.Builder().url(balance_url + System.currentTimeMillis() + "?w=" + address)
                .addHeader("Content-Type", "application/json").build();
        List<Token> tokens = new ArrayList<Token>();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String res = responseBody.string();
            responseBody.close();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(res);
            if (response.isSuccessful()) {
                if (actualObj.get("code").asInt() == 0) {
                    Iterator<Map.Entry<String, JsonNode>> iterator = actualObj.get("data").fields();
                    while (iterator.hasNext()) {
                        Map.Entry<String, JsonNode> next = iterator.next();
                        if (next.getValue().has("value")) {
                            String key = next.getKey();
                            String[] ci = key.split("_");
                            Token token = new Token();
                            token.setCurrency(ci[0]);
                            if (ci.length < 2) {
                                token.setIssuer("");
                            } else {
                                token.setIssuer(ci[1]);
                            }
                            token.setValue(next.getValue().get("value").asText());
                            token.setfreezed(next.getValue().get("frozen").asText());
                            tokens.add(token);
                        }
                    }
                    callBack.onResponse(tokens);
                } else {
                    callBack.onFail(new Exception(actualObj.get("msg").asText()));
                }
            } else {
                callBack.onFail(new Exception(response.message()));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            callBack.onFail(e);
        }
    }

    /**
     * 获取井通RPC节点地址
     */
    public List<String> getJintumRPC() {
        Request request = new Request.Builder().url(rpc_url + "?" + System.currentTimeMillis())
                .addHeader("Content-Type", "application/json").build();
        List<String> nodes = new ArrayList<String>();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String res = responseBody.string();
            responseBody.close();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(res);
            if (response.isSuccessful()) {
                for (JsonNode jsonNode : actualObj.get("rpcpeers")) {
                    nodes.add(jsonNode.asText());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return nodes;
    }
}
