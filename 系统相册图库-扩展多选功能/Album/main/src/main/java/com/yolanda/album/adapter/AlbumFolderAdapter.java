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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yolanda.album.R;
import com.yolanda.album.entity.AlbumFolder;
import com.yolanda.album.util.DisplayUtil;
import com.yolanda.nohttp.tools.ImageLocalLoader;

import java.util.List;

/**
 * Created in 2016/5/8 21:09.
 *
 * @author Yolanda;
 */
public class AlbumFolderAdapter extends RecyclerView.Adapter<AlbumFolderAdapter.AlbumFolderViewHolder> {

    private List<AlbumFolder> albumFolders;

    private OnItemClickListener onItemClickListener;

    public AlbumFolderAdapter() {
    }

    public void notifyDataSetChanged(List<AlbumFolder> albumFolders) {
        this.albumFolders = albumFolders;
        super.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public AlbumFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_folder, parent, false);
        return new AlbumFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumFolderViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return albumFolders == null ? 0 : albumFolders.size();
    }

    public AlbumFolder albumFolder(int position) {
        return albumFolders.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class AlbumFolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * 预览图片
         */
        private ImageView mIvImage;
        /**
         * 预览图片所在的文件夹名称
         */
        private TextView mTvName;

        public AlbumFolderViewHolder(View itemView) {
            super(itemView);
            int size = DisplayUtil.screenWidth / 2;
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);
            itemView.setLayoutParams(layoutParams);
            itemView.setOnClickListener(this);
            mIvImage = (ImageView) itemView.findViewById(R.id.iv_album_folder_preview);
            mTvName = (TextView) itemView.findViewById(R.id.tv_album_folder_name);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView && onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }

        /**
         * 设置数据
         */
        public void setData() {
            AlbumFolder albumFolder = albumFolder(getAdapterPosition());
            ImageLocalLoader.getInstance().loadImage(mIvImage, albumFolder.getFirstPhoto().getPath());
            mTvName.setText(albumFolder.getName());
        }
    }

}