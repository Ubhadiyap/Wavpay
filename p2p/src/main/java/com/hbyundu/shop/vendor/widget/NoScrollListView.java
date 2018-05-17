package com.hbyundu.shop.vendor.widget;

/**
 * Created by zhangyue on 2018-01-10.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by itgoyo on 2017/10/30.
 * 了解更多  https://github.com/itgoyo
 * 更新时间 2017/10/30
 * 更新描述  解决ScrolView与ListView滑动冲突
 */

public class NoScrollListView extends ListView {

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
