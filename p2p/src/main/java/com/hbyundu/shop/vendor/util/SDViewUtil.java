package com.hbyundu.shop.vendor.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.application.App;
import com.hbyundu.shop.vendor.adapter.SDBaseAdapter;

import java.util.List;

public class SDViewUtil {
    /**
     * 重置listview高度，解决和scrollview嵌套问题
     *
     * @param listView
     */
    public static void resetListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem != null) {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height += 5;// if without this statement,the listview will be a
        // little short
        listView.setLayoutParams(params);
    }

    // public static void resetGridViewHeightBasedOnChildren(GridView gridView)
    // {
    // ListAdapter listAdapter = gridView.getAdapter();
    // if (listAdapter == null)
    // {
    // return;
    // }
    // int numColums = gridView.getNumColumns();
    //
    // }

    public static void measureView(View v) {
        if (v == null) {
            return;
        }
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
    }

    public static void resetLinearLayoutHeight(LinearLayout linLayout, View flowView, Context context) {
        if (linLayout != null && flowView != null && context != null) {
            measureView(linLayout);
            measureView(flowView);
            int flowHeight = flowView.getMeasuredHeight();
            int linWidth = linLayout.getMeasuredWidth();
            int screenWidth = SDUIUtil.getScreenWidth(context);
            if (linWidth + 10 >= screenWidth) // 超过屏幕宽度
            {
                LayoutParams params = (LayoutParams) linLayout.getLayoutParams();
                float rate = (float) (linWidth + 10) / (float) screenWidth;
                int rateInt = (int) rate;
                params.height = flowHeight * (rateInt + 2);
                linLayout.setLayoutParams(params);
            }
        }
    }

    public static void toggleEmptyMsgByList(List<? extends Object> list, View emptyView) {
        if (emptyView != null) {
            if (list != null && list.size() > 0) {
                if (emptyView.getVisibility() != View.GONE) {
                    emptyView.setVisibility(View.GONE);
                }
            } else {
                if (emptyView.getVisibility() != View.VISIBLE) {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public static void toggleViewByList(List<? extends Object> list, View view) {
        if (view != null) {
            if (list != null && list.size() > 0) {
                if (view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            } else {
                if (view.getVisibility() != View.GONE) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    public static <T> void updateAdapterByList(List<T> listOriginalData, List<T> listNewData, SDBaseAdapter<T> mAdapter, boolean isLoadMore) {
        if (mAdapter != null) {
            if (listNewData != null && listNewData.size() > 0) {
                if (!isLoadMore) {
                    listOriginalData.clear();
                }
                listOriginalData.addAll(listNewData);
            } else {
                listOriginalData.clear();
                SDToast.showToast(App.getStringById(R.string.data_not_found));
            }
            mAdapter.updateListViewData(listOriginalData);
        }
    }

}
