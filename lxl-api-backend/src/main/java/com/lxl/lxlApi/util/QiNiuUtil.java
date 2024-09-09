package com.lxl.lxlApi.util;

import com.google.gson.Gson;
import com.lxl.lxlApi.constant.FileConstant;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.FileInputStream;

public class QiNiuUtil {

    /**
     * 将图片上传到七牛云
     */
    public String upload(FileInputStream file, String fileType) throws Exception {
        // zone0华东区域,zone1是华北区域,zone2是华南区域,新加坡
        Configuration cfg = new Configuration(Region.xinjiapo());
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        Auth auth = Auth.create(FileConstant.accessKey, FileConstant.secretKey);
        String upToken = null;
        String path = null;
        if (fileType.equals(FileConstant.IMAGE)) {
            upToken = auth.uploadToken(FileConstant.bucketPictureName);
            path = FileConstant.domainPicture;
        } else if (fileType.equals(FileConstant.FILE)) {
            upToken = auth.uploadToken(FileConstant.bucketFileName);
            path = FileConstant.domainFile;
        }
        Response response = uploadManager.put(file, null, upToken, null, null);
        // 解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return path + putRet.key;
    }
}
