package com.hbyundu.shop.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.base.Config;
import com.hbyundu.shop.rest.model.category.CategoryItemModel;

import java.util.List;

/**
 * author：wangzihang
 * date： 2017/8/8 19:15
 * desctiption：
 * e-mail：wangzihang@xiaohongchun.com
 */

public class CategoryHomeItemAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryItemModel> itemModels;

    private LayoutInflater inflater;
    private String name;

    public CategoryHomeItemAdapter(Context context, List<CategoryItemModel> itemModels, String name) {
        this.context = context;
        this.itemModels = itemModels;
        this.name = name;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (itemModels != null) {
            return itemModels.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return itemModels.size();
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
            convertView = inflater.inflate(R.layout.category_item_homeitem, null);
            viewHold = new ViewHold();
            viewHold.tv_name = (TextView) convertView.findViewById(R.id.category_item_homeitem_name_tv);
            viewHold.iv_icon = (ImageView) convertView.findViewById(R.id.category_item_homeitem_imageView);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        CategoryItemModel itemModel = itemModels.get(position);
        viewHold.tv_name.setText(itemModel.name);

        RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_goods_default_image).dontAnimate();
        Glide.with(context).load(Config.SERVER + itemModel.img).apply(options).into(viewHold.iv_icon);

        return convertView;
    }

    private static class ViewHold {
        private TextView tv_name;
        private ImageView iv_icon;
    }

}
