package org.goodev.discourse.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.goodev.discourse.R;

public class AddSiteFragment extends DialogFragment {
    private AddSiteListener mListener;

    private EditText mName;
    private EditText mUrl;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddSiteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddSiteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_site, null);
        mName = (EditText) view.findViewById(R.id.name);
        mUrl = (EditText) view.findViewById(R.id.url);
        builder.setView(view).setTitle(R.string.add_site_title).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String name = mName.getText().toString().trim();
                String url = mUrl.getText().toString().trim();
                if (mListener != null) {
                    mListener.add(name, url);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                AddSiteFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    public interface AddSiteListener {
        void add(String name, String url);
    }

}
