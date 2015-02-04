package org.goodev.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.goodev.discourse.R;
import org.goodev.discourse.utils.Utils;

public class AddLinkFragment extends DialogFragment {
    public static final int MD_URL = 1;
    public static final int MD_IMG = 2;
    private OnAddLinkListener mListener;

    private EditText mName;
    private EditText mUrl;
    /**
     * Url 或者 图片
     */
    private int mType;

    /**
     * 默认类型为 URL
     *
     * @param des
     * @return
     */
    public static AddLinkFragment newInstance(CharSequence des) {
        return newInstance(des, MD_URL);
    }

    public static AddLinkFragment newInstance(CharSequence des, int type) {
        AddLinkFragment r = new AddLinkFragment();
        Bundle args = new Bundle();
        args.putInt(Utils.EXTRA_TYPE, type);
        if (des != null) {
            args.putString(Utils.EXTRA_TITLE, des.toString());
        }
        r.setArguments(args);
        return r;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(Utils.EXTRA_TYPE, MD_URL);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAddLinkListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnAddLinkListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_link, null);
        Bundle arg = getArguments();
        String des = null;
        if (arg != null) {
            des = arg.getString(Utils.EXTRA_TITLE, null);
        }
        mName = (EditText) view.findViewById(R.id.name);
        mUrl = (EditText) view.findViewById(R.id.url);
        if (!TextUtils.isEmpty(des)) {
            mName.setText(des);
            mUrl.requestFocus();
        }
        int title = R.string.add_link_title;
        if (mType == MD_IMG) {
            title = R.string.add_img_title;
            mName.setHint(R.string.add_img_alt_text_hint);
            mUrl.setHint(R.string.add_img_hint);
        }
        builder.setView(view).setTitle(title).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String name = mName.getText().toString().trim();
                String url = mUrl.getText().toString().trim();
                if (mListener != null) {
                    mListener.add(mType, name, url);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                AddLinkFragment.this.getDialog().cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }

    public interface OnAddLinkListener {
        void add(int type, String name, String url);
    }

}
