package com.lxl.lxlApi.rpc.impl;

import com.lxl.lxlApi.rpc.RpcDemoService;



public class RpcServiceImpl implements RpcDemoService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
