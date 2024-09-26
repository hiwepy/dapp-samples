package com.github.hiwepy.dapp.exception;

import com.github.hiwepy.api.ApiRestResponse;
import com.github.hiwepy.api.Constants;
import com.github.hiwepy.api.CustomApiCode;
import com.github.hiwepy.api.exception.BizRuntimeException;

/**
 * 1、通用异常参数枚举
 * a、国际化key 是大写的字符 _ 拼接，统一使用小写的如，api.not.start
 * b、变量按服务区分，统一加上服务名， 比如 third.api.fail 就是 third 服务的，user.info.save.fail 就表示 user 服务的
 * c、编码按服务进行分段，比如
 * common：10000 - 20000，
 * user：20000+
 */
public enum BizExceptionCode implements CustomApiCode {

	// common

	SIGN_MISSING("1403", 1403, "Signature information missing", "签名信息缺失"),

	SUCCESS_V1("SUCCESS", 200, "success", "SUCCESS"),
	FAILED("FAILED", 1000, "failed", "失败"),
	REPEATED("REPEATED", 2000, "repeated submit", "repeated submit"),
	SYSTEM_ERROR("system.error", 9999, "system error", "流量过大系统开小差啦，请尝试重新发起"),
	SYSTEM_UPGRADING("system.upgrading", 9999, "服务正在升级维护...，请稍后再试！", "服务正在升级维护...，请稍后再试！"),
	SYSTEM_DEPEND_UPGRADING("system.depend.upgrading", 9999, "依赖服务正在升级维护...，请稍后再试！", "依赖服务正在升级维护...，请稍后再试！"),
	REMOTE_INVOKE_ERROR("remote.invoke.error", 9998, "Remote Invoke Error", "远程调用失败"),
	MESSAGE_KEY_IS_NULL("message.key.is.null", 9997, "messageKeyIsNull", "messageKey不能为空"),
	DATABASE_FILTERS_EXISTS("database.filters.exists", 9996, "", ""),
	PARAM_NOT_EXISTS("param.not.exists", 9995, "", ""),
	LOGIN_FAILED("login.failed", 10010, "Login Failed", "登录失败"),
	USER_NOT_FOUND("user.not.found", 10011, "User Not Found", "用户不存在"),
	SIGN_CLAIM_FAILED("sign.claim.failed", 10011, "Sign Claim Failed", "签到失败"),


	;

	/**
	 * 国际化Key
	 */
	private String i18nKey;

	/**
	 * 错误编号
	 */
	private int errorCode;

	/**
	 * 错误信息
	 */
	private String errorMsg;

	/**
	 * 错误描述
	 */
	private String desc;


	private BizExceptionCode(String i18nKey, int errorCode, String errorMsg, String desc) {
		this.i18nKey = i18nKey;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.desc = desc;
	}

	public void throwException() {
		throw this.asException();
	}

	public static void throwByErrorcode(Integer errorCode) {
		getByErrorcode(errorCode).throwException();
	}

	public static void throwByResponse(ApiRestResponse<?> response) {
		throw new BizRuntimeException(response.getCode(), response.getMessage());
	}

	public static BizExceptionCode getByErrorcode(Integer errorCode) {
		if (errorCode == null) {
			return BizExceptionCode.SYSTEM_ERROR;
		}
		BizExceptionCode[] errorCodes = values();

		for (BizExceptionCode bizErrorCode : errorCodes) {
			if (errorCode.equals(bizErrorCode.getErrorCode())) {
				return bizErrorCode;
			}
		}
		return BizExceptionCode.SYSTEM_ERROR;
	}

	public BizRuntimeException asException() {
		return new BizRuntimeException(this.getErrorCode(), this.getI18nKey(), this.getErrorMsg());
	}

	public String getI18nKey() {
		return i18nKey;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	@Override
	public int getCode() {
		return errorCode;
	}

	@Override
	public String getReason() {
		return errorMsg;
	}

	@Override
	public String getStatus() {
		return Constants.RT_FAIL;
	}

}
