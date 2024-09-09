package com.lxl.lxlApi.controller;

import com.lxl.lxlApi.common.BaseResponse;
import com.lxl.lxlApi.common.ErrorCode;
import com.lxl.lxlApi.common.ResultUtils;
import com.lxl.lxlApi.exception.BusinessException;
import com.lxl.lxlApi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.LinkedList;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    //图片路径
    @Value("${api.path}")
    private String basePath;

    @Resource
    private UserService userService;

    @PostMapping("/upload")
    public BaseResponse<String> handleFileUpload(MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension; // 生成随机文件名
        //保存到数据库
        userService.userUploadAvatar(userService.getLoginUser(request).getId(),newFileName);
        String filePath = basePath+newFileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success("上传成功");
    }

}
