package com.lxl.lxlApi.service.inner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxl.lxlApi.service.InterfaceInfoService;
import com.lxl.lxlcommon.model.entity.InterfaceInfo;
import com.lxl.lxlcommon.service.InnerInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
@Slf4j
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    InterfaceInfoService interfaceInfoService;

    @Override
    public InterfaceInfo getInterfaceInfoByPathAndMethod(String path, String method) {
        //分割uri
        // 如果带参数，去除第一个？和之后后的参数
        if (path.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }
        if (path.startsWith("http://")) {
            path = path.substring(7);
        }
        if (path.startsWith("https://")) {
            path = path.substring(8);
        }
        log.info("接口信息地址: "+path);
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfo::getMethod,method);
        queryWrapper.eq(InterfaceInfo::getUrl,path);
        return interfaceInfoService.getOne(queryWrapper);
    }
}
