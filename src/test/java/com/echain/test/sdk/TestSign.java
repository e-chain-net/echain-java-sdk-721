package com.echain.test.sdk;

import com.echain.account.Account;
import com.echain.sdk.EChainSDK;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.fisco.bcos.sdk.jni.common.JniException;
import org.fisco.bcos.sdk.jni.utilities.tx.TxPair;

import java.math.BigInteger;

public class TestSign {
    public static String contractAddress = "0x18f8597118953b3374c2515ecf799ce4750361bb";
    public static Account owner = new Account("0x2ebca12753f7e9526ef76f2698b7124e37e5ce87","fbdd35578242a81d238d39f4174c0da651dce0d6b4e45a185bad2746a0e6b086");
    public static Account user1 = new Account("0x95a1a99be965777d8b0e42fe5ec1c161f6c3a5da","20af5ca9552563576673abda1af0540ff6c72ea631b1de8b11296c94167a6b06");
    public static Account user2 = new Account("0xf53baf7526a2c8aec2f185ed48e94316e29e9e95","42e548a753fe86d0937372b24ae472559966929fb3f8d0672376849d23f6a43d");
    public static EChainSDK sdk = new EChainSDK();
    public static void main(String args[]) throws JniException {
        BigInteger tokenId = BigInteger.valueOf(1003);

        long blockNumber = 691660;

        TxPair mintRes = sdk.signMint(user1.getAddress(),tokenId,contractAddress,owner.getPrivateKey(),blockNumber);
        System.out.println("Mint txHash:" + mintRes.getTxHash());
        System.out.println("Mint signed:" + mintRes.getSignedTx());

        TxPair transferRes = sdk.signTransferFrom(user1.getAddress(),user2.getAddress(),tokenId,contractAddress,user1.getPrivateKey(),blockNumber);
        System.out.println("Transfer txHash:" + transferRes.getTxHash());
        System.out.println("Transfer signed:" + transferRes.getSignedTx());

        TxPair burnRes = sdk.signBurn(tokenId,contractAddress,user2.getPrivateKey(),blockNumber);
        System.out.println("Burn txHash:" + burnRes.getTxHash());
        System.out.println("Burn signed:" + burnRes.getSignedTx());

        //授权相关，user1授权给owner
        TxPair approveRes = sdk.signSetApproveForAll(owner.getAddress(),true,contractAddress,user1.getPrivateKey(),blockNumber);
        System.out.println("Approve txHash:" + approveRes.getTxHash());
        System.out.println("Approve signed:" + approveRes.getSignedTx());

        tokenId = BigInteger.valueOf(1004);
        //铸造给user1
        mintRes = sdk.signMint(user1.getAddress(),tokenId,contractAddress,owner.getPrivateKey(),blockNumber);
        System.out.println("Mint2 txHash:" + mintRes.getTxHash());
        System.out.println("Mint2 signed:" + mintRes.getSignedTx());
        //由owner发起将user1的token转移给user2
        transferRes = sdk.signTransferFrom(user1.getAddress(),user2.getAddress(),tokenId,contractAddress,owner.getPrivateKey(),blockNumber);
        System.out.println("Transfer2 txHash:" + transferRes.getTxHash());
        System.out.println("Transfer2 signed:" + transferRes.getSignedTx());

        String didContractAddress = "0x11c7afd80560f72891df3ab8969e5b524d738f04";
        TxPair didPair = sdk.signRegisterDID(user1.getAddress(),"https://www.bing.com/?mkt=zh-CN",didContractAddress,owner.getPrivateKey(),blockNumber);
        System.out.println("RegisterDID txHash:" + didPair.getTxHash());
        System.out.println("RegisterDID signed:" + didPair.getSignedTx());
    }
}
