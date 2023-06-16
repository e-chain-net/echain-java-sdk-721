package com.echain.test.request;

import com.echain.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestQuery {

    static{
        HttpRequest.setServerCrtPath("D:\\yeepay\\e-chain.net.cn_server.crt");
    }

    public static void main(String args[]) throws InterruptedException {
        //请求区块号
        TestGetBlockNumber();

        //请求交易收据，判断交易结果
        TestGetTransactionReceipt();

        //查询nft余额
        TestBalanceOf();

        //查询token拥有者
        TestOwnerOf();

        //查询授权信息
        TestIsApproveForAll();
    }



    public static void TestGetBlockNumber(){
        String payload = Util.formatQueryPayload("getBlockNumber",new ArrayList<>());
        String response = HttpRequest.sendPost(Define.UrlQuery,payload);
        System.out.println("请求区块号：");
        try{
            int blockNumber = getBlockNumber();
            System.out.println("BlockNumber=" + blockNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static int getBlockNumber() throws Exception {
        String payload = Util.formatQueryPayload("getBlockNumber",new ArrayList<>());
        String response = HttpRequest.sendPost(Define.UrlQuery,payload);
//        System.out.println(response);
        JSONObject obj = new JSONObject(response);
        if(!obj.getString("code").equals("EC000000")){
            throw new Exception("请求区块号失败："+obj.getString("message"));
        }else{
            return obj.getJSONObject("data").getInt("blockNumber");
        }
    }

    public static void TestGetTransactionReceipt(){
        System.out.println("");
        String txHash = "0x71b86e2ba763eb85d5159700090771c99beda29954e04849767d478e8f003a76";

        String payload = Util.formatQueryPayload("getTransactionReceipt",new ArrayList<>(Arrays.asList(txHash,false)));
//        System.out.println(payload.toString());

        String response = HttpRequest.sendPost(Define.UrlQuery,payload);
        System.out.println("请求交易收据：");
        System.out.println(response);
        JSONObject obj = new JSONObject(response);
        if(!obj.getString("code").equals("EC000000")){
            System.out.println("请求交易收据失败："+obj.getString("message"));
        }else{
            boolean ok = obj.getJSONObject("data").getJSONObject("result").getBoolean("statusOK");
            System.out.println("交易结果=" + (ok?"成功":"失败"));
        }
    }


    public static void TestBalanceOf(){
        System.out.println("");
        String account = "0x95a1a99be965777d8b0e42fe5ec1c161f6c3a5da";
        BigInteger tokenId = BigInteger.valueOf(1001);

        System.out.println("请求NFT余额：");
        try{
            int balance = getBalanceOf(account,tokenId,Define.ContractAddress);
            System.out.println("NFT余额="+balance);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String encodeBalanceOf(String address,BigInteger tokenId){
        Function function = new Function("balanceOf",
                Arrays.<Type>asList(new Address(address)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return FunctionEncoder.encode(function);
    }

    private static int getBalanceOf(String account,BigInteger tokenId,String contractAddress) throws Exception {
        String input = encodeBalanceOf(account,tokenId);
        String payload = Util.formatQueryPayload("call",new ArrayList<>(Arrays.asList(contractAddress,input)));
//        System.out.println(payload.toString());
        String response = HttpRequest.sendPost(Define.UrlQuery,payload);
//        System.out.println(response);
        JSONObject obj = new JSONObject(response);
        if(!obj.getString("code").equals("EC000000")){
            throw new Exception("请求balanaceOf："+obj.getString("message"));
        }else{
            int status = obj.getJSONObject("data").getJSONObject("result").getInt("status");
            if(status != 0){
                throw new Exception("请求balanaceOf 失败，status="+status);
            }else{
                String output = obj.getJSONObject("data").getJSONObject("result").getString("output");
                BigInteger balance = new BigInteger(output.substring(2),16);
                return balance.intValue();
            }
        }
    }

    private static void TestOwnerOf(){
        System.out.println("");
        BigInteger tokenId = BigInteger.valueOf(1002);

        System.out.println("请求NFT拥有者：");
        try{
            String owner = getOwnerOf(tokenId,Define.ContractAddress);
            System.out.println("Owner of token " + tokenId.intValue() + "=" + owner);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String encodeOwnerOf(BigInteger tokenId){
        Uint256 tokenId256 = new Uint256(tokenId);
        Function function = new Function(
                "ownerOf",                      // Function name
                Arrays.asList(tokenId256),         // Input parameters
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));  // Output parameter(s)

        return FunctionEncoder.encode(function);
    }

    private static String getOwnerOf(BigInteger tokenId,String contractAddress) throws Exception {
        String input = encodeOwnerOf(tokenId);
        String payload = Util.formatQueryPayload("call",new ArrayList<>(Arrays.asList(contractAddress,input)));
//        System.out.println(payload.toString());
        String response = HttpRequest.sendPost(Define.UrlQuery,payload);
//        System.out.println(response);
        JSONObject obj = new JSONObject(response);
        if(!obj.getString("code").equals("EC000000")){
            throw new Exception("请求ownerOf："+obj.getString("message"));
        }else{
            int status = obj.getJSONObject("data").getJSONObject("result").getInt("status");
            if(status != 0){
                throw new Exception("请求ownerOf 失败，status="+status);
            }else{
                String output = obj.getJSONObject("data").getJSONObject("result").getString("output");
                String owner = "0x" + output.substring(26);
                return owner;
            }
        }
    }

    private static void TestIsApproveForAll(){
        System.out.println("");
        String contractOwner = "0x2ebca12753f7e9526ef76f2698b7124e37e5ce87";
        String user1 = "0x95a1a99be965777d8b0e42fe5ec1c161f6c3a5da";
        String contractAddress = "0x18f8597118953b3374c2515ecf799ce4750361bb";
        System.out.println("请求授权查询：");
        try{
            boolean ret = getIsApproveForAll(user1,contractOwner,contractAddress);
            System.out.println("IsApproveForAll return:" + ret);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String encodeIsApproveForAll(String owner,String operator){
        Function function = new Function(
                "isApprovedForAll",                      // Function name
                Arrays.asList(new Address(owner),new Address(operator)),         // Input parameters
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));  // Output parameter(s)

        return FunctionEncoder.encode(function);
    }

    private static boolean getIsApproveForAll(String owner,String operator,String contractAddress) throws Exception {
        String input = encodeIsApproveForAll(owner,operator);
        String payload = Util.formatQueryPayload("call",new ArrayList<>(Arrays.asList(contractAddress,input)));
//        System.out.println(payload.toString());
        String response = HttpRequest.sendPost(Define.UrlQuery,payload);
        System.out.println(response);
        JSONObject obj = new JSONObject(response);
        if(!obj.getString("code").equals("EC000000")){
            throw new Exception("请求isApprovedForAll："+obj.getString("message"));
        }else{
            int status = obj.getJSONObject("data").getJSONObject("result").getInt("status");
            if(status != 0){
                throw new Exception("请求ownerOf 失败，status="+status);
            }else{
                String output = obj.getJSONObject("data").getJSONObject("result").getString("output");
                BigInteger value = new BigInteger(output.substring(2),16);
                return value.intValue() == 1;
            }
        }
    }
}
