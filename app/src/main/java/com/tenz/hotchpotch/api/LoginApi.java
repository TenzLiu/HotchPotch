package com.tenz.hotchpotch.api;

import com.tenz.hotchpotch.http.BaseResponse;
import com.tenz.hotchpotch.module.login.entity.Login;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Author: TenzLiu
 * Date: 2018-01-22 11:47
 * Description: LoginApi
 */

public interface LoginApi {

    public static final String HOST = "http://www.wanandroid.com/tools/mockapi/2164/";

    //自定义接口与返回数据模拟返回统一格式数据封装(http://www.wanandroid.com/tools/mockapi)
    @GET("hotchpotch_login")
    Observable<BaseResponse<Login>> login();

}
