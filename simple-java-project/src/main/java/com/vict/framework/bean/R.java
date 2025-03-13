package com.vict.framework.bean;

import com.alibaba.fastjson.JSONObject;
import com.vict.framework.Constants;
import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {
	private static final long serialVersionUID = -3344341032720339347L;

	private String code = "success";

	private String msg = null;

	private T data = (T) new JSONObject();


	public R() {
		super();
	}

	public R(T data) {
		super();
		this.data = data;
	}

	public R(T data, String msg) {
		super();
		this.data = data;
		this.msg = msg;
	}

	public R(String code, String msg, T data) {
		super();
		this.data = data;
		this.msg = msg;
		this.code = code;
	}

	public R(Throwable e) {
		super();
		this.msg = e.getMessage();
		this.code = null;
	}

	public static <T> R<T> ok() {
		return (R<T>) restResult(Constants.success, null, Constants.successMsg);
	}

	public static <T> R<T> ok(T data) {
		return restResult(Constants.success, data, Constants.successMsg);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(Constants.success, data, msg);
	}

	public static <T> R<T> failed(String msg) {
		return (R<T>) restResult(Constants.fail, null, msg);
	}

	public static <T> R<T> failed(String code, String msg) {
		return (R<T>) restResult(code, null, msg);
	}

	public static <T> R<T> failed(String code, String msg, T data) {
		R<T> result = (R<T>) restResult(code, null, msg);
		result.setData(data);
		return result;
	}

	private static <T> R<T> restResult(String code, T data, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data == null ? (T) new JSONObject() : data);
		apiResult.setMsg(msg);
		return apiResult;
	}

	@Override
	public String toString() {
		return "{" +
				"code=" + code +
				", data=" + data +
				", msg='" + msg + '\'' +
				'}';
	}
}
