package org.goodev.discourse.photos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.goodev.discourse.utils.Utils;

public class CursorPagerAdapter<F extends Fragment> extends FragmentStatePagerAdapter {
    private final Class<F> fragmentClass;
    private final String[] projection;

    public CursorPagerAdapter(FragmentManager fm, Class<F> fragmentClass, String[] projection) {
        super(fm);
        this.fragmentClass = fragmentClass;
        this.projection = projection;
    }

    @Override
    public F getItem(int position) {
        if (projection == null) // shouldn't happen
            return null;

        F frag;
        try {
            frag = fragmentClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_URL, projection[position]);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public int getCount() {
        if (projection == null)
            return 0;
        else
            return projection.length;
    }

}
