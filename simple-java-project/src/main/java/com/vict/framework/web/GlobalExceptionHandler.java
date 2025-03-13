package com.vict.framework.web;

import com.vict.framework.bean.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	// /**
	//  * 自定义业务异常
	//  */
	// @ExceptionHandler(BusinessException.class)
	// @ResponseBody
	// public R handleBusinessException(BusinessException e) {
	// 	String message = e.getMessage();
	// 	log.error(message + "异常", e);
	// 	return R.failed(e.getCode(), e.getMessage());
	// }

	/**
	 * 校验异常自定义
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		AtomicReference<String> messageAto = new AtomicReference<>("");
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			messageAto.set(messageAto.get() + errorMessage);
			messageAto.set(messageAto.get() + ", ");
		});

		// 通用异常处理逻辑
		String message = messageAto.get();
		R<Object> failed = R.failed(message);

		e.printStackTrace();

		return failed;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public R handleException(Exception e) {
		// 通用异常处理逻辑
		String message = e.getMessage();
		message = Optional.ofNullable(message).map(o -> o.trim()).filter(o -> !o.equals("")).orElse("未知异常");
		// if (message.length() > 20) {
		// 	message = message.substring(0, 20);
		// }
		R<Object> failed = R.failed(message);

		log.error(message + "异常", e);

		return failed;
	}
}
