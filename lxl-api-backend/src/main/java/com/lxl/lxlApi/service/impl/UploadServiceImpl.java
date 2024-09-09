package com.lxl.lxlApi.service.impl;

import com.lxl.lxlApi.common.ErrorCode;
import com.lxl.lxlApi.constant.FileConstant;
import com.lxl.lxlApi.exception.BusinessException;
import com.lxl.lxlApi.service.UploadService;
import com.lxl.lxlApi.util.QiNiuUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Override
    public String upload(MultipartFile file, String fileType) {
        //文件名处理
        if (file.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension; // 生成随机文件名
        //文件名验证
        if (!this.validateFileName(fileName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //图片数据处理
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = (FileInputStream) file.getInputStream();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        String url = "";
        if (fileType.equals(FileConstant.IMAGE)) {
            try {
                url = new QiNiuUtil().upload(fileInputStream, FileConstant.IMAGE);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        } else if (fileType.equals(FileConstant.FILE)) {
            try {
                url = new QiNiuUtil().upload(fileInputStream, FileConstant.FILE);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        }
        return url;
    }

    /**
     * 验证文件名称：仅包含 汉字、字母、数字、下划线和点号
     *
     * @param fileName 文件名称
     * @return 返回true表示符合要求
     */
    private boolean validateFileName(String fileName) {
        String regex = "^[a-zA-Z0-9_\\u4e00-\\u9fa5_\\-.]+$";
        return fileName.matches(regex);
    }
}
