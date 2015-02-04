package org.goodev.discourse.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.goodev.discourse.R;
import org.goodev.discourse.utils.Utils;

public class InviteReplyDialogFragment extends DialogFragment {

    private OnInviteListener mListener;
    private EditText mEmail;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment parent = getParentFragment();
        if (parent instanceof OnInviteListener) {
            mListener = (OnInviteListener) parent;
        } else {
            try {
                mListener = (OnInviteListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnInviteListener");
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_invite_reply, null);
        mEmail = (EditText) view.findViewById(R.id.email);
        builder.setView(view).setTitle(R.string.action_invite_reply).setPositiveButton(R.string.invite, null).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        final AlertDialog d = builder.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String email = mEmail.getText().toString().trim();
                        if (Utils.isValidEmail(email)) {
                            mEmail.setError(null);
                            if (mListener != null) {
                                mListener.onInvite(email);
                            }
                            d.dismiss();
                        } else {
                            mEmail.setError(getString(R.string.invalid_email));
                            //                    		Toast.makeText(getActivity(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return d;

    }

    public interface OnInviteListener {
        void onInvite(String email);
    }

}
