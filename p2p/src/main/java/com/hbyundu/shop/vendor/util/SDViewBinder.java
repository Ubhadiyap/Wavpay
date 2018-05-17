package com.hbyundu.shop.vendor.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hbyundu.shop.application.App;

public class SDViewBinder
{

	public static void setTextView(TextView textView, String content, String emptyTip)
	{
		if (!TextUtils.isEmpty(content))
		{
			textView.setText(content);
		} else
		{
			textView.setText(emptyTip);
		}
	}

	public static void setTextView(TextView textView, String content)
	{
		setTextView(textView, content, "");
	}

	public static void setTextViewsVisibility(TextView textView, String content)
	{
		if (TextUtils.isEmpty(content))
		{
			textView.setVisibility(View.GONE);
		} else
		{
			textView.setText(content);
		}
	}

	public static void setTextViewColorByColorId(TextView textView, int colorId)
	{
		textView.setTextColor(App.getApplication().getResources().getColor(colorId));
	}

	public static boolean setViewsVisibility(View view, boolean visible)
	{
		int state = view.getVisibility();
		if (visible)
		{
			if (state != View.VISIBLE)
			{
				view.setVisibility(View.VISIBLE);
			}
			return true;
		} else
		{
			if (state != View.GONE)
			{
				view.setVisibility(View.GONE);
			}
			return false;
		}
	}

	public static boolean setViewsVisibility(View view, int visible)
	{
		if (visible == 0)
		{
			return setViewsVisibility(view, false);
		} else if (visible == 1)
		{
			return setViewsVisibility(view, true);
		}
		return false;
	}

//	public static void setImageView(ImageView imageView, String url)
//	{
//		try
//		{
//			ImageLoaderManager.getImageLoader().displayImage(url, imageView);
//		} catch (Exception e)
//		{
//			imageView.setImageResource(R.drawable.ic_frag_main_head_empty);
//		}
//	}

//	public static void setImageFillScreenWidthByScale(final ImageView targetView, final View wrapView, String url)
//	{
//		try
//		{
//			ImageLoaderManager.getImageLoader().displayImage(url, targetView, new ImageLoadingListener()
//			{
//
//				@Override
//				public void onLoadingStarted(String imageUri, View view)
//				{
//				}
//
//				@Override
//				public void onLoadingFailed(String imageUri, View view, FailReason failReason)
//				{
//				}
//
//				@Override
//				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
//				{
//					if (loadedImage != null && targetView != null && wrapView != null)
//					{
//						ViewGroup.LayoutParams params = wrapView.getLayoutParams();
//						if (params != null)
//						{
//							double scale = (double) (loadedImage.getHeight()) / (double) (loadedImage.getWidth());
//							double height = scale * (double) (App.getApplication().getResources().getDisplayMetrics().widthPixels);
//							params.height = (int) height;
//							wrapView.setLayoutParams(params);
//						}
//					}
//				}
//
//				@Override
//				public void onLoadingCancelled(String imageUri, View view)
//				{
//				}
//			});
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	public static void setBackgroundImageFromUrl(final View targetView, String url)
//	{
//		try
//		{
//			ImageLoaderManager.getImageLoader().loadImage(url, new ImageLoadingListener()
//			{
//				@Override
//				public void onLoadingStarted(String imageUri, View view)
//				{
//				}
//
//				@Override
//				public void onLoadingFailed(String imageUri, View view, FailReason failReason)
//				{
//					view.setBackgroundResource(R.drawable.ic_frag_main_head_empty);
//				}
//
//				@Override
//				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
//				{
//					targetView.setBackgroundDrawable(SDImageUtil.Bitmap2Drawable(loadedImage));
//				}
//
//				@Override
//				public void onLoadingCancelled(String imageUri, View view)
//				{
//				}
//			});
//		} catch (Exception e)
//		{
//			// TODO: handle exception
//		}
//	}

	public static void setRatingBar(RatingBar ratingBar, float rating)
	{
		ratingBar.setRating(rating);
	}

}
