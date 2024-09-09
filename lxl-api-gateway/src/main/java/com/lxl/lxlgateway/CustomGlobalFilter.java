package com.lxl.lxlgateway;


import com.lxl.lxlapiclientsdk.utils.SignUtils;
import com.lxl.lxlcommon.common.ErrorCode;
import com.lxl.lxlcommon.model.entity.InterfaceInfo;
import com.lxl.lxlcommon.model.vo.UserVO;
import com.lxl.lxlcommon.service.InnerInterfaceInfoService;
import com.lxl.lxlcommon.service.InnerUserInterfaceInfoService;
import com.lxl.lxlcommon.service.InnerUserService;
import com.lxl.lxlgateway.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 自定义全局的异常处理器
 * 类似于servlet的过滤器
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    InnerUserService innerUserService;

    @DubboReference
    InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final long FIVE_MINUTES = 5L * 60;

    //白名单
    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1","127.0.0.3");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        //1. 用户发送请求到API网关
//        //2. 请求日志
//        ServerHttpResponse response = exchange.getResponse();
//        ServerHttpRequest request = exchange.getRequest();
//        String remoteAddress = request.getRemoteAddress().getHostString();
//        log.info("请求地址: {}", remoteAddress);
//        //3. 黑白名单
//        if (!IP_WHITE_LIST.contains(remoteAddress)) {
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }
//        //4. 用户鉴权（判断ak，sk是否合法）  rpc调用
//        HttpHeaders headers = request.getHeaders();
//        String user = headers.getFirst("user");
//        String nonce = headers.getFirst("nonce");
//        String timestamp = headers.getFirst("timestamp");
//        String accessKey = headers.getFirst("accessKey");
//        UserVO invokeUserByAccessKey = innerUserService.getInvokeUserByAccessKey(accessKey);
//        String sign = headers.getFirst("sign");
//        String m = Objects.requireNonNull(request.getMethod()).toString();
//        String u = request.getURI().toString();
//        //检验随机数
//        if (nonce !=null && Long.parseLong(nonce)>10000){
//            return handleNoAuth(response);
//        }
//        //检验时间戳
//        if (timestamp !=null && Math.abs(Long.parseLong(timestamp)-System.currentTimeMillis()/1000) > 120){
//            return handleNoAuth(response);
//        }
//        //检验sign值,secretKey是从数据库中拿出来的
//        String genSign = SignUtils.genSign(user, "abcdefg");
//        // 如果签名不相同
//        if(!genSign.equals(sign)){
//            return handleNoAuth(response);
//        }
//        //5. 请求的模拟接口是否存在？   数据库查询，rpc调用
//        //6. **请求转发，调用模拟接口**
//        Mono<Void> filter = chain.filter(exchange);
//        //7. 响应日志
//        log.info("响应状态码：{}", response.getStatusCode());
//        if (response.getStatusCode() == HttpStatus.OK) {
//            // 8. 调用成功，接口调用次数+1
//        } else {
//            // 9. 调用失败，返回规范错误码
//            return handleInvokeError(response);
//        }
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求id: {}", request.getId());
        log.info("请求路径: {}", request.getPath());
        log.info("请求方法: {}", request.getMethod());
        log.info("请求参数: {}", request.getQueryParams());
        log.info("请求头: {}", request.getHeaders());
        HttpHeaders headers = request.getHeaders();
        log.info("Host: {}",headers.get("Host"));
        log.info("请求地址: {}", Objects.requireNonNull(request.getRemoteAddress()).getHostString());
        return verifyParameters(exchange, chain);
    }


    /**
     * 验证参数
     *
     * @param exchange 交换
     * @param chain    链条
     * @return {@link Mono}<{@link Void}>
     */
   private Mono<Void> verifyParameters(ServerWebExchange exchange, GatewayFilterChain chain){
       ServerHttpRequest request = exchange.getRequest();
       //1.白名单判断
       String hostString = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
       if (!IP_WHITE_LIST.contains(Objects.requireNonNull(hostString))){
           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
       }
       //2.参数判断 ak,sk等等
       HttpHeaders headers = request.getHeaders();
       //用户
       String body = headers.getFirst("user");
       String accessKey = headers.getFirst("accessKey");
       String nonceStr = headers.getFirst("nonce");
       String timestamp = headers.getFirst("timestamp");
       String sign = headers.getFirst("sign");

       //请求头中的参数必须完整
       if(StringUtils.isAnyBlank(body,accessKey,nonceStr,timestamp,sign)){
           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
       }

       //时间校验
       assert timestamp != null;
       if (Math.abs(Long.parseLong(timestamp)-System.currentTimeMillis()/1000) > FIVE_MINUTES){
           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"会话过期");
       }
       //随机数校验
       assert nonceStr != null;
       long nonce = Long.parseLong(nonceStr);
       if (!(nonce > 999 && nonce < 100000)){
           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"非法请求");
       }
       //3.用户信息鉴权
       try {
           UserVO invokeUser = innerUserService.getInvokeUserByAccessKey(accessKey);
           if (invokeUser == null){
               throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"用户不存在");
           }
           //4.接口信息判断
           String method = Objects.requireNonNull(request.getMethod()).toString();
           String uri = request.getURI().toString();
           InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfoByPathAndMethod(uri, method);
           if (interfaceInfo == null){
               //接口为空不存在
               throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"请求接口不存在");
           }
           return handleResponse(exchange,chain,interfaceInfo.getId(),invokeUser.getId());
       }catch (Exception e) {
           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, e.getMessage());
       }
       //5.密钥鉴权

       //5.成功就调用
