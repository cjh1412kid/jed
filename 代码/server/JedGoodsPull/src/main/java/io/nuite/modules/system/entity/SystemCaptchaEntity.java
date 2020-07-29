package io.nuite.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统验证码
 * 
 * @author fl
 * @since 2.0.0 2018-04-20
 */
@TableName("YHSR_Other_Captcha")
public class SystemCaptchaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.INPUT)
	private String uuid;
	/**
	 * 验证码
	 */
	@TableField(value = "code")
	private String code;
	/**
	 * 过期时间
	 */
	@TableField(value = "expireTime")
	private Date expireTime;

	public String getUuid() {
		return uuid;
	}

	public String getCode() {
		return code;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

}
