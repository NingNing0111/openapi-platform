package com.pgthinker.backend.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * @Project: com.pgthinker.backend.utils
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/24 22:17
 * @Description: Key生成
 */
public class GenKeyUtils {

    // 根据input信息 输出 长度为len的字符串
    public static String genKey(int len){
        if( len <= 0){
            throw new RuntimeException("生成的key的长度不合法");
        }
        // 随机生成
        return RandomStringUtils.randomAlphabetic(len);
    }

    // 生成签名字符串
    public static String genSign(Map<String,String> data,String secretKey){
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = data.toString() + "." + secretKey;
        return digester.digestHex(content);
    }

}
