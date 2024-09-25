package com.github.hiwepy.dapp.exception;

import com.github.hiwepy.api.ApiCode;
import com.github.hiwepy.api.CustomApiCode;
import com.github.hiwepy.api.exception.BizRuntimeException;

public class ChainSignException extends BizRuntimeException {

    public ChainSignException(int code) {
        super(code);
    }

    public ChainSignException(String msg) {
        super(msg);
    }

    public ChainSignException(int code, String msg) {
        super(code, msg);
    }

    public ChainSignException(ApiCode code, String i18n) {
        super(code, i18n);
    }

    public ChainSignException(int code, String i18n, String defMsg) {
        super(code, i18n, defMsg);
    }

    public ChainSignException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }

    public ChainSignException(int code, String i18n, String defMsg, Throwable cause) {
        super(code, i18n, defMsg, cause);
    }

    public ChainSignException(CustomApiCode code) {
        super(code);
    }
}
