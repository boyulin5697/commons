package com.by.commons.tools;

import org.apache.commons.codec.binary.Hex;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 Utils
 *
 * @author by.
 * @date 2022/7/8
 */
public class MD5Utils {

    /**
     * Generate normal md5
     * @param key key
     * @return MD5 md5
     * @throws NoSuchAlgorithmException if md5 is not exist in this jdk
     */
    public static String MD5(String key) throws NoSuchAlgorithmException {
        MessageDigest md5 = null;
        md5 = MessageDigest.getInstance("MD5");
        char[] charArray = key.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * Generate md5 value with salt
     * @param key The encode key
     * @param salt The salt value
     * @return
     */
    public static String generateMd5WithSalt(String key,String salt) {
        assert salt.length()==16;
        key = md5Hex(key + salt);
        //mix salt into md5
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            assert key != null;
            cs[i] = key.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = key.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * verify key, salt and encode md5
     * @param key key
     * @param salt salt
     * @param md5 md5 value
     */
    public static boolean verify(String key, String salt, String md5) {
        assert key!=null && key.length()==16;
        assert salt!=null && salt.length()==16;
        assert md5!=null && md5.length()==32;
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String decodeSalt = new String(cs2);
        if(!decodeSalt.equals(salt)){
            return false;
        }
        return md5Hex(key + salt).equals(new String(cs1));
    }

    /**
     * Generate md5
     */
    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }
}
