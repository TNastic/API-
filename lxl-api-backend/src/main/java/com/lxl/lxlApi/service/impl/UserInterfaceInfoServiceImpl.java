package com.lxl.lxlApi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxl.lxlApi.common.ErrorCode;
import com.lxl.lxlApi.exception.BusinessException;
import com.lxl.lxlApi.model.entity.UserInterfaceInfo;
import com.lxl.lxlApi.service.UserInterfaceInfoService;
import com.lxl.lxlApi.mapper.UserInterfaceInfoMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 86135
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service实现
* @createDate 2023-11-22 16:53:16
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        // 信息为空，请求参数错误，抛出异常
        if(userInterfaceInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    @Transactional
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
        return update(updateWrapper);
    }
}




