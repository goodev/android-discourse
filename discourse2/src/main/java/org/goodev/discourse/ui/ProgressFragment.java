package org.goodev.discourse.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.goodev.discourse.R;
import org.goodev.discourse.utils.Utils;

public class ProgressFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        String msg = null;
        if (getArguments() != null) {
            msg = getArguments().getString(Utils.EXTRA_MSG);
        }
        if (msg == null) {
            msg = getString(R.string.check_url_dialog_msg);
        }
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
