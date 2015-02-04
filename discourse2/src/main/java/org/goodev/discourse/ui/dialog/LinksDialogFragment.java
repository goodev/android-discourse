package org.goodev.discourse.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.data.Links;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;

public class LinksDialogFragment extends DialogFragment implements OnItemClickListener {

    private Links[] mLinks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            savedInstanceState = getArguments();
        }
        mLinks = (Links[]) savedInstanceState.getSerializable(Utils.EXTRA_LINKS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Utils.EXTRA_LINKS, mLinks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_links, container, false);
        getDialog().setTitle(R.string.post_links_title);
        ListView lv = (ListView) view.findViewById(R.id.links_list_view);
        lv.setAdapter(new LinksAdapter(inflater));
        lv.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Links l = (Links) parent.getItemAtPosition(position);
        L.d("links %s \n URL: %s", l.title, l.url);
        String url = l.url;
        if (url.startsWith(Utils.SLASH) || url.startsWith(App.getSiteUrl())) {
            ActivityUtils.openDiscourseLinks(getActivity(), url);
        } else {
            ActivityUtils.openUrl(getActivity(), url);
        }

        dismissAllowingStateLoss();
    }

    class LinksAdapter extends BaseAdapter {
        LayoutInflater mInflater;

        public LinksAdapter(LayoutInflater inflater) {
            mInflater = inflater;
        }

        @Override
        public int getCount() {
            if (mLinks == null) {
                return 0;
            }
            return mLinks.length;
        }

        @Override
        public Object getItem(int position) {
            if (mLinks == null) {
                return null;
            }
            return mLinks[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Links l = (Links) getItem(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.dialog_links_item, parent, false);
            }
            TextView title = (TextView) convertView.findViewById(R.id.links_title);
            TextView clicks = (TextView) convertView.findViewById(R.id.links_click_number);
            title.setText(l.getTitle());
            clicks.setText(String.valueOf(l.clicks));
            if (l.url.startsWith(App.getSiteUrl())) {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_nav, 0);
            } else {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            return convertView;
        }

    }

}
