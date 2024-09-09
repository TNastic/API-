package com.lxl.lxlcommon.service;

import com.lxl.lxlcommon.model.entity.InterfaceInfo;

public interface InnerInterfaceInfoService {

    /**
     * 查询接口信息
     * @param path uri路径
     * @param method 方法类型
     * @return
     */
    InterfaceInfo getInterfaceInfoByPathAndMethod(String path,String method);
}
