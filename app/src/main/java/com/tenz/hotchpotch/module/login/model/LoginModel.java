package com.tenz.hotchpotch.module.login.model;

import com.tenz.hotchpotch.api.LoginApi;
import com.tenz.hotchpotch.http.BaseResponse;
import com.tenz.hotchpotch.http.RetrofitFactory;
import com.tenz.hotchpotch.module.login.contract.LoginContract;
import com.tenz.hotchpotch.module.login.entity.Login;

import io.reactivex.Observable;


/**
 * Author: TenzLiu
 * Date: 2018-01-20 21:53
 * Description: LoginModel
 */

public class LoginModel implements LoginContract.ILoginModel {

    public static LoginModel newInstance(){
        return new LoginModel();
    }

    @Override
    public Observable<BaseResponse<Login>> login(String account, String password) {

        return RetrofitFactory.getInstance().createApi(LoginApi.class).login();

    }

}
