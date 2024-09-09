package com.lxl.lxlcommon.service;

import com.lxl.lxlcommon.model.entity.User;
import com.lxl.lxlcommon.model.vo.UserVO;

public interface InnerUserService {

    /**
     * 根据accessKey获得user，再获得secretKey
     *
     * @param accessKey
     * @return {@link User}
     */
    UserVO getInvokeUserByAccessKey(String accessKey);

    /**
     * 用户调用接口，需要更改用户的接口调用次数和剩余接口调用次数
     * @param userId
     * @return
     */
    Boolean userInvokeInterface(long userId);
}
