package com.tenz.hotchpotch.module.login.contract;

import com.tenz.hotchpotch.base.BasePresenter;
import com.tenz.hotchpotch.base.IBaseModel;
import com.tenz.hotchpotch.base.IBaseView;
import com.tenz.hotchpotch.http.BaseResponse;
import com.tenz.hotchpotch.module.login.entity.Login;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 21:48
 * Description: LoginContract
 */

public interface LoginContract {

    abstract class LoginPresenter extends BasePresenter<ILoginModel,ILoginView>{


        /**
         * 构造方法
         *
         * @param activityProvider activity管理生命周期
         * @param fragmentProvider fragment管理生命周期
         */
        public LoginPresenter(LifecycleProvider<ActivityEvent> activityProvider,
                              LifecycleProvider<FragmentEvent> fragmentProvider) {
            super(activityProvider, fragmentProvider);
        }

        public abstract void login(String account, String password);

    }

    interface ILoginModel extends IBaseModel{

        Observable<BaseResponse<Login>> login(String account, String password);

    }

    interface ILoginView extends IBaseView{

        void loginSuccess(String token);

        void loginFail(String message);

    }

}
