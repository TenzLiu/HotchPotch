package com.tenz.hotchpotch.module.login.model;

import com.tenz.hotchpotch.module.login.contract.LoginContract;

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
    public String login(String account, String password) {
        if("user".equals(account)&&"123456".equals(password)){
            return "success";
        }else{
            return "账号或密码有误";
        }
    }

}
