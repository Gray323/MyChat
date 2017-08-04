package com.rxd.mychat.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxd.mychat.R;

/**
 * Created by Administrator on 2017/7/11.
 */

public class LoadingDialog extends Dialog{
    private TextView tv;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.ll_loading);
        linearLayout.getBackground().setAlpha(210);
    }
}
