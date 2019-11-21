package com.elink.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Evloution_
 * @date 2018/9/5
 * @explain AES 的加密和解密
 */
public class AESEncryptUtil {

    //编码方式
    public static final String CODE_TYPE = "UTF-8";
    //填充类型
    public static final String AES_TYPE = "AES/ECB/PKCS5Padding";
    public static final String AES = "AES";

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 加密需要的密码
     * @return 密文
     */
    /*public static byte[] encrypt(String content, String password) {
        try {
            // 转换为 AES 专用密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(), AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES_TYPE);

            byte[] byteContent = content.getBytes(CODE_TYPE);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // 加密
            byte[] result = cipher.doFinal(byteContent);

            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    *//**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 加密时的密码
     * @return 明文
     *//*
    public static byte[] decrypt(byte[] content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            // 转换为 AES 专用密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(), AES);
            // 创建密码器
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // 加密
            byte[] result = cipher.doFinal(content);
            // 返回明文
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*
     * 加密
     */
    public static String encrypt(String cleartext, String password) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypts(password, cleartext);
            return new String(Base64.encode(result, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encrypts(String password, String clear) throws Exception {
        // 创建AES秘钥
        SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        // 加密
        return cipher.doFinal(clear.getBytes("UTF-8"));
    }

    private byte[] decrypt(byte[] content, String password) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化解密器
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 解密
        return cipher.doFinal(content);
    }

    public static String md5(String str) {
        String md5str = "";
        try {
            // 初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 转换成byte数组
            byte[] input = str.getBytes();
            // 计算后获得125字节数组
            byte[] buff = md.digest(input);
            // 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }
}
