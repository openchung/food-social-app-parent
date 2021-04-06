package com.dc.commons.utils;

import com.dc.commons.constant.ApiConstant;
import com.dc.commons.model.domain.ResultInfo;

/**
 * 公共返回對象工具類
 */
public class ResultInfoUtil {

    /**
     * 請求出錯返回
     *
     * @param path 請求路徑
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> buildError(String path) {
        ResultInfo<T> resultInfo = build(ApiConstant.ERROR_CODE,
                ApiConstant.ERROR_MESSAGE, path, null);
        return resultInfo;
    }

    /**
     * 請求出錯返回
     *
     * @param errorCode 錯誤代碼
     * @param message   錯誤提示信息
     * @param path      請求路徑
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> buildError(int errorCode, String message, String path) {
        ResultInfo<T> resultInfo = build(errorCode, message, path, null);
        return resultInfo;
    }

    /**
     * 請求成功返回
     *
     * @param path 請求路徑
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> buildSuccess(String path) {
        ResultInfo<T> resultInfo = build(ApiConstant.SUCCESS_CODE,
                ApiConstant.SUCCESS_MESSAGE, path, null);
        return resultInfo;
    }

    /**
     * 請求成功返回
     *
     * @param path 請求路徑
     * @param data 返回數據對象
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> buildSuccess(String path, T data) {
        ResultInfo<T> resultInfo = build(ApiConstant.SUCCESS_CODE,
                ApiConstant.SUCCESS_MESSAGE, path, data);
        return resultInfo;
    }

    /**
     * 構建返回對象方法
     *
     * @param code
     * @param message
     * @param path
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> build(Integer code, String message, String path, T data) {
        if (code == null) {
            code = ApiConstant.SUCCESS_CODE;
        }
        if (message == null) {
            message = ApiConstant.SUCCESS_MESSAGE;
        }
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(code);
        resultInfo.setMessage(message);
        resultInfo.setPath(path);
        resultInfo.setData(data);
        return resultInfo;
    }

}
