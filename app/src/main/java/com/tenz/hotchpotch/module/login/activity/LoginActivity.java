package com.tenz.hotchpotch.module.login.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.app.Constant;
import com.tenz.hotchpotch.base.BaseMvpActivity;
import com.tenz.hotchpotch.module.login.contract.LoginContract;
import com.tenz.hotchpotch.module.login.model.LoginModel;
import com.tenz.hotchpotch.module.login.presenter.LoginPresenter;
import com.tenz.hotchpotch.module.main.MainActivity;
import com.tenz.hotchpotch.util.SpUtil;
import com.tenz.hotchpotch.util.StringUtil;
import com.tenz.hotchpotch.widget.edittext.ClearWriteEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 18:42
 * Description: LoginActivity
 */

public class LoginActivity extends BaseMvpActivity<LoginPresenter,LoginModel>
        implements LoginContract.ILoginView {

    @BindView(R.id.iv_login_bg)
    ImageView iv_login_bg;
    @BindView(R.id.et_account)
    ClearWriteEditText et_account;
    @BindView(R.id.et_password)
    ClearWriteEditText et_password;

    private String mAccount;
    private String mPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_login_bg);
        iv_login_bg.setAnimation(animation);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick({R.id.btn_login})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                mAccount = et_account.getText().toString().trim();
                mPassword = et_password.getText().toString().trim();
                if(StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)){
                    showToast("请输入账号跟密码");
                }else{
                    mPresenter.login(mAccount,mPassword);
                }
                break;
        }
    }

    @NonNull
    @Override
    protected LoginPresenter initPresenter() {
        return LoginPresenter.newInstance();
    }

    @Override
    public void loginSuccess(String token) {
        showToast("登录成功");
        SpUtil.putBoolean(mContext, Constant.KEY_IS_LOGIN,true);
        startActivity(MainActivity.class);
    }

    @Override
    public void loginFail(String message) {
        showToast(message);
    }

}
