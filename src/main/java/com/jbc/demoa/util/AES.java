package com.jbc.demoa.util;



import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by 此生无分起相思 on 2018/5/23.
 * 使用AES加密算法对String类型进行加密和解密，提供setKey(String key)、String encrypt(String content)
 * 和decrypt(String content)三个方法，第一个方法用于设置加解密秘钥（默认秘钥 cswfqxs），第二个方法返回
 * 加密后的字符串，第三个方法对加密后的字符串进行解密并返回解密后的明文字符串。
 */

public class AES
{
    public static void main(String[] args) {
        AES.encrypt(1);
    }
    private static String keygen = "cswfqxs"; //加密秘钥 默认秘钥为cswfqxs




//        //public static void setKey(String key)
//        {
//            keygen = key;
//        }

    public static String encrypt(double num) //加密字符串 其中content为需要加密的内容
    {
        String content=""+num;
        // return content;
        // return content;

        //  System.out.println(System.getProperty("java.version"));
        try
        {
            //   KeyGenerator keyGen=KeyGenerator.getInstance("com.AES");
            KeyGenerator keyGen = KeyGenerator.getInstance("com.AES");
            keyGen.init(128, new SecureRandom(keygen.getBytes()));
            SecretKey secretKey = keyGen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "com.AES"); // 创建密码器
            Cipher cipher = Cipher.getInstance("com.AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key); // 初始化
            byte[] result = cipher.doFinal(byteContent);
            String code = parseByteToHexStr(result);
            return code;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String content)  // 解密字符串 其中content为需要解密的内容
    {
        //return content;
        byte[] code = parseHexStrToByte(content);
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("com.AES");
            keyGen.init(128, new SecureRandom(keygen.getBytes()));
            SecretKey secretKey = keyGen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "com.AES");
            Cipher cipher = Cipher.getInstance("com.AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(code);
            try {
                return new String(result, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();}
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

    private static String parseByteToHexStr(byte buf[]) //流转字符串 辅助函数
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++)
        {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStrToByte(String hexStr)  //字符串转流 辅助函数
    {
        if (hexStr.length() < 1)
        {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++)
        {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
