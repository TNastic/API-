package com.lxl.lxlinterface.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignUtils {

    public static String genSign(String body,String secretKey){
        Digester md5 = new Digester(DigestAlgorithm.SHA256);

        return md5.digestHex(body+secretKey);
    }
}
