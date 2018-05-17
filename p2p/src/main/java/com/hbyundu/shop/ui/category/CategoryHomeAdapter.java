package com.hbyundu.shop.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.model.category.CategoryItemModel;
import com.hbyundu.shop.rest.model.category.CategoryMenuModel;
import com.hbyundu.shop.ui.recommend.RecommendAdapter;
import com.hbyundu.shop.vendor.widget.GridViewForScrollView;

import java.util.List;

/**
 * 右侧主界面ListView的适配器
 *
 * @author Administrator
 */
public class CategoryHomeAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;
    private List<CategoryMenuModel> titleArray;

    public CategoryHomeAdapter(Context context, List<CategoryMenuModel> titleArray) {
        this.titleArray = titleArray;
        this.context = context;

        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titleArray != null ? titleArray.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return titleArray != null ? titleArray.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.category_item_home, null);
            viewHold.gridView = (GridViewForScrollView) convertView.findViewById(R.id.category_item_home_gridView);
            viewHold.blank = (TextView) convertView.findViewById(R.id.category_item_home_blank_tv);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        CategoryMenuModel menuModel = titleArray.get(position);
        final List<CategoryItemModel> itemModels = menuModel.list;
        String name = menuModel.catename;

        CategoryHomeItemAdapter adapter = new CategoryHomeItemAdapter(context, itemModels, name);
        viewHold.gridView.setAdapter(adapter);

        viewHold.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, itemModels.get(i));
                }
            }
        });


        viewHold.blank.setText(menuModel.catename);

        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private static class ViewHold {
        private GridViewForScrollView gridView;
        private TextView blank;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, Object item);
    }
}
