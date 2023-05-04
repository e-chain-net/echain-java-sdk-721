package com.echain.test.request;

import com.echain.account.Account;
import com.echain.util.Util;
import org.json.JSONObject;

public class TestContractDeploy {
    public static Account owner = new Account("0x2ebca12753f7e9526ef76f2698b7124e37e5ce87","fbdd35578242a81d238d39f4174c0da651dce0d6b4e45a185bad2746a0e6b086");
    static{
        HttpRequest.setServerCrtPath("D:\\yeepay\\e-chain.net.cn_server.crt");
    }

    public static void main(String args[]){
        try{
            String contractAddress = requestDeployContract("Deploy-Test-2",owner.getAddress());
            System.out.println("部署合约成功，合约地址：" + contractAddress);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String requestDeployContract(String reqNo,String owner) throws Exception{
        String payload = Util.formatDeployPayload(reqNo,owner);
        String response = HttpRequest.sendPost(Define.UrlDeploy,payload);
        JSONObject obj = new JSONObject(response);
        if(!obj.getString("code").equals("EC000000")){
            throw new Exception("请求合约部署失败："+obj.getString("message"));
        }else{
             return obj.getJSONObject("data").getString("contractAddress");
        }
    }
}
