package com.tenz.hotchpotch.http;

/**
 * Author: TenzLiu
 * Date: 2018-01-22 11:39
 * Description: BaseResponse 解析实体基类
 */

public class BaseResponse<T> {

    private int code;
    private String message;
    private T data;
    private static final int CODE_SUCCESS = 1;//成功的code
    private static final int CODE_OVERDUE = 2;//token过期的code

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 是否请求数据成功
     * @return
     */
    public boolean isSuccess(){
        return getCode() == CODE_SUCCESS;
    }

    /**
     * 是否请求token过期
     * @return
     */
    public boolean isTokenOverdue(){
        return getCode() == CODE_OVERDUE;
    }

}
