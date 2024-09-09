package com.lxl.lxlinterface.client;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lxl.lxlinterface.model.User;
import com.lxl.lxlinterface.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用接口
 */
public class LxlApiClient {

    private String accessKey = "lxl";
    public String getNameByGet(String name){

        // 最简单的HTTP请求，可以自动通过header等信息判断编码，不区分HTTP和HTTPS
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result= HttpUtil.get("http://localhost:8123/api/name", paramMap);
        return result;
    }

    public String getNameByPost(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result= HttpUtil.post("http://localhost:8123/api/name", paramMap);
        return result;
    }


    // 发请求，将请求头中加入accesskey，timestamp，nones值校验
    private Map<String,String> getMap(String user){
        HashMap<String,String> map = new HashMap<>();
        map.put("accesskey",accessKey);
        // user
        map.put("user",user);
        //生成四位的随机数
        map.put("nonce", RandomUtil.randomNumbers(4));
        // 时间戳
        map.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        map.put("sign", SignUtils.genSign(user,"abcdefg"));
        return map;
    }


    public void getUserNameByPost(User user){
        String toJsonStr = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post("http://localhost:8123/api/name/user")
                .addHeaders(getMap(toJsonStr))
                .body(toJsonStr)
                .execute();
        System.out.println(response.getStatus());
        System.out.println(response);
    }
}
