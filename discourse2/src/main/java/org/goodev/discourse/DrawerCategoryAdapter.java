package org.goodev.discourse;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.goodev.discourse.model.Categories;
import org.goodev.discourse.utils.Utils;

public class DrawerCategoryAdapter extends CursorAdapter {

    public DrawerCategoryAdapter(Context context) {
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.drawer_category_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Categories c = new Categories(cursor);
        view.setTag(c);
        View color = view.findViewById(R.id.category_color);
        String colorString = c.getColor();
        int colorValue = Utils.parseColor(colorString);
        color.setBackgroundColor(colorValue);
        TextView name = (TextView) view.findViewById(R.id.category_name);
        name.setText(c.getName());
    }

}
