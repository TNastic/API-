package com.lxl.lxlApi;

import com.lxl.lxlApi.service.inner.impl.InnerInterfaceInfoServiceImpl;
import com.lxl.lxlApi.service.inner.impl.InnerUserServiceImpl;
import com.lxl.lxlcommon.model.entity.InterfaceInfo;
import com.lxl.lxlcommon.model.vo.UserVO;
import com.lxl.lxlcommon.service.InnerInterfaceInfoService;
import com.lxl.lxlcommon.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MyApplicationTest {

    @DubboReference
    InnerUserService innerUserService;

    @DubboReference
    InnerInterfaceInfoService innerInterfaceInfoService;

    @Test
    void test(){
        InterfaceInfo get = innerInterfaceInfoService
                .getInterfaceInfoByPathAndMethod("http://localhost:9000/api/endpoint1", "GET");

        Assertions.assertNotNull(get);
    }

    @Test
    void userServiceTest(){
        UserVO invokeUserByAccessKey = innerUserService.getInvokeUserByAccessKey("111");
        Assertions.assertNotNull(invokeUserByAccessKey);
    }

}
