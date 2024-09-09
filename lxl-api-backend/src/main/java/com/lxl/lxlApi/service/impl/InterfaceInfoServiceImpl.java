package com.lxl.lxlApi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxl.lxlApi.common.ErrorCode;
import com.lxl.lxlApi.exception.BusinessException;
import com.lxl.lxlApi.mapper.InterfaceInfoMapper;
import com.lxl.lxlcommon.model.entity.InterfaceInfo;
import com.lxl.lxlApi.service.InterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 86135
* @description 针对表【interface_info(接口表)】的数据库操作Service实现
* @createDate 2023-11-15 22:13:56
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService{


    /**
     * 校验
     * @param interfaceInfo
     * @param add
     */
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        // 信息为空，请求参数错误，抛出异常
        if(interfaceInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();

        if(add){
            // 不为空
            if (StringUtils.isAnyBlank(name) || ObjectUtils.anyNull(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        //有参数则校验
        if (StringUtils.isNoneBlank(name) && name.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"名字过长");
        }


    }
}




