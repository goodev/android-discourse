package org.goodev.discourse.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.goodev.discourse.R;
import org.goodev.discourse.contentprovider.Provider;
import org.goodev.discourse.utils.Utils;

import static org.goodev.discourse.database.tables.CategoriesTable.COLOR;
import static org.goodev.discourse.database.tables.CategoriesTable.DESCRIPTION;
import static org.goodev.discourse.database.tables.CategoriesTable.DESCRIPTION_EXCERPT;
import static org.goodev.discourse.database.tables.CategoriesTable.ID;
import static org.goodev.discourse.database.tables.CategoriesTable.NAME;
import static org.goodev.discourse.database.tables.CategoriesTable.SLUG;
import static org.goodev.discourse.database.tables.CategoriesTable.TEXT_COLOR;
import static org.goodev.discourse.database.tables.CategoriesTable.TOPIC_COUNT;
import static org.goodev.discourse.database.tables.CategoriesTable.UID;

public class EditorChangeTitleFragment extends DialogFragment implements LoaderCallbacks<Cursor> {
    private static final int LOADER_ID_CATEGORY = 0;
    private static final String[] CATEGORY_COLUMNS = new String[]{ID, UID, NAME, COLOR, TEXT_COLOR, SLUG, TOPIC_COUNT, DESCRIPTION, DESCRIPTION_EXCERPT};
    private static final int INDEX_UID = 1;
    private static final int INDEX_NAME = 2;
    private static final int INDEX_COLOR = 3;
    private static final int INDEX_TEXT_COLOR = 4;
    private static final int INDEX_SLUG = 5;
    private static final int INDEX_TOPIC_COUNT = 6;
    private ChangeTopicListener mListener;
    private EditText mTitleET;
    private int mCategoryIndex;
    private long mCategoryId;
    private String mTitle;
    private Spinner mCategoriesSpinner;
    private CursorAdapter mCategoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoryAdapter = new CategoryAdapter(getActivity(), null);
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(Utils.EXTRA_TITLE, null);
            mCategoryIndex = savedInstanceState.getInt(Utils.EXTRA_CAT_INDEX, 0);

        } else {
            Bundle args = getArguments();
            mTitle = args.getString(Utils.EXTRA_TITLE, null);
            mCategoryId = args.getLong(Utils.EXTRA_ID, 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String title = mTitleET.getText().toString();
        if (!TextUtils.isEmpty(title)) {
            outState.putString(Utils.EXTRA_TITLE, title);
        }
        int index = mCategoriesSpinner.getSelectedItemPosition();
        outState.putInt(Utils.EXTRA_CAT_INDEX, index);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment f = getParentFragment();
        if (activity instanceof ChangeTopicListener) {
            mListener = (ChangeTopicListener) activity;
        } else if (f instanceof ChangeTopicListener) {
            mListener = (ChangeTopicListener) f;
        } else {
            throw new ClassCastException(activity.toString() + " must implement ChangeTopicListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID_CATEGORY, null, this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editor_change_title, null);
        mTitleET = (EditText) view.findViewById(R.id.edit_title);
        mTitleET.setText(mTitle);
        mCategoriesSpinner = (Spinner) view.findViewById(R.id.edit_categories_spinner);
        mCategoriesSpinner.setAdapter(mCategoryAdapter);
        builder.setView(view).setTitle(R.string.edit_topic_title).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String name = mTitleET.getText().toString().trim();
                int position = mCategoriesSpinner.getSelectedItemPosition();
                String catName = "";
                long catId = 0;
                if (position != 0) {
                    Cursor c = (Cursor) mCategoriesSpinner.getSelectedItem();
                    if (c != null) {
                        catName = c.getString(INDEX_NAME);
                        catId = c.getLong(INDEX_UID);
                    }
                }
                if (mListener != null) {
                    mListener.onTopicChange(name, catName, catId);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.CATEGORIES_CONTENT_URI, CATEGORY_COLUMNS, null, null, UID + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCategoryAdapter.swapCursor(data);
        if (mCategoryId > 0) {
            data.moveToFirst();
            int index = 0;
            while (!data.isAfterLast()) {
                long id = data.getLong(INDEX_UID);
                if (id == mCategoryId) {
                    mCategoryIndex = index;
                    break;
                }
                data.moveToNext();
                index++;
            }
        }
        mCategoriesSpinner.setSelection(mCategoryIndex);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface ChangeTopicListener {
        void onTopicChange(String title, String category, long categoryId);
    }

    class CategoryAdapter extends CursorAdapter {
        LayoutInflater mLayoutInflater;
        Context mContext;

        public CategoryAdapter(Context context, Cursor c) {
            super(context, c, false);
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mLayoutInflater.inflate(R.layout.editor_category_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView category = (TextView) view.findViewById(R.id.category_name);
            String name = cursor.getString(INDEX_NAME);
            category.setText(name);
        }

        public void bindDropDownView(View view, Context context, Cursor cursor) {
            TextView category = (TextView) view.findViewById(R.id.category_name);
            TextView topicCount = (TextView) view.findViewById(R.id.category_topic_count);
            String name = cursor.getString(INDEX_NAME);
            String bgColor = cursor.getString(INDEX_COLOR);
            String textColor = cursor.getString(INDEX_TEXT_COLOR);
            Utils.setCategoryView(category, name, bgColor, textColor);
            long count = cursor.getLong(INDEX_TOPIC_COUNT);
            topicCount.setText(mContext.getString(R.string.editor_category_count, count));
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (mDataValid) {
                mCursor.moveToPosition(position);
                View v;
                if (convertView == null) {
                    v = newDropDownView(mContext, mCursor, parent);
                } else {
                    v = convertView;
                }
                bindDropDownView(v, mContext, mCursor);
                return v;
            } else {
                return null;
            }
        }

        @Override
        public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
            return mLayoutInflater.inflate(R.layout.editor_category_dropdown_item, parent, false);
        }

    }
}
