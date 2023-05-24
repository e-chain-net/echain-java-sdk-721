package com.echain.account;

import com.echain.util.Util;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.util.Arrays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.*;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

import java.security.spec.*;

import org.bouncycastle.math.ec.ECPoint;

public class Account {
    private static final String curveName = "secp256k1"; // 曲线名称
    private static final KeyPairGenerator keyPairGenerator; // 密钥对生成器
    private static final SecureRandom secureRandom = new SecureRandom(); // 安全随机数生成器

    private String privateKey;
    private String address;

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Security.setProperty("crypto.policy", "unlimited");
            // 初始化密钥对生成器
            keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
            keyPairGenerator.initialize(new ECGenParameterSpec(curveName), secureRandom);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    //Disable new Account()
    private Account(){
    }

    public Account(String address,String privateKey){
        this.address = address;
        this.privateKey = privateKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getAddress() {
        return address;
    }

    public static Account generateAccount(){
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return generateAccountFromKeyPair(keyPair);
    }

    private static Account generateAccountFromKeyPair(KeyPair keyPair){
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();

        // Convert public key to compressed form
        ECPoint point = publicKey.getQ();
        byte[] publicKeyBytes = point.getEncoded(false);
        // Create a Keccak256 digest
        MessageDigest digest = new Keccak.Digest256();
        byte[] dig =  digest.digest(Arrays.copyOfRange(publicKeyBytes,1,publicKeyBytes.length));

        String address = "0x" + Util.bytesToHex(Arrays.copyOfRange(dig,12,dig.length));
        String privateKeyHex =  adjustTo64(privateKey.getD().toString(16));
        return new Account(address,privateKeyHex);
    }

    private static String adjustTo64(String s) {
        switch (s.length()) {
            case 62:
                return "00" + s;
            case 63:
                return "0" + s;
            case 64:
                return s;
            default:
                throw new IllegalArgumentException("not a valid key: " + s);
        }
    }

    //这个函数，没调试好，还有问题
    private static Account generateAccountFromPrivateKey(String privateKeyHex) {
        try {
            // Convert the input private key hex string to BigInteger
            BigInteger privateKeyInt = new BigInteger(privateKeyHex, 16);

            // Generate a new EC KeyPair using the given private key value and curve parameters
            ECGenParameterSpec ecGenSpec = new ECGenParameterSpec("secp256k1");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
            keyPairGenerator.initialize(ecGenSpec, new SecureRandom(privateKeyInt.toByteArray()));
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            ECPrivateKey privateKey = (ECPrivateKey)keyPair.getPrivate();
            System.out.println(privateKey.getD().toString(16));
            return generateAccountFromKeyPair(keyPair);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}