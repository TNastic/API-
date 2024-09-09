package com.lxl.lxlApi.config;

import com.lxl.lxlApi.constant.FileConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss.qiniu")
public class QiNiuInitConfig {

    /**
     * AccessKey
     */
    private String accessKey;
    /**
     * SecretKey
     */
    private String secretKey;
    /**
     * 图片存储空间名
     */
    private String bucketPictureName;
    /**
     * 图片外链
     */
    private String domainPicture;
    /**
     * 文件存储空间名
     */
    private String bucketFileName;
    /**
     * 文件外链
     */
    private String domainFile;

    @Bean
    public void init() {
        FileConstant.accessKey = this.accessKey;
        FileConstant.secretKey = this.secretKey;
        FileConstant.bucketPictureName = this.bucketPictureName;
        FileConstant.domainPicture = this.domainPicture;
        FileConstant.bucketFileName = this.bucketFileName;
        FileConstant.domainFile = this.domainFile;
    }
}
