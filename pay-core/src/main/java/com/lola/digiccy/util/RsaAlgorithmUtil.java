package com.lola.digiccy.util;

import com.lola.digiccy.response.KeyPairDto;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * <p>
 * RSA签名校验 防止篡改
 * 通过私钥进行加签 通过公钥进行解密
 * </p>
 *
 * @author Micheal
 * @since 2021/3/17
 */
public class RsaAlgorithmUtil {

    private static final Logger logger = LoggerFactory.getLogger(RsaAlgorithmUtil.class);


    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 公钥信息
     */
   // public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKzIQirsb4/wk7NGyxWx5ng0S3f9Uvwm8zHw3jCgTGx6h1fv+TIWKrTIsNHjGas/sBu1u/psCWQpeQfC4TuSFb82gSwLi72G9Joida6hrokFoEfxk9DH7qTJd3WzCSOyGcRELmkEIh3y4tMc+esMy+35NX46Ipt6yzWkmodFa/BwIDAQAB";
    /**
     * 私钥信息
     */
    public static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIrMhCKuxvj/CTs0bLFbHmeDRLd/1S/CbzMfDeMKBMbHqHV+/5MhYqtMiw0eMZqz+wG7W7+mwJZCl5B8LhO5IVvzaBLAuLvYb0miJ1rqGuiQWgR/GT0MfupMl3dbMJI7IZxEQuaQQiHfLi0xz56wzL7fk1fjoim3rLNaSah0Vr8HAgMBAAECgYAccWiqitJveCP2I/oaJigG4lmUmPeg/+E1wqlf20+7RHL7EPlitza3D8p297cGktdfNXvJvGw+3oqcqzXuCMQS/BS7dt+n/YvUBkUxXzBN6SjsJkrJuBKTjJW0YyOi7qIGTaO4CNUl1jbfel7tXmYP5/OHdGK3yhhL9Hide7dMwQJBANEHnPeRVf4tY46Aien+4/f6bnBIrw9yDu5ncgzPIs2iKFKgAFoWGJYj/1JFahxdLLl7i7DfrQH05NDItEqipLECQQCp/OPRZ8WsIZ3BIQJmvo5Yk6NXAL4fR5MQPTFmK1YlilaiHC05seniNPWxhFbdpNykXn15g4qvilMKRj6JJm03AkAZT9KyJ+dB4D0uBGKu8y6n2KuG7UZFmIo2VFB7PJn6TKiVWMqyHfrcM3+3drBivuWrkEpaILvSgNU0cxlh8FABAkEAgKSm0GoFObAzD95oT9M0LKqGeahROaDUFf0JbnWrezuHNqW2QCx5gLxtl8s8Zf6HEEmGEbjP8uruzx674tVW3wJAVON5sx9KGjU2JgeZM7dkkbwobv8aDG1BnUUqgDjBaOnvuW1EVPXf28Af/3JDAOHdC4tuU2qUr0Yl8NEilhuJxA==";

    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfC/oaQZZksZ9xPqk8ggOqRErkwaIl9EYUtxIAxn1EYFjgwP1gpmwTdFx6d9BvjG0youb9+vg+YpiSX9a318lECVuIgrbn8rawKoKY1SpxS0DCO4MM1VtdLG0BVG5JOkuecZ3dtJFcyTCbnW8ujwEqViOq7qMidCo7dw0ZarbGQQIDAQAB";




    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return PrivateKey
     */

    public static PrivateKey getPrivateKey(String privateKey) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        return keyFactory.generatePrivate(keySpec);

    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return PublicKey
     */

    public static PublicKey getPublicKey(String publicKey) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

        return keyFactory.generatePublic(keySpec);

    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */

    public static KeyPairDto genKeyPair(){
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            logger.info("获取密钥对失败，失败信息【{}】",e.getMessage());
            e.printStackTrace();
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        //得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        KeyPairDto keyPairDto = new KeyPairDto();
        keyPairDto.setPublicKey(publicKeyString);
        keyPairDto.setPrivateKey(privateKeyString);
        return keyPairDto;

    }

    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception  加密过程中的异常信息
     */

    public static String encrypt(String str, String publicKey) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] data = str.getBytes("UTF-8");
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        String outStr = Base64.encodeBase64String(encryptedData);
        return outStr;

    }

    /**
     * RSA私钥解密
     *
     * @param str 加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */

    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] data = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        //对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        String outStr = new String(decryptedData);
        return outStr;

    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @param apiPrivateKey 私钥
     * @return 签名
     */

    public static String sign(String data, String apiPrivateKey) throws Exception {
        PrivateKey privateKey = getPrivateKey(apiPrivateKey);
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));

    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param apiKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */

    public static boolean checkSign(String srcData, String apiKey, String sign) throws Exception {
        PublicKey publicKey = getPublicKey(apiKey);
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));

    }

//    public static void main(String[] args) throws Exception {
//        PrivateKey privateKey1 = getPrivateKey(privateKey);
//        String sign = sign("测试", privateKey1);
//        PublicKey publicKey1 = getPublicKey(publicKey);
//        boolean checkSign = checkSign("测试", publicKey1, sign);
//        System.out.println(checkSign);
////        KeyPairDto keyPairDto = genKeyPair();
////        System.out.println(keyPairDto.getPublicKey());
//    }

}
