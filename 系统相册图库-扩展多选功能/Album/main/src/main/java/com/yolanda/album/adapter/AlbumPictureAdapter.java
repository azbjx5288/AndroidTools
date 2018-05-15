/*
 * Copyright © Yolanda. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yolanda.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yolanda.album.R;
import com.yolanda.album.entity.AlbumPicture;
import com.yolanda.album.util.DisplayUtil;
import com.yolanda.album.util.Toast;
import com.yolanda.nohttp.tools.ImageLocalLoader;

import java.util.List;

/**
 * Created in 2016/5/8 22:10.
 *
 * @author Yolanda;
 */
public class AlbumPictureAdapter extends RecyclerView.Adapter<AlbumPictureAdapter.AlbumPictureViewHolder> {

    private Context mContext;
    /**
     * 本次预览的图片
     */
    private List<AlbumPicture> albumPictures;
    /**
     * 选中监听
     */
    private OnPictureSelectedChanged onPictureSelectedChaged;
    /**
     * 允许用户选择几张
     */
    private int count;
    /**
     * 已经选择了几张
     */
    private int hasCount;

    public AlbumPictureAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 刷新数据
     *
     * @param albumPictures
     */
    public void notifyDataSetChanged(List<AlbumPicture> albumPictures) {
        this.albumPictures = albumPictures;
        super.notifyDataSetChanged();
    }

    public void setOnPictureSelectedChaged(OnPictureSelectedChanged onPictureSelectedChaged) {
        this.onPictureSelectedChaged = onPictureSelectedChaged;
    }

    /**
     * 设置允许用户选择几张
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public AlbumPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_picture, parent, false);
        return new AlbumPictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumPictureViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return albumPictures == null ? 0 : albumPictures.size();
    }

    public AlbumPicture albumFolder(int position) {
        return albumPictures.get(position);
    }

    public interface OnPictureSelectedChanged {
        void onSelectedChanged(int position, boolean selected);
    }

    class AlbumPictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * 显示的图片
         */
        private ImageView mIvImage, mIvCheck;
        /**
         * 图片名称
         */
        private TextView mTvName;

        public AlbumPictureViewHolder(View itemView) {
            super(itemView);
            int size = DisplayUtil.screenWidth / 2;
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);
            itemView.setLayoutParams(layoutParams);
            itemView.setOnClickListener(this);

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_album_picture_preview);
            mTvName = (TextView) itemView.findViewById(R.id.tv_album_picture_name);
            mIvCheck = (ImageView) itemView.findViewById(R.id.iv_album_picture_check);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                boolean isCheck = mIvCheck.isSelected();
                if (isCheck) {
                    mIvCheck.setSelected(false);
                    hasCount--;
                    if (onPictureSelectedChaged != null) {
                        onPictureSelectedChaged.onSelectedChanged(getAdapterPosition(), false);
                    }
                } else {
                    if (hasCount >= count) {
                        Toast.show(mContext, "在本文件夹最多只能选择" + count + "张");
                    } else {
                        mIvCheck.setSelected(true);
                        hasCount++;
                        if (onPictureSelectedChaged != null) {
                            onPictureSelectedChaged.onSelectedChanged(getAdapterPosition(), true);
                        }
                    }
                }

                // 为了用户体验好，给图片加一个【滤镜】
                // 16进制表示法ARGB(alpha, red, green, blue)，77代表的是A...
                mIvImage.setColorFilter(mIvCheck.isSelected() ? 0X77000000 : 0X00000000);
                albumPictures.get(getAdapterPosition()).setChecked(mIvCheck.isSelected());
            }
        }

        /**
         * 设置数据
         */
        public void setData() {
            AlbumPicture albumPicture = albumFolder(getAdapterPosition());
            ImageLocalLoader.getInstance().loadImage(mIvImage, albumPicture.getPath());
            mTvName.setText(albumPicture.getName());
        }
    }

}
