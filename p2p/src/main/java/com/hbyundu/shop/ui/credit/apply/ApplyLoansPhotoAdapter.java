package com.hbyundu.shop.ui.credit.apply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbyundu.shop.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/6/15.
 */
public class ApplyLoansPhotoAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;

    private List<String> mPhotos;

    private int mMaxPhotos;

    private MediaUploadListener mListener;

    public ApplyLoansPhotoAdapter(Context context, List<String> photos, int maxPhotos, MediaUploadListener listener) {
        this.mContext = context;
        this.mPhotos = photos;
        this.mMaxPhotos = maxPhotos;
        this.mListener = listener;
    }

    public List<File> getPhotoFiles() {
        List<File> photos = new ArrayList<>();
        if (mPhotos != null) {
            for (String photo : mPhotos) {
                photos.add(new File(photo));
            }
        }
        return photos;
    }

    @Override
    public int getCount() {
        return getMediaCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_apply_loans_photo, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.uploadImageView = (ImageView) convertView.findViewById(R.id.item_apply_loans_photo_iv);
            viewHolder.deleteImageButton = (ImageButton) convertView.findViewById(R.id.item_apply_loans_photo_delete_ib);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mPhotos != null && mPhotos.size() > position) {
            String photo = mPhotos.get(position);
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_photo_add_bg).dontAnimate();
            Glide.with(mContext).load("file://" + photo).apply(options).into(viewHolder.uploadImageView);
            viewHolder.deleteImageButton.setOnClickListener(this);
            viewHolder.deleteImageButton.setVisibility(View.VISIBLE);
            viewHolder.deleteImageButton.setTag(position);
        } else {
            viewHolder.uploadImageView.setImageDrawable(null);
            viewHolder.uploadImageView.setTag(null);
            viewHolder.deleteImageButton.setVisibility(View.GONE);
        }

        return convertView;
    }

    private int getMediaCount() {
        return Math.min(mMaxPhotos, (mPhotos != null ? mPhotos.size() : 0) + 1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_apply_loans_photo_delete_ib) {
            if (v.getTag() != null && mListener != null) {
                int position = Integer.valueOf(String.valueOf(v.getTag()));
                mListener.onDeleteClick(position);
            }
        }
    }

    class ViewHolder {
        ImageView uploadImageView;
        ImageButton deleteImageButton;
    }

    public interface MediaUploadListener {
        void onDeleteClick(int position);
    }
}