//       ServerHttpRequest request = exchange.getRequest();
//       // 请求白名单
//       if (!IP_WHITE_LIST.contains(Objects.requireNonNull(request.getRemoteAddress()).getHostString())) {
//           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
//       }
//
//       HttpHeaders headers = request.getHeaders();
//       String body = headers.getFirst("body");
//       String accessKey = headers.getFirst("accessKey");
//       String timestamp = headers.getFirst("timestamp");
//       String sign = headers.getFirst("sign");
//       // 请求头中参数必须完整
//       if (StringUtils.isAnyBlank(body, sign, accessKey, timestamp)) {
//           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
//       }
//       // 防重发XHR
//       long currentTime = System.currentTimeMillis() / 1000;
//       assert timestamp != null;
//       if (currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES) {
//           throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "会话已过期,请重试！");
//       }
//       try {
//           UserVO user = innerUserService.getInvokeUserByAccessKey(accessKey);
//           if (user == null) {
//               throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "账号不存在");
//           }
//           // 校验accessKey
//           if (!user.getAccessKey().equals(accessKey)) {
//               throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "请先获取请求密钥");
//           }
//           String method = Objects.requireNonNull(request.getMethod()).toString();
//           String uri = request.getURI().toString().trim();
//
//           if (StringUtils.isAnyBlank(uri, method)) {
//               throw new BusinessException(ErrorCode.PARAMS_ERROR);
//           }
//           InterfaceInfo interfaceInfo = interfaceInfoService.getInterfaceInfo(uri, method);
//
//           if (interfaceInfo == null) {
//               throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
//           }
//           if (interfaceInfo.getStatus() == InterfaceStatusEnum.AUDITING.getValue()) {
//               throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口审核中");
//           }
//           if (interfaceInfo.getStatus() == InterfaceStatusEnum.OFFLINE.getValue()) {
//               throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口未开启");
//           }
//           MultiValueMap<String, String> queryParams = request.getQueryParams();
//           String requestParams = interfaceInfo.getRequestParams();
//           // 校验请求参数
//           if (StringUtils.isNotBlank(requestParams)) {
//               List<RequestParamsField> list = new Gson().fromJson(requestParams, new TypeToken<List<RequestParamsField>>() {
//               }.getType());
//               for (RequestParamsField requestParamsField : list) {
//                   if ("是".equals(requestParamsField.getRequired())) {
//                       if (StringUtils.isBlank(queryParams.getFirst(requestParamsField.getFieldName())) || !queryParams.containsKey(requestParamsField.getFieldName())) {
//                           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "请求参数有误，" + requestParamsField.getFieldName() + "为必选项，详细参数请参考API文档：https://doc.qimuu.icu/");
//                       }
//                   }
//               }
//           }
//           return handleResponse(exchange, chain);
//       } catch (Exception e) {
//           throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, e.getMessage());
//       }

   }

    /**
     * 处理响应
     *
     * @param exchange 交换
     * @param chain    链条
     * @return {@link Mono}<{@link Void}>
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceId,long userId) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        // 缓存数据的工厂
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        // 拿到响应码
        HttpStatus statusCode = originalResponse.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            // 装饰，增强能力
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                // 等调用完转发的接口后才会执行
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        // 往返回值里写数据
                        return super.writeWith(
                                fluxBody.map(dataBuffer -> {
                                    // todo 业务操作
                                    innerUserInterfaceInfoService.invokeInterfaceCount(userId,interfaceId);
                                    //用户的次数减少的业务操作
                                    innerUserService.userInvokeInterface(userId);
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    // 释放掉内存
                                    DataBufferUtils.release(dataBuffer);
                                    String data = new String(content, StandardCharsets.UTF_8);
                                    // 打印日志
                                    log.info("响应结果：" + data);
                                    return bufferFactory.wrap(content);
                                }));
                    } else {
                        // 8. 调用失败，返回一个规范的错误码
                        log.error("<--- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            // 设置 response 对象为装饰过的
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }
        // 降级处理返回数据
        return chain.filter(exchange);
    }

    /**
     * 处理无权限的行为
     * @param response
     * @return
     */
    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
