package com.lxl.lxlApi.model.dto.interfaceinfo;

import com.lxl.lxlApi.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索接口信息功能
 */
@Data
public class InterfaceInfoSearchTextRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索内容文本
     */
    private String searchText;
}
