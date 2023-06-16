package com.echain.sdk;

import com.echain.account.Account;
import com.echain.util.AbiUtil;
import com.echain.util.Util;
import org.fisco.bcos.sdk.jni.common.JniException;
import org.fisco.bcos.sdk.jni.utilities.tx.TransactionBuilderJniObj;
import org.fisco.bcos.sdk.jni.utilities.tx.TxPair;

import java.math.BigInteger;

public class EChainSDK {
    public EChainSDK(){
    }

    /**
     * 生成随机账户
     * @return  Account对象
     */
    public Account generateAccount(){
        return Account.generateAccount();
    }

    /**
     * 签名铸造交易
     * @param toAddress 目标地址
     * @param tokenID   NFT-ID
     * @param contractAddress   合约地址
     * @param privateHex        账户私钥
     * @param blockNumber       当前链上最新区块号
     * @return          TxPair，包含交易哈希与签名后的交易体
     * @throws JniException
     */
    public TxPair signMint(String toAddress,
                           BigInteger tokenID,
                           String contractAddress,
                           String privateHex,
                           long blockNumber) throws JniException
    {
        String input = AbiUtil.encodeMint(toAddress,tokenID);
        return createSignedTx(privateHex,contractAddress,input,blockNumber);
    }

    /**
     *
     * @param fromAddress   源地址
     * @param toAddress     目标地址
     * @param tokenID       NFT-ID
     * @param contractAddress   合约地址
     * @param privateHex        账户私钥
     * @param blockNumber       当前链上最新区块号
     * @return              TxPair，包含交易哈希与签名后的交易体
     * @throws JniException
     */
    public TxPair signTransferFrom(String fromAddress,
                                   String toAddress,
                                   BigInteger tokenID,
                                   String contractAddress,
                                   String privateHex,
                                   long blockNumber) throws JniException
    {
        String input = AbiUtil.encodeTransferFrom(fromAddress,toAddress,tokenID);
        return createSignedTx(privateHex,contractAddress,input,blockNumber);
    }
    /**
     * 签名销毁交易
     * @param tokenID   NFT-ID
     * @param contractAddress   合约地址
     * @param privateHex        账户私钥
     * @param blockNumber       当前链上最新区块号
     * @return          TxPair，包含交易哈希与签名后的交易体
     * @throws JniException
     */
    public TxPair signBurn(BigInteger tokenID,
                           String contractAddress,
                           String privateHex,
                           long blockNumber) throws JniException
    {
        String input = AbiUtil.encodeBurn(tokenID);
        return createSignedTx(privateHex,contractAddress,input,blockNumber);
    }
    /**
     * 签名授权交易
     * @param operator   被授权方地址（一般指平台方账户）
     * @param approved   true：授权，false：取消授权
     * @param contractAddress   合约地址
     * @param privateHex        账户私钥
     * @param blockNumber       当前链上最新区块号
     * @return          TxPair，包含交易哈希与签名后的交易体
     * @throws JniException
     */
    public TxPair signSetApproveForAll(String operator,
                              boolean approved,
                              String contractAddress,
                              String privateHex,
                              long blockNumber) throws JniException
    {
        String input = AbiUtil.encodeSetApproveForAll(operator,approved);
        return createSignedTx(privateHex,contractAddress,input,blockNumber);
    }

    private TxPair createSignedTx(String privateHex, String contractAddress, String input, long blockNumber) throws JniException {
        long jniKeyPair = Util.convertJniKeyPair(privateHex);
        long blockLimit = blockNumber + 900;
        return TransactionBuilderJniObj.createSignedTransaction(jniKeyPair, "group0", "chain0", contractAddress, input, "", blockLimit, 1, "");
    }
}
