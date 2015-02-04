package org.goodev.discourse.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.goodev.discourse.R;
import org.goodev.discourse.utils.Utils;

import in.uncod.android.bypass.Bypass;

public class PreviewDialogFragment extends DialogFragment {

    public static PreviewDialogFragment newInstance(String msg) {
        PreviewDialogFragment f = new PreviewDialogFragment();
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_MSG, msg);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString(Utils.EXTRA_MSG);

        Bypass bypass = new Bypass();

        CharSequence string = bypass.markdownToSpannable(message);

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.action_preview).setMessage(string).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).create();

    }
}
