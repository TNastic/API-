package com.lxl.lxlApi.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InvokeInterfaceInfoRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 参数
     */
    private String userRequestParams;
}
