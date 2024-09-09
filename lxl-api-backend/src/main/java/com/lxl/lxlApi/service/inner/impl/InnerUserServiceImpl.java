package com.lxl.lxlApi.service.inner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxl.lxlApi.common.ErrorCode;
import com.lxl.lxlApi.exception.BusinessException;
import com.lxl.lxlcommon.model.entity.User;
import com.lxl.lxlApi.service.UserService;
import com.lxl.lxlcommon.model.vo.UserVO;
import com.lxl.lxlcommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    UserService userService;

    /**
     *  查询
     * @param accessKey
     * @return
     */
    @Override
    public UserVO getInvokeUserByAccessKey(String accessKey) {
        //判空
        //查询
        //返回
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getAccessKey, accessKey);
        User user = userService.getOne(userLambdaQueryWrapper);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }

    @Override
    @Transactional
    public Boolean userInvokeInterface(long userId) {
        //判断
        if (userId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //调用次数加一，剩余调用次数减一
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,userId)
                .gt(User::getRestCount,0)
                .setSql("restCount = restCount - 1 , hasCounted = hasCounted + 1");
        return userService.update(updateWrapper);
    }
}
