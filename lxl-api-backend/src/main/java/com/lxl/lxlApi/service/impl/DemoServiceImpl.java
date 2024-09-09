package com.lxl.lxlApi.service.impl;

import com.lxl.lxlcommon.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return name;
    }
}
