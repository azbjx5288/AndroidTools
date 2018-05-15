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
package com.yolanda.album.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.yolanda.album.R;
import com.yolanda.album.adapter.AlbumPictureAdapter;
import com.yolanda.album.entity.AlbumFolder;
import com.yolanda.album.entity.AlbumPicture;
import com.yolanda.album.view.GridViewDecorration;

import java.util.ArrayList;

/**
 * <p>显示文件夹中的图片，选定。</p>
 * Created in 2016/5/8 18:42.
 *
 * @author Yolanda;
 */
public class PictureActivity extends BasicActivity {

    /**
     * 输入参数：Parcelable, 文件夹AlbumFolder
     */
    public static final String KEY_INPUT_ALBUM_FOLDER = "KEY_INPUT_ALBUM_FOLDER";
    /**
     * 输出参数：{@code List<AlbumPicture>}
     */
    public static final String KEY_OUTPUT_PICTURES = "KEY_OUTPUT_PICTURES";

    /**
     * 文件夹适配器
     */
    private AlbumPictureAdapter albumPictureAdapter;
    /**
     * 文件夹
     */
    private AlbumFolder albumFolder;
    /**
     * 选中的张数
     */
    private int hasCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        setBackBarEnable(true);

        Intent intent = getIntent();
        albumFolder = intent.getParcelableExtra(KEY_INPUT_ALBUM_FOLDER);
        int count = intent.getIntExtra(AlbumActivity.KEY_INPUT_COUNT, 1);
        if (albumFolder != null) {
            init(count);
        } else {
            finish();
        }
    }

    /**
     * 更新图片选中的数目
     */
    private void updateSelectedCount() {
        getToolbar().setSubtitle("已选择(" + hasCount + ")张");
    }

    /**
     * 初始化UI
     *
     * @param count
     */
    private void init(int count) {
        // 设置文件夹名称为标题
        setTitle(albumFolder.getName());
        // 初始化RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_album_folder);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridViewDecorration(3));
        // 设置数据
        albumPictureAdapter = new AlbumPictureAdapter(this);
        recyclerView.setAdapter(albumPictureAdapter);
        albumPictureAdapter.notifyDataSetChanged(albumFolder.getAlbumPictureList());

        // 设置图片选择监听
        albumPictureAdapter.setOnPictureSelectedChaged(onPictureSelectedChanged);
        // 允许选择几张
        albumPictureAdapter.setCount(count);
    }

    /**
     * 图片选择监听
     */
    private AlbumPictureAdapter.OnPictureSelectedChanged onPictureSelectedChanged = new AlbumPictureAdapter.OnPictureSelectedChanged() {
        @Override
        public void onSelectedChanged(int position, boolean selected) {
            hasCount = selected ? hasCount + 1 : hasCount - 1;
            updateSelectedCount();
        }
    };

    @Override
    public boolean onOptionsItemSelectedCompat(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_album_sure) {
            ArrayList<AlbumPicture> albumPictureList = new ArrayList<>();

            ArrayList<AlbumPicture> tempAlbumPictureList = albumFolder.getAlbumPictureList();
            for (AlbumPicture albumPicture : tempAlbumPictureList) {
                if (albumPicture.isChecked()) {
                    albumPictureList.add(albumPicture);
                }
            }
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(KEY_OUTPUT_PICTURES, albumPictureList);
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }

    @Override
    public void onFinish() {
        setResult(RESULT_CANCELED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }
}
