package com.lxl.lxlApi.service;

import com.lxl.lxlcommon.model.entity.InterfaceInfo;
import com.lxl.lxlApi.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86135
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service
* @createDate 2023-11-22 16:53:16
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     *  用户调用接口
     * @param userId
     * @param interfaceId
     * @return
     */
    boolean invokeInterfaceCount(long userId,long interfaceId);
}
