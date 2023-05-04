package com.echain.util;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

   public static final String SIGN_ALGORITHMS = "SHA256withRSA";

   /**
    * @param content:签名的参数内容
    * @param privateKey：私钥
    * @return
    */
   public static String sign(String content, String privateKey) {
      String charset = "utf-8";
      try {
         PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
         KeyFactory keyf = KeyFactory.getInstance("RSA");
         PrivateKey priKey = keyf.generatePrivate(priPKCS8);
         java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
         signature.initSign(priKey);
         signature.update(content.getBytes(charset));
         byte[] signed = signature.sign();
         return Base64.encode(signed);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   /**
    * @param content：验证参数的内容
    * @param sign：签名
    * @param publicKey：公钥
    * @return
    */
   public static boolean doCheck(String content, String sign, String publicKey) {
      try {
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         byte[] encodedKey = Base64.decode(publicKey);
         PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
         java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
         signature.initVerify(pubKey);
         signature.update(content.getBytes("utf-8"));
         boolean bverify = signature.verify(Base64.decode(sign));
         return bverify;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static void main(String[] args) {
      String content = "1020000189025321" + "-" + "1681178471210";
      String signstr = RSAUtils.sign(content, "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCY/i3wvgeqo1gSusHf1AuPYU+nRMNLkZeIPpA6YjvE5iQ26DQXdrQ0WamsvAKz3GEQMwRqmtesEQhLygJj4wJdO4PDhGNqlObsWQx4N1cyrbvGouTbO2hUhnesP3PURMRBEpJd1A/koKmh606i4oKcXKuVBwjoBMdPPCMZ4QchcJ30KFLtiXT9OBpGim7SgFBiKnWxO/4CGd1KkixRj4ID0lyYhRnnCUvFptN822V4g5yr7vSUsH3M7IxVO4SgFzFZjM9pvuo0G2vf51NJ+fK3y9rK24/Vt671sEf59s28OXuyRXErjgfIoDdM2triJ2Fq8jZQV9kVvR6gn7rmZfUnAgMBAAECggEAQ248J1BKJr5Jsi+YBaP62F4Gcm3POb5YsFcK0IC9YSIiMgUT+Id8E1q1ewl+k3F9YltqBeZrSk5TfrvxY78JKrhxcbom6zHnuaHh6hZSG2cRTRI8lhfP+vktQ8DPt237pcaetjYiLx1UxqXkicwVzv7VLSDlnwWEJvsVaXGR5/2BT8q+/2VEK4qCe8DESNpWNlDfonXAK0FDtDzWkjwLeWzJtzWQLw0ps8gSTQsUYRA2GUBtcp3MWOy+GOAIzhbTawOYTi3EjvAsRB7YuLYLOnueid0vYVRu6IHETcOBJIpGbBxV0IpbNvYNJ53A1bgyELvKIM9xUYs/3m5HIc6mmQKBgQDKvaYx1nUTjikkkn88IC+TgnMGSBSDSKcxZd0IOUPC1ohtnB0x/IcH+mEBot8GEkn7CjnyvtbaBq3I+RxGpfrLzdzt8BH9tLxyrGA872iXfB8owRMpoOs0hDMRTT2gZPsXpNdQwDP4UvLqmsQPw0QO5id7gLnc5Rm6OSMcIaXx/QKBgQDBLvl3we7ZzN+PydXBY9AKnvAl9BeDFPgynsRXn0dYNuKDWR3PXF/IOLGraa7LHZ3L6WJY3fLRr5CMV+k8RjWo6aZMHRqFzsQGRW3ta7XczTO1yq6/ks6xHje/yQqeGdbJLD07StCwslA7JDukA5u0WuPkaozjRKLrN9ShiHDq8wKBgQCOxpoo1NekSuQsjkKuTBhVMHPiw5Y2kk60GgFbzkArETwIvP1Oe4F4m9n+9f1L4EtbUGtYyQ6zgiqWsuA33KHPLw3cPsncupBPzZcEsrEcpVuoLrhZA6tAU61HDPdOYm71yq+bfY/b3EaX8yAJ3cCrIWhCsHez2V+R5rUUFZow3QKBgQC81Sr7OfE8qrt49OTh5awNRbEemEuHUS8PZAwuTj5R50xg8fJmqDfkIi7hjCtU1f1Rvi7pCQL6nm9gD+qnhUWcd8+bJPOxChyouKMsaZXaYCcEszs/fcRWc2AxMtYTFtTRzlGILKhzn8k3FkLKHtDLafDLbK+M06Gg5PEOeK1PqwKBgQCn0r+NDE09ImX9PVymwomScpPRWC/SxVgmzx3mGDG0AaRqGjfa1hskqxwx8eRfT3exwvQ9dvYaPyfATyQZ0uEkg7bJ47Jfr7YHkcKMWM1+u8iVxN4mn6Kj3aSfy71iumPzm8J/9BZHX1cTLzXDi1OiP+mQs+UXqwXGfvx/UZHSgw==");
      System.out.println("签名原串：" + content);
      System.out.println("签名串：" + signstr);
      System.out.println();
      System.out.println("---------------公钥校验签名------------------");
      System.out.println("签名原串：" + content);
      System.out.println("签名串：" + signstr);
//      signstr = "RgdvfNBvfkccRgnOC2y5Zcfyh5myAYZnTGS6mhBCv0t0rIQfhLBqLVge7S931/1QavD9X0G4E0wTB1Dl2iMcWGZABC+HrtgZtndigUwNaIJhxkX+drkxPeFZROjwk02xvfx3deMvAHqMYx3EZVmTB6x3Og/7Z7HEKt3mGWNuDh/usS+SV8CG+iRrFDSlnCTJc4N6/6B+bFT2O2U5a0wCwgaIg5q3mrNgSMQ7W3WxGue8/DxonYNUUupqQltwUH56SX6WKj0sy87ahAJe1fdIuNH/DN9R/guOqqLYARMYyC5bStqj+lfKm9EkIUahOoy3QWG5jVNG7g6wqxbSYXjHSg==";
      System.out.println("验签结果：" + RSAUtils.doCheck(content, signstr, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmP4t8L4HqqNYErrB39QLj2FPp0TDS5GXiD6QOmI7xOYkNug0F3a0NFmprLwCs9xhEDMEaprXrBEIS8oCY+MCXTuDw4RjapTm7FkMeDdXMq27xqLk2ztoVIZ3rD9z1ETEQRKSXdQP5KCpoetOouKCnFyrlQcI6ATHTzwjGeEHIXCd9ChS7Yl0/TgaRopu0oBQYip1sTv+AhndSpIsUY+CA9JcmIUZ5wlLxabTfNtleIOcq+70lLB9zOyMVTuEoBcxWYzPab7qNBtr3+dTSfnyt8vaytuP1beu9bBH+fbNvDl7skVxK44HyKA3TNra4idhavI2UFfZFb0eoJ+65mX1JwIDAQAB"));
      System.out.println();
   }
}