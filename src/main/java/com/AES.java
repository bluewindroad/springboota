package com;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

    /**
     * Created by 此生无分起相思 on 2018/5/23.
     * 使用AES加密算法对String类型进行加密和解密，提供setKey(String key)、String encrypt(String content)
     * 和decrypt(String content)三个方法，第一个方法用于设置加解密秘钥（默认秘钥 cswfqxs），第二个方法返回
     * 加密后的字符串，第三个方法对加密后的字符串进行解密并返回解密后的明文字符串。
     */

    public class AES
    {
        private static String key = "cswfqxsarjgczzqa"; //加密秘钥 默认秘钥为cswfqxs
        private static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec("0000000000000000".getBytes());

        /**
         *  加密成十六进制字符串
         *
         *  <p>
         *     使用AES加密，并将Cipher加密后的byte数组转换成16进制字符串
         *  </p>
         *
         * @author Cr
         * @date 2020-03-22
         * */
        public static String encrypt(double data2){
            String data=""+data2;
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), IV_PARAMETER_SPEC);
                return bytesConvertHexString(cipher.doFinal(Arrays.copyOf(data.getBytes(), 16 * ((data.getBytes().length / 16) + 1))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 将加密后的十六进制字符串进行解密
         *
         * @author Cr
         * @date 2020-03-22
         *
         * **/
        public static String decrypt(String data){
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), IV_PARAMETER_SPEC);
                return new String(cipher.doFinal(hexStringConvertBytes(data.toLowerCase())),"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         *  byte数组转换成十六进制字符串
         *
         *  <p>
         *      先对每个byte数值补码成十进制,
         *      然后在将十进制转换成对应的十六进制.
         *      如果单次转换, 十六进制只有一位时， 将在前面追加0变成两位.
         *  </p>
         *
         * @author Cr
         * @date 2020-03-22
         * */
        private static String bytesConvertHexString(byte [] data){
            StringBuffer result = new StringBuffer();
            String hexString = "";
            for (byte b : data) {
                // 补码成正十进制后转换成16进制
                hexString = Integer.toHexString(b & 255);
                result.append(hexString.length() == 1 ? "0" + hexString : hexString);
            }
            return result.toString().toUpperCase();
        }

        /**
         * 十六进制字符串转换成byte数组
         *
         *  <p>
         *      在加密时, 十六进制数值和byte字节的对应关系 是:  2个十六进制数值对应  1个byte字节  (2: 1)
         *      所以byte数组的长度应该是十六进制字符串的一半, 并且在转换时
         *      应是两个十六进制数值转换成一个byte字节  (2个2个十六进制数值进行转换)
         *     这也是为什么可以*2的原因， 例如: 0, 2, 4, 6, 8, 10, 12 依次遍历
         *  </p>
         *
         * @author Cr
         * @date 2020-04-22
         * */
        private static byte [] hexStringConvertBytes(String data){
            int length = data.length() / 2;
            byte [] result = new byte[length];
            for (int i = 0; i < length; i++) {
                int first = Integer.parseInt(data.substring(i * 2, i * 2 + 1), 16);
                int second = Integer.parseInt(data.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (first * 16 + second);
            }
            return result;
        }



//        //public static void setKey(String key)
//        {
//            keygen = key;
//        }

    /*    public static String encrypt(double num) //加密字符串 其中content为需要加密的内容
        {
            String content=""+num;
           // return content;
           // return content;

          //  System.out.println(System.getProperty("java.version"));
            try
            {
             //   KeyGenerator keyGen=KeyGenerator.getInstance("com.AES");
                KeyGenerator keyGen = new  KeyGenerator("com.AES");
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
        }*/

    }
