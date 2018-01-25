package com.tenz.hotchpotch.rx;

import android.accounts.NetworkErrorException;

import com.tenz.hotchpotch.http.BaseResponse;
import com.tenz.hotchpotch.util.ToastUtil;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Author: TenzLiu
 * Date: 2018-01-25 11:16
 * Description: BaseObserver 封装观察者(请求错误，成功，token过期处理)
 */

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        if(tBaseResponse.isTokenOverdue()){
            try {
                onCodeOverdue("token过期，请重新登陆");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (tBaseResponse.isSuccess()){
            try {
                onSuccess(tBaseResponse.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                onError(tBaseResponse.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof HttpException
                    || e instanceof UnknownHostException) {
                onError("网络异常");
            } else {
                onError(e.toString());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 请求数据成功返回
     * @param data
     * @throws Exception
     */
    protected abstract void onSuccess(T data) throws Exception;

    /**
     * 请求token过期返回
     * @throws Exception
     */
    protected void onCodeOverdue(String message) throws Exception{
        ToastUtil.showToast(message);
    }

    /**
     * 请求数据失败返回
     * @param message
     */
    protected void onError(String message) throws Exception{
        ToastUtil.showToast(message);
    }

}
