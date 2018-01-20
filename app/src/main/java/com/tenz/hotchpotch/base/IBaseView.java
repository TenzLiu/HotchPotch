package com.tenz.hotchpotch.base;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 16:43
 * Description: IBaseView
 */

public interface IBaseView {

    /**
     * 显示Toast
     * @param message
     */
    void showToast(String message);

    /**
     * 显示加载框
     */
    void showLoadingDialog();

    /**
     * 显示加载框
     */
    void showLoadingDialog(String message);

    /**
     * 隐藏加载框
     */
    void dismissLoadingDialog();

    /**
     * 隐藏键盘
     */
    void hideKeyBord();

    /**
     * 回退
     */
    void back();

}
