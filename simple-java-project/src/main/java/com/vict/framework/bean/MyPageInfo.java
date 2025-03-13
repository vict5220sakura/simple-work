package com.vict.framework.bean;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xjp
 * @version 1.0
 * @date 2024-02-27 10:57
 */
@Data
public class MyPageInfo<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 总条数
	 */
	private String total;
	/**
	 * 页码
	 */
	private String pageNum;
	/**
	 * 每页条数
	 */
	private String pageSize;
	/**
	 * 总页数
	 */
	private String totalPage;
	/**
	 * 数据
	 */
	private List<T> list;

	public MyPageInfo(){}

	public MyPageInfo(Page page) {
		this.total = Optional.ofNullable(page).map(o-> o.getTotal()).map(o-> o.toString()).orElse(null);
		this.pageNum = Optional.ofNullable(page).map(o-> o.getPageNum()).map(o-> o.toString()).orElse(null);
		this.pageSize = Optional.ofNullable(page).map(o-> o.getPageSize()).map(o-> o.toString()).orElse(null);
		this.totalPage = Optional.ofNullable(page).map(o-> o.getPages()).map(o-> o.toString()).orElse(null);
		this.list = Optional.ofNullable(page).map(o-> o.getResult()).orElse(null);
	}

	public static <T> MyPageInfo<T> copyPageNoList(Page page, Class<T> clazz){
		MyPageInfo<T> myPageInfo = new MyPageInfo<>();

		myPageInfo.total = Optional.ofNullable(page).map(o-> o.getTotal()).map(o-> o.toString()).orElse(null);
		myPageInfo.pageNum = Optional.ofNullable(page).map(o-> o.getPageNum()).map(o-> o.toString()).orElse(null);
		myPageInfo.pageSize = Optional.ofNullable(page).map(o-> o.getPageSize()).map(o-> o.toString()).orElse(null);
		myPageInfo.totalPage = Optional.ofNullable(page).map(o-> o.getPages()).map(o-> o.toString()).orElse(null);
		myPageInfo.list = new ArrayList<T>();

		return myPageInfo;
	}

	public MyPageInfo(Page page, Class<T> clazz) {
		this.total = Optional.ofNullable(page).map(o-> o.getTotal()).map(o-> o.toString()).orElse(null);
		this.pageNum = Optional.ofNullable(page).map(o-> o.getPageNum()).map(o-> o.toString()).orElse(null);
		this.pageSize = Optional.ofNullable(page).map(o-> o.getPageSize()).map(o-> o.toString()).orElse(null);
		this.totalPage = Optional.ofNullable(page).map(o-> o.getPages()).map(o-> o.toString()).orElse(null);
		this.list = Optional.ofNullable(page).map(o-> o.getResult()).orElse(null);
	}

	public MyPageInfo(String total, String pageNum, String pageSize, String totalPage, List<T> list) {
		this.total = total;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.list = list;
	}

	public MyPageInfo(PageInfo pageInfo) {
		this.total = String.valueOf(pageInfo.getTotal());
		this.pageNum = String.valueOf(pageInfo.getPageNum());
		this.pageSize = String.valueOf(pageInfo.getPageSize());
		this.totalPage = String.valueOf(pageInfo.getPages());
		this.list = pageInfo.getList();
	}
}
