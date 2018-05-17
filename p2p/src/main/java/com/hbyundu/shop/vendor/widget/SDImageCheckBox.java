package com.hbyundu.shop.vendor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.hbyundu.shop.R;

public class SDImageCheckBox extends ImageView
{

	private int mIdImageNormal = R.mipmap.ic_my_interest_bid_normal;
	private int mIdImageSelect = R.mipmap.ic_my_interest_bid_select;

	private boolean mIsSelect = false;
	private SDCheckBoxListener mListener = null;

	public boolean ismIsSelect()
	{
		return mIsSelect;
	}

	public int getmIdImageNormal()
	{
		return mIdImageNormal;
	}

	public void setmIdImageNormal(int mIdImageNormal)
	{
		this.mIdImageNormal = mIdImageNormal;
	}

	public int getmIdImageSelect()
	{
		return mIdImageSelect;
	}

	public void setmIdImageSelect(int mIdImageSelect)
	{
		this.mIdImageSelect = mIdImageSelect;
	}

	public SDCheckBoxListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SDCheckBoxListener mListener)
	{
		this.mListener = mListener;
	}

	public SDImageCheckBox(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
	}

	private void initView()
	{
		this.setScaleType(ScaleType.CENTER_INSIDE);
		setCheckState(mIsSelect);
		this.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mIsSelect = !mIsSelect;
				setCheckState(mIsSelect);
				if (mListener != null)
				{
					mListener.onChecked(mIsSelect);
				}
			}
		});
	}

	public void setCheckState(boolean isChecked)
	{
		if (isChecked)
		{
			if (mIdImageSelect != 0)
			{
				this.setImageResource(mIdImageSelect);
			}
			mIsSelect = true;
		} else
		{
			if (mIdImageNormal != 0)
			{
				this.setImageResource(mIdImageNormal);
			}
			mIsSelect = false;
		}
	}

	public interface SDCheckBoxListener
	{
		public void onChecked(boolean isChecked);
	}

}
