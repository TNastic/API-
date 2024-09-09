package com.lxl.lxlcommon.service;

public interface InnerUserInterfaceInfoService {

    /**
     *  用户调用接口
     * @param userId
     * @param interfaceId
     * @return
     */
    boolean invokeInterfaceCount(long userId,long interfaceId);
}
