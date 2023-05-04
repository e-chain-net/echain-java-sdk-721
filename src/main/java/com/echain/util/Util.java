package com.echain.util;

import org.bouncycastle.util.encoders.Hex;
import org.fisco.bcos.sdk.jni.common.JniException;
import org.fisco.bcos.sdk.jni.utilities.keypair.KeyPairJniObj;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Util {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xff;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0f];
        }
        return new String(hexChars);
    }

    public static long convertJniKeyPair(String privateHex) throws JniException {
        return KeyPairJniObj.createJniKeyPair(0, Hex.decode(privateHex));
    }

    public static String formatQueryPayload(String method, List<Object> list){
        JSONArray params = new JSONArray();
        for(Object obj : list){
            params.put(obj);
        }
        JSONObject jsonRpc = new JSONObject();
        jsonRpc.put("method",method);
        jsonRpc.put("params",params);
        JSONObject reqObj = new JSONObject();
        reqObj.put("jsonRpc",jsonRpc);

        return reqObj.toString();
    }

    public static String formatSendTxPayload(String txHash,String txSigned,String callbackUrl){
        JSONArray params = new JSONArray();
        params.put(txSigned);
        params.put(false);
        JSONObject jsonRpc = new JSONObject();
        jsonRpc.put("method","sendTransaction");
        jsonRpc.put("params",params);
        JSONObject reqObj = new JSONObject();
        reqObj.put("jsonRpc",jsonRpc);
        reqObj.put("reqNo",txHash);
        reqObj.put("callbackUrl",callbackUrl);

        return reqObj.toString();
    }

    public static String formatDeployPayload(String reqNo, String owner){
        JSONObject jsonRpc = new JSONObject();
        jsonRpc.put("reqNo",reqNo);
        jsonRpc.put("contractType","ERC721");
        jsonRpc.put("owner",owner);

        return jsonRpc.toString();
    }
}
