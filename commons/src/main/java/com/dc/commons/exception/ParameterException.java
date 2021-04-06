package com.dc.commons.exception;

import com.dc.commons.constant.ApiConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * 全局異常類
 */
@Getter
@Setter
public class ParameterException extends RuntimeException {

    private Integer errorCode;

    public ParameterException() {
        super(ApiConstant.ERROR_MESSAGE);
        this.errorCode = ApiConstant.ERROR_CODE;
    }

    public ParameterException(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ParameterException(String message) {
        super(message);
        this.errorCode = ApiConstant.ERROR_CODE;
    }

    public ParameterException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}