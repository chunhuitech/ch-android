package com.chunhuitech.reader.service;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.dialog.WaitingDialog;

public class MessageService {

    private WaitingDialog waitingDialog;

    public void toastText(CharSequence text) {
        toastText(text, Toast.LENGTH_LONG);
    }

    public void toastText(CharSequence text, int duration) {
        Activity currentActivity = App.instanceApp().getCurrentActivity();
        if (currentActivity != null && !currentActivity.isFinishing()) {
            Toast toast = Toast.makeText(App.instanceApp().getCurrentActivity(), text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void showWaitingDialog() {
        waitingDialog = new WaitingDialog(App.instanceApp().getCurrentActivity());
        waitingDialog.show();
    }

    public void hideWaitingDialog() {
        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.cancel();
        }
    }

}
