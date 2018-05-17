package com.hbyundu.shop.vendor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbyundu.shop.R;

/**
 * Title:
 * 
 * @author: yhz CreateTime：2014-6-11 上午9:08:35
 */
public class SDSimpleProjectDetailItemView extends LinearLayout
{
	public TextView inverstdetail_item_tv_left;
	public TextView inverstdetail_item_tv_right;

	public SDSimpleProjectDetailItemView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public SDSimpleProjectDetailItemView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();

	}

	private void init()
	{
		View view = LayoutInflater.from(getContext()).inflate(R.layout.view_simple_projectdetail_item, this, true);
		inverstdetail_item_tv_left = (TextView) view.findViewById(R.id.inverstdetail_item_tv_left);
		inverstdetail_item_tv_right = (TextView) view.findViewById(R.id.inverstdetail_item_tv_right);

	}

	public void setTV_Left(String text)
	{
		if (null != text)
		{
			this.inverstdetail_item_tv_left.setText(text);
		}
	}

	public void setTV_Right(String text)
	{
		if (null != text)
		{
			this.inverstdetail_item_tv_right.setText(text);
		}
	}
}
