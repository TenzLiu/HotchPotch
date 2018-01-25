package com.tenz.hotchpotch.module.login.presenter;

import com.tenz.hotchpotch.http.BaseResponse;
import com.tenz.hotchpotch.module.login.contract.LoginContract;
import com.tenz.hotchpotch.module.login.entity.Login;
import com.tenz.hotchpotch.module.login.model.LoginModel;
import com.tenz.hotchpotch.rx.BaseObserver;
import com.tenz.hotchpotch.rx.RxScheduler;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 21:55
 * Description: LoginPresenter
 */

public class LoginPresenter extends LoginContract.LoginPresenter {

    /**
     * 构造方法
     *
     * @param provider RxLifecycle管理生命周期
     */
    public LoginPresenter(LifecycleProvider<ActivityEvent> provider) {
        super(provider);
    }

    @Override
    public void login(String account, String password) {
        if(mIModel !=null && mIView != null){
            mIView.showLoadingDialog();
            mIModel.login(account, password)
                    .compose(RxScheduler.<BaseResponse<Login>>rxSchedulerTransform())
                    .compose(getLifecycleProvider().<BaseResponse<Login>>bindToLifecycle())
                    .subscribe(new BaseObserver<Login>() {
                        @Override
                        protected void onSuccess(Login login) throws Exception {
                            mIView.dismissLoadingDialog();
                            mIView.loginSuccess(login.getToken());
                        }

                        @Override
                        protected void onCodeOverdue(String message) throws Exception {
                            super.onCodeOverdue(message);
                            //可不重写，在BaseObserver做退出登录操作
                        }

                        @Override
                        protected void onError(String message) throws Exception {
                            //可不重写，已把错误信息Toast给用户（需要做处理时重写）
                            super.onError(message);
                        }
                    });
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
