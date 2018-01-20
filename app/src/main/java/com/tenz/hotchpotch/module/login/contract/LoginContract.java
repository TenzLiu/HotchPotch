package com.tenz.hotchpotch.module.login.contract;

import com.tenz.hotchpotch.base.BasePresenter;
import com.tenz.hotchpotch.base.IBaseModel;
import com.tenz.hotchpotch.base.IBaseView;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 21:48
 * Description: LoginContract
 */

public interface LoginContract {

    abstract class LoginPresenter extends BasePresenter<ILoginModel,ILoginView>{

        public abstract void login(String account, String password);

    }

    interface ILoginModel extends IBaseModel{

        String login(String account, String password);

    }

    interface ILoginView extends IBaseView{

        void loginSuccess(String token);

        void loginFail(String message);

    }

}
