package com.have.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @ClassName PasswordSaltCipher
 * @Description TODO
 * @Author G
 * @Date 2019/6/11 10:10
 * @Version 1.0
 **/
public class PasswordSaltCipher {

    // 生成随机盐
    public static String generateSalt(int len) {
        int byteLen = len >> 1;
        SecureRandomNumberGenerator secureRandomNum = new SecureRandomNumberGenerator();
        return secureRandomNum.nextBytes(byteLen).toHex();
    }
    // hashAlgorithm: hash算法名称 MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512
    public static String encryptPassword(String hashAlgorithm, String password, String salt, int hashIterations) {
        SimpleHash hash = new SimpleHash(hashAlgorithm, password, salt, hashIterations);
        return hash.toString();
    }
}
