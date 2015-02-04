package org.goodev.discourse.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.goodev.discourse.R;

import java.util.Arrays;
import java.util.Comparator;

public class SimpleSectionedListAdapter extends BaseAdapter {
    // private final int mSectionResourceId;
    private final LayoutInflater mLayoutInflater;
    private final ListAdapter mBaseAdapter;
    private final SparseArray<Section> mSections = new SparseArray<Section>();
    private final OnClickListener mAddUserListener;
    private boolean mValid = true;

    public SimpleSectionedListAdapter(Context ctx, ListAdapter baseAdapter, OnClickListener listener) {
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // mSectionResourceId = sectionResId;
        mAddUserListener = listener;
        mBaseAdapter = baseAdapter;
        mBaseAdapter.registerDataSetObserver(new DataSetObserver() {

            @Override
            public void onChanged() {
                mValid = true;
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                mValid = false;
                notifyDataSetInvalidated();
            }

        });
    }

    public void setSections(Section[] sections) {
        mSections.clear();
        Arrays.sort(sections, new Comparator<Section>() {

            @Override
            public int compare(Section lhs, Section rhs) {
                return (lhs.firstPosition == rhs.firstPosition) ? 0 : ((lhs.firstPosition < rhs.firstPosition) ? -1 : 1);
            }
        });

        int offset = 0;// offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            mSections.append(section.sectionedPosition, section);
            ++offset;
        }

        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).firstPosition > position) {
                break;
            }
            ++offset;
        }

        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return ListView.INVALID_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }

    @Override
    public int getCount() {
        return (mValid ? mBaseAdapter.getCount() + mSections.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return isSectionHeaderPosition(position) ? mSections.get(position) : mBaseAdapter.getItem(sectionedPositionToPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position) ? Integer.MAX_VALUE - mSections.indexOfKey(position) : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position) ? getViewTypeCount() - 1 : mBaseAdapter.getItemViewType(position);
    }

    // @Override
    // public boolean isEnabled(int position) {
    // return isSectionHeaderPosition(position)
    // ? false
    // : mBaseAdapter.isEnabled(sectionedPositionToPosition(position));
    // }

    @Override
    public int getViewTypeCount() {
        return mBaseAdapter.getViewTypeCount() + 1;// the section headings
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return mBaseAdapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isSectionHeaderPosition(position)) {
            View view = convertView;
            if (view == null) {
                view = mLayoutInflater.inflate(R.layout.site_list_item, parent, false);
            }

            TextView siteName = (TextView) view.findViewById(R.id.site_name);
            TextView siteUrl = (TextView) view.findViewById(R.id.site_url);
            View addUser = view.findViewById(R.id.site_add_user);
            View delete = view.findViewById(R.id.delete_site);

            siteName.setText(mSections.get(position).title);
            siteUrl.setText(mSections.get(position).url);
            addUser.setTag(mSections.get(position).url);
            addUser.setOnClickListener(mAddUserListener);
            delete.setTag(mSections.get(position).url);
            delete.setOnClickListener(mAddUserListener);
            return view;
        } else {
            return mBaseAdapter.getView(sectionedPositionToPosition(position), convertView, parent);
        }
    }

    /**
     * discourse site
     */
    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;
        CharSequence url;
        Long id;

        public Section(int firstPosition, CharSequence title, CharSequence url, long id) {
            this.firstPosition = firstPosition;
            this.title = title;
            this.url = url;
            this.id = id;
        }
    }
}
