package com.elink.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    /**
     * AES加密字符串(JDK1.8.0_192)
     *
     * @param content 需要被加密的字符串
     * @param secret  密钥
     * @return str 加密后的字符串
     */
    public static String encryptDataToStr(String content, String secret) {
        return base64Encrypt(encrypt(content, secret));
    }

    /**
     * AES解密字符串(JDK1.8.0_192)
     *
     * @param dataStr 需要被解密的字符串
     * @param secret  密钥
     * @return Str 解密后的字符串
     */
    public static String decryptDataToStr(String dataStr, String secret) {
        // TODO Auto-generated catch block
        String Str = null;
        try {
            Str = new String(AESUtil.decrypt(AESUtil.base64Decrypt(dataStr), secret), "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Str;
    }

    /**
     * AES加密字符串(JDK1.8.0_192)
     *
     * @param content 需要被加密的字符串
     * @param key     加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(String content, String key) {
        try {

            return encrypt(content.getBytes(Charset.forName("UTF-8")), key);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密byte[](JDK1.8.0_192)
     *
     * @param bytecontent 需要被加密的byte[]
     * @param key         加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(byte[] bytecontent, String key) {
        try {
            SecretKeySpec keyval = new SecretKeySpec(key.getBytes(), "AES");// 转换为AES专用密钥

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器

            cipher.init(Cipher.ENCRYPT_MODE, keyval);// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(bytecontent);// 加密

            return result;

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串(JDK1.8.0_192)
     *
     * @param content AES加密过的内容
     * @param key     加密时的密码
     * @return 明文
     */
    public static byte[] decrypt(byte[] content, String key) {
        try {
            SecretKeySpec keyval = new SecretKeySpec(key.getBytes(), "AES");// 转换为AES专用密钥

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, keyval);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            return result; // 明文

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String base64Encrypt(byte[] b) {
        String a;
        try {
            a = new String(Base64.encode(b, Base64.DEFAULT), "utf-8");
            return a;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] base64Decrypt(String b) {
        byte[] bytes;
        try {

            bytes = Base64.decode(b.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT);
            return bytes;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


}
