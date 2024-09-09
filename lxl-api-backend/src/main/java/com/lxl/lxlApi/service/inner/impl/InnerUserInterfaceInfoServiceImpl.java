package com.lxl.lxlApi.service.inner.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxl.lxlApi.common.ErrorCode;
import com.lxl.lxlApi.exception.BusinessException;
import com.lxl.lxlApi.model.entity.UserInterfaceInfo;
import com.lxl.lxlApi.service.UserInterfaceInfoService;
import com.lxl.lxlcommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeInterfaceCount(long userId, long interfaceId) {
        //校验
        if (userId <=0 || interfaceId <=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //剩余调用次数减一，总调用次数加一
        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInterfaceInfo::getUserId,userId)
                .gt(UserInterfaceInfo::getLeftCount,0)
                .setSql("leftCount = leftCount - 1,totalCount = totalCount + 1");
        //同步的话应该加锁
        return userInterfaceInfoService.update(updateWrapper);
    }
}
