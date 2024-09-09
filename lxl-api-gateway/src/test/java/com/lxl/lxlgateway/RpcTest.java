package com.lxl.lxlgateway;




import com.lxl.lxlcommon.service.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RpcTest {

    @DubboReference
    DemoService demoService;

    @Test
    void testRpc() {
        System.out.println(demoService.sayHello("我靠，终于给你搞好了"));
    }
}
