package com.echain.test.request;

import com.echain.util.Util;
import org.json.JSONObject;

public class TestSendTx {
    static{
        HttpRequest.setServerCrtPath("D:\\yeepay\\e-chain.net.cn_server.crt");
    }

    public static void main(String args[]){
        //txHash,txSigned通过签名接口返回，参考TestSign
        String txHash = "0xa9253d8c5e9c3446b045f87332bde7988bf783a09eb6bc3928a84287af7e7b0a";
        String txSigned = "0x1a1c2606636861696e30360667726f7570304177e3564e313132333335393434343637313230333131393232363734383639313631363839323431333536383536353034303338373530333336363432313338373636373633313139333439373137393732662a3078313866383539373131383935336233333734633235313565636637393963653437353033363162627d00004440c10f1900000000000000000000000095a1a99be965777d8b0e42fe5ec1c161f6c3a5da00000000000000000000000000000000000000000000000000000000000003e90b2d000020a9253d8c5e9c3446b045f87332bde7988bf783a09eb6bc3928a84287af7e7b0a3d00004132e0d9387b8cb0bf3a11756fbbd8bc8cfe25628d86d47cdfff03412ba68dd74826da11644fffc6822a85fd4c452f4e9ec9aba2e9d02c21fad8c077c907aebeb2015001";
        try{
            sendTx(txHash,txSigned,"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendTx(String txHash,String txSigned,String callbackUrl) throws Exception{
        String payload = Util.formatSendTxPayload(txHash,txSigned,"");
//        System.out.println(payload);
        String response = HttpRequest.sendPost(Define.UrlSendTx,payload);
        System.out.println(response);
        JSONObject obj = new JSONObject(response);
        if(!obj.getString("code").equals("EC000001")){
            throw new Exception("请求发送交易失败："+obj.getString("message"));
        }
    }
}
