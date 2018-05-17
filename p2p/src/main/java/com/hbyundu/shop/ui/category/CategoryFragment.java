package com.hbyundu.shop.ui.category;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.category.CategoryAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.category.CategoryItemModel;
import com.hbyundu.shop.rest.model.category.CategoryMenuModel;
import com.hbyundu.shop.rest.model.category.CategoryModel;
import com.hbyundu.shop.ui.goods.list.GoodsListActivity;
import com.hbyundu.shop.ui.goods.search.GoodsSearchActivity;
import com.hbyundu.shop.ui.recommend.RecommendAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {

    private List<Integer> showTitle;
    ;

    private ListView lv_menu;
    private ListView lv_home;

    private CategoryMenuAdapter menuAdapter;
    private CategoryHomeAdapter homeAdapter;
    private List<CategoryMenuModel> menuModels = new ArrayList<>();
    public List<CategoryItemModel> itemModels = new ArrayList<>();
    private int currentItem;

    private TextView tv_title;

    private View contentView;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_category, container, false);
        initView();
        initAPI();

        return contentView;
    }

    private void initAPI() {
        CategoryAPI.getInstance().category(new SubscriberOnListener<CategoryModel>() {
            @Override
            public void onSucceed(CategoryModel data) {
                menuModels.clear();
                menuModels.addAll(data.accessarray);
                for (CategoryMenuModel model : menuModels) {
                    if (model.list.size() == 0) {
                        CategoryItemModel itemModel = new CategoryItemModel(model.cateid, model.catename, model.cateimg);
                        model.list.add(itemModel);

                    }

                }
                showTitle = new ArrayList<>();
                for (int i = 0; i < menuModels.size(); i++) {
                    showTitle.add(i);
                }
                menuAdapter.notifyDataSetChanged();
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void initView() {
        lv_menu = (ListView) contentView.findViewById(R.id.fragment_category_main_menu_lv);
        tv_title = (TextView) contentView.findViewById(R.id.fragment_category_main_title_tv);
        lv_home = (ListView) contentView.findViewById(R.id.fragment_category_main_home_lv);
        menuAdapter = new CategoryMenuAdapter(getActivity(), menuModels);
        lv_menu.setAdapter(menuAdapter);

        homeAdapter = new CategoryHomeAdapter(getActivity(), menuModels);
        lv_home.setAdapter(homeAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuAdapter.setSelectItem(position);
                menuAdapter.notifyDataSetInvalidated();
                lv_home.setSelection(showTitle.get(position));
            }
        });

        lv_home.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                int current = showTitle.indexOf(firstVisibleItem);
                if (currentItem != current && current >= 0) {
                    currentItem = current;
                    menuAdapter.setSelectItem(currentItem);
                    menuAdapter.notifyDataSetInvalidated();
                }
            }
        });

        homeAdapter.setOnItemClickListener(new CategoryHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item) {
                CategoryItemModel category = (CategoryItemModel) item;

                Intent intent = new Intent(getActivity(), GoodsListActivity.class);
                intent.putExtra("categoryName", category.name);
                intent.putExtra("categoryId", category.id);
                startActivity(intent);
            }
        });

        contentView.findViewById(R.id.fragment_category_search_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_category_search_tv) {
            searchAction();
        }
    }

    private void searchAction() {
        Intent intent = new Intent(getActivity(), GoodsSearchActivity.class);
        startActivity(intent);
    }
}
