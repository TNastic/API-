package com.lxl.lxlApi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lxl.lxlcommon.model.entity.InterfaceInfo;

/**
* @author 86135
* @description 针对表【interface_info(接口表)】的数据库操作Service
* @createDate 2023-11-15 22:13:56
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
