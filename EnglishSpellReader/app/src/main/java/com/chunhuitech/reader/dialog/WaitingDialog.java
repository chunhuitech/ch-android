package com.chunhuitech.reader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chunhuitech.reader.R;

public class WaitingDialog extends Dialog {

    public WaitingDialog(Context context) {
        super(context, R.style.TransDialogStyle);
        initDialog(context);
    }

    private void initDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dialog_loading,null);


        setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        // 是否可以按“返回键”消失
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.CENTER);
            window.setAttributes(lp);
        }
    }
}
