package com.por.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * hash工具类
 * @author Falan
 * @date 2025-10-08 22:13:41
 */
public class HashUtils {

    /**
     * 计算hash
     * @param origin    原始数据
     * @return
     */
    public static String calcHash(String origin){

        try{

            // 创建MessageDigest实例
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 计算哈希值
            byte[] hashBytes = digest.digest(origin.getBytes(StandardCharsets.UTF_8));

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        }catch (Exception e){
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(calcHash("BTC,bc1p3rynzzrpldcwmpqv5k7n98zxazrqm86arzsdzmmgkv4xvnjru3rqc2rs2g,0\n"));
    }
}
