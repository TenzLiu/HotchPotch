package com.tenz.hotchpotch.module.login.presenter;

import com.tenz.hotchpotch.module.login.contract.LoginContract;
import com.tenz.hotchpotch.module.login.model.LoginModel;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 21:55
 * Description: LoginPresenter
 */

public class LoginPresenter extends LoginContract.LoginPresenter {

    public static LoginPresenter newInstance(){
        return new LoginPresenter();
    }

    @Override
    public void login(String account, String password) {
        if(mIModel !=null && mIView != null){
            String result = mIModel.login(account,password);
            if("success".equals(result)){
                mIView.loginSuccess("user_123456");
            }else{
                mIView.loginFail(result);
            }
        }
    }

    @Override
    public LoginContract.ILoginModel getModel() {
        return LoginModel.newInstance();
    }

    @Override
    public void onStart() {

    }

}
