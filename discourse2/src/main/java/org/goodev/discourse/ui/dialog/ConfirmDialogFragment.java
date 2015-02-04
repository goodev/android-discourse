package org.goodev.discourse.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import org.goodev.discourse.utils.Utils;

public class ConfirmDialogFragment extends DialogFragment {

    private String mTitle;
    private String mMsg;
    private ConfirmListener mListener;

    public static ConfirmDialogFragment newInstance(String title, String msg) {
        ConfirmDialogFragment f = new ConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_TITLE, title);
        args.putString(Utils.EXTRA_MSG, msg);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mTitle = args.getString(Utils.EXTRA_TITLE);
        mMsg = args.getString(Utils.EXTRA_MSG);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment f = getParentFragment();
        if (activity instanceof ConfirmListener) {
            mListener = (ConfirmListener) activity;
        } else if (f instanceof ConfirmListener) {
            mListener = (ConfirmListener) f;
        } else {
            throw new ClassCastException(activity.toString() + " must implement ConfirmListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMsg).setTitle(mTitle).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {
                    mListener.onConfirmClicked();
                }
            }
        }).setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    public interface ConfirmListener {
        void onConfirmClicked();
    }
}
