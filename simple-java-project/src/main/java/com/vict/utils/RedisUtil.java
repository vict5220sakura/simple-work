package com.vict.utils;

import com.vict.framework.Constants;
import com.vict.framework.FrameworkCommon;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author xjp
 * @version 1.0
 * @date 2024-03-04 11:32
 */
public class RedisUtil {

	/**
	 * 获取StringRedisTemplate
	 *
	 * @return StringRedisTemplate
	 */
	public static StringRedisTemplate getStringRedisTemplate() {
		if(FrameworkCommon.redis_enable == false){
			return null;
		}
		return SpringUtils.getBean(Constants.framworkStringRedisTemplate, StringRedisTemplate.class);
	}
}
