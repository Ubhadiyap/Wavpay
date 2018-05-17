package com.hbyundu.shop.vendor.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class SDBaseAdapter<T> extends BaseAdapter
{

	protected List<T> mListModel = new ArrayList<T>();
	protected LayoutInflater mInflater = null;
	protected Activity mActivity = null;

	public SDBaseAdapter(List<T> listModel, Activity activity)
	{
		if (listModel != null)
		{
			this.mListModel = listModel;
		}
		this.mActivity = activity;
		this.mInflater = mActivity.getLayoutInflater();
	}

	public void setData(List<T> listModel)
	{
		if (listModel != null)
		{
			this.mListModel = listModel;
		} else
		{
			this.mListModel = new ArrayList<T>();
		}
	}

	public List<T> getData()
	{
		return mListModel;
	}

	public void updateListViewData(List<T> listModel)
	{
		setData(listModel);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (mListModel != null)
		{
			return mListModel.size();
		} else
		{
			return 0;
		}
	}

	@Override
	public T getItem(int position)
	{
		if (mListModel != null && mListModel.size() > 0 && mListModel.size() > position)
		{
			return mListModel.get(position);
		} else
		{
			return null;
		}
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getItemView(position, convertView, parent, getItem(position));
	}

	public abstract View getItemView(int position, View convertView, ViewGroup parent, final T model);

}
