package org.goodev.discourse.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.R;

public class AddUserFragment extends DialogFragment implements OnClickListener {
    private AddUserListener mListener;

    private EditText mName;
    private EditText mPassword;
    private CheckBox mCheckBox;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment parent = getParentFragment();
        if (parent instanceof AddUserListener) {
            mListener = (AddUserListener) parent;
        } else {
            try {
                mListener = (AddUserListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement AddUserListener");
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_user, null);
        mName = (EditText) view.findViewById(R.id.name);
        mPassword = (EditText) view.findViewById(R.id.password);
        mCheckBox = (CheckBox) view.findViewById(R.id.remember_password);
        view.findViewById(R.id.social_login).setOnClickListener(this);
        builder.setView(view).setTitle(R.string.add_user_title).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String name = mName.getText().toString().trim();
                String url = mPassword.getText().toString().trim();

                if (mListener != null) {
                    mListener.add(name, url, mCheckBox.isChecked());
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                AddUserFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.social_login:
                ActivityUtils.openSocialHelpActivity(getActivity());
                break;

        }
    }

    public interface AddUserListener {
        void add(String name, String password, boolean remember);
    }

}
