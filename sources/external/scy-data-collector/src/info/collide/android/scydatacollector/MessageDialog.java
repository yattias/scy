package info.collide.android.scydatacollector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class MessageDialog {

    private Activity _application;

    public MessageDialog(Activity application) {
        _application = application;
    }

    public void createYesNoDialog(String msgText, OnClickListener oclYes, String strYes, OnClickListener oclNo, String strNo) {
        // "Answer" callback.
        Builder alMsgBox = new AlertDialog.Builder(_application).setMessage(msgText).setPositiveButton(strYes, oclYes);
        if (oclNo != null)
            alMsgBox.setNegativeButton(strNo, oclNo);
        else
            alMsgBox.setNegativeButton(strNo, new OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                }
            });
        alMsgBox.show();
    }

    public void createInfoDialog(String msgText, OnClickListener ocl) {
        // "Answer" callback.
        Builder alMsgBox = new AlertDialog.Builder(_application).setMessage(msgText).setNeutralButton("OK", ocl);
        alMsgBox.show();
    }
}
