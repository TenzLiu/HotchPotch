package com.tenz.hotchpotch.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 17:56
 * Description: TODO
 */

public class LoadingDialog extends ProgressDialog {

    public LoadingDialog(Context context) {
        this(context,0);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setCanceledOnTouchOutside(false);
    }
}
