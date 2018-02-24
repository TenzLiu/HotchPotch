package com.tenz.hotchpotch.widget.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.util.StringUtil;

/**
 * Author: TenzLiu
 * Date: 2018-02-23 14:33
 * Description: ConfirmDialog
 */

public class ConfirmDialog {

    /**
     *
     * @param fragmentManager
     * @param msg
     * @param confirmDialogListener
     * @return
     */
    public static BaseNiceDialog show(FragmentManager fragmentManager, String msg,
                                   ConfirmDialogListener confirmDialogListener){
        return show(fragmentManager,"",msg,"取消", "确定",
                true,confirmDialogListener);
    }

    /**
     *
     * @param fragmentManager
     * @param title
     * @param msg
     * @param confirmDialogListener
     * @return
     */
    public static BaseNiceDialog show(FragmentManager fragmentManager, String title, String msg,
                                   ConfirmDialogListener confirmDialogListener){
        return show(fragmentManager,title,msg,"取消", "确定",
                true,confirmDialogListener);
    }

    /**
     *
     * @param fragmentManager
     * @param title
     * @param msg
     * @param negativeMsg
     * @param positiveMsg
     * @param confirmDialogListener
     * @return
     */
    public static BaseNiceDialog show(FragmentManager fragmentManager, String title, String msg,
                                   String negativeMsg, String positiveMsg,
                                   ConfirmDialogListener confirmDialogListener){
        return show(fragmentManager,title,msg,negativeMsg, positiveMsg,
                true,confirmDialogListener);
    }

    /**
     *
     * @param fragmentManager        fragmentManager
     * @param title                  标题
     * @param msg                    内容
     * @param negativeMsg            取消内容
     * @param positiveMsg            确定内容
     * @param outCancel              是否可以取消
     * @param confirmDialogListener  点击事件
     */
    public static BaseNiceDialog show(FragmentManager fragmentManager, String title, String msg,
                                   String negativeMsg, String positiveMsg,boolean outCancel,
                                   ConfirmDialogListener confirmDialogListener){
        return NiceDialog.init()
                .setLayoutId(R.layout.layout_dialog_confirm)     //设置dialog布局文件
                .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        //是否有标题
                        if(StringUtil.isEmpty(title)){
                            holder.getView(R.id.tv_title).setVisibility(View.GONE);
                        }
                        //是否有取消按钮
                        if(StringUtil.isEmpty(negativeMsg)){
                            holder.getView(R.id.tv_negative).setVisibility(View.GONE);
                            holder.getView(R.id.view_center_line).setVisibility(View.GONE);
                        }
                        holder.setText(R.id.tv_title,title);
                        holder.setText(R.id.tv_content,msg);
                        holder.setText(R.id.tv_negative,negativeMsg);
                        holder.setText(R.id.tv_positive,positiveMsg);
                        holder.setOnClickListener(R.id.tv_negative, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialogListener.onNegative(dialog);
                            }
                        });
                        holder.setOnClickListener(R.id.tv_positive, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialogListener.onPositive(dialog);
                            }
                        });

                    }
                })
                .setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
                //.setShowBottom(true)     //是否在底部显示dialog，默认flase
                .setMargin(50)     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
                //.setWidth()     //dialog宽度（单位：dp），默认为屏幕宽度，-1代表WRAP_CONTENT
                //.setHeight()     //dialog高度（单位：dp），默认为WRAP_CONTENT
                .setOutCancel(outCancel)     //点击dialog外是否可取消，默认true
                //.setAnimStyle(R.style.EnterExitAnimation)     //设置dialog进入、退出的动画style(底部显示的dialog有默认动画)
                .show(fragmentManager);     //显示dialog
    }

    public static abstract class ConfirmDialogListener {
        /**
         * 取消
         */
        public abstract void onNegative(BaseNiceDialog dialog);

        /**
         * 确定
         */
        public abstract void onPositive(BaseNiceDialog dialog);
    }

}
