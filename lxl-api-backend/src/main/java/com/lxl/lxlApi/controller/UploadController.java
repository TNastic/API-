package com.lxl.lxlApi.controller;

import com.lxl.lxlApi.common.BaseContext;
import com.lxl.lxlApi.common.BaseResponse;
import com.lxl.lxlApi.common.ErrorCode;
import com.lxl.lxlApi.common.ResultUtils;
import com.lxl.lxlApi.constant.FileConstant;
import com.lxl.lxlApi.exception.BusinessException;
import com.lxl.lxlApi.service.UploadService;
import com.lxl.lxlApi.service.UserService;
import com.lxl.lxlcommon.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static com.lxl.lxlApi.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@Slf4j
public class UploadController {

    @Resource
    UserService userService;


    @Resource
    private UploadService uploadService;

    @PostMapping("/image/upload")
    public BaseResponse<String> fileUpload(MultipartFile file,HttpServletRequest request) {
        String url = uploadService.upload(file, FileConstant.IMAGE);
        //保存url到数据库
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        user.setUserAvatar(url);
        userService.updateById(user);
        return ResultUtils.success(url);
    }



}
