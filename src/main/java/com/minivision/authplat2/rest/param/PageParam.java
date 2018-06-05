package com.minivision.authplat2.rest.param;

import javax.validation.constraints.Max;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页处理入参
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
@ToString
public class PageParam extends RestParam {

	private static final long serialVersionUID = 3504109724633498804L;

    private int offset = 0;
    
    @Max(100)
    private int limit = 10;

    private String search;
    
}
