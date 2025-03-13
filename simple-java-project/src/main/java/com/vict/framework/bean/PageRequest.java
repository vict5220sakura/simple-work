package com.vict.framework.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页请求
 */
@Data
public class PageRequest {
	@ApiModelProperty(value = "页码，默认为1", example = "1")
	private Integer pageNum = 1;

	@ApiModelProperty(value = "每页显示条数，不能小于1，默认20", example = "20")
	private Integer pageSize = 20;
}
