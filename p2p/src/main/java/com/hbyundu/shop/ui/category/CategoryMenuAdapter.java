package com.hbyundu.shop.ui.category;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.model.category.CategoryMenuModel;

import java.util.List;

/**
 * 左侧菜单ListView的适配器
 *
 * @author Administrator
 */
public class CategoryMenuAdapter extends BaseAdapter {

    private Context context;
    private int selectItem = 0;
    private List<CategoryMenuModel> list;
    private LayoutInflater inflater;

    public CategoryMenuAdapter(Context context, List<CategoryMenuModel> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = inflater.inflate(R.layout.category_item_menu, null);
            holder.tv_name = (TextView) arg1.findViewById(R.id.category_item_menu_name_tv);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        if (arg0 == selectItem) {
            holder.tv_name.setBackgroundColor(Color.WHITE);
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.colorTitleBar));
        } else {
            holder.tv_name.setBackgroundColor(context.getResources().getColor(R.color.background));
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.black));
        }

        CategoryMenuModel item = list.get(arg0);
        holder.tv_name.setText(item.catename);

        return arg1;
    }

    static class ViewHolder {
        private TextView tv_name;
    }
}
