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
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.yolanda.album.R;
import com.yolanda.album.adapter.AlbumFolderAdapter;
import com.yolanda.album.entity.AlbumFolder;
import com.yolanda.album.entity.AlbumPicture;
import com.yolanda.album.util.AlbumScanner;
import com.yolanda.album.util.App;
import com.yolanda.album.util.DisplayUtil;
import com.yolanda.album.util.ThreadUtil;
import com.yolanda.album.util.WaitDialog;
import com.yolanda.album.view.GridViewDecorration;
import com.yolanda.nohttp.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>图库入口：显示文件夹，让用户选择。</p>
 * Created in 2016/5/8 18:42.
 *
 * @author Yolanda;
 */
public class AlbumActivity extends BasicActivity implements AlbumFolderAdapter.OnItemClickListener {

    /**
     * 图片选择回调
     */
    private static final int REQUEST_CODE_PICTURE_SELECT = 0x02;

    /**
     * 输入int参数：要选择的图片张数
     */
    public static final String KEY_INPUT_COUNT = "KEY_INPUT_COUNT";
    /**
     * 输出参数：{@code List<String>}
     */
    public static final String KEY_OUTPUT_STRING = "KEY_OUTPUT_STRING";
    /**
     * 允许用户选择的张数
     */
    private int count = 1;
    /**
     * 用户已经选择的
     */
    private int hasCount = 0;
    /**
     * 文件夹适配器
     */
    private AlbumFolderAdapter albumFolderAdapter;
    /**
     * 扫描到的结果
     */
    private List<AlbumFolder> albumFolders;
    /**
     * 已经选择的图片
     */
    private ArrayList<AlbumPicture> hasSelectedPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("图库");
        setContentView(R.layout.activity_album);
        setBackBarEnable(true);
        DisplayUtil.initScreen(this);

        count = getIntent().getIntExtra(KEY_INPUT_COUNT, 1);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_album_folder);
        recyclerView.addItemDecoration(new GridViewDecorration(3));

        // 千万记得要给RecyclerView设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        albumFolderAdapter = new AlbumFolderAdapter();
        recyclerView.setAdapter(albumFolderAdapter);

        albumFolderAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scan();// 每次打开界面的时候都扫描
    }

    /**
     * 解析扫描出来的图片
     */
    private void parseFolder() {
        if (albumFolderAdapter != null && albumFolders != null) {
            albumFolderAdapter.notifyDataSetChanged(albumFolders);
        }
    }

    /**
     * 点击某个文件夹的处理
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        AlbumFolder albumFolder = albumFolders.get(position);
        Intent intent = new Intent(this, PictureActivity.class);
        intent.putExtra(PictureActivity.KEY_INPUT_ALBUM_FOLDER, albumFolder);
        intent.putExtra(KEY_INPUT_COUNT, (count - hasCount));
        startActivityForResult(intent, REQUEST_CODE_PICTURE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICTURE_SELECT && resultCode == RESULT_OK) {
            List<AlbumPicture> selectedPictures = data.getParcelableArrayListExtra(PictureActivity.KEY_OUTPUT_PICTURES);

            if (hasSelectedPictures == null)
                hasSelectedPictures = new ArrayList<>();
            hasSelectedPictures.addAll(selectedPictures);
            hasCount = hasSelectedPictures.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelectedCompat(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_album_sure) {
            Intent intent = new Intent();
            ArrayList<String> stringArrayList = new ArrayList<>();
            for (AlbumPicture albumPicture : hasSelectedPictures) {
                stringArrayList.add(albumPicture.getPath());
            }
            intent.putStringArrayListExtra(KEY_OUTPUT_STRING, stringArrayList);
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }

    @Override
    public void onFinish() {
        setResult(RESULT_CANCELED);
    }

    /////////扫描相册////////

    /**
     * 阻塞的dialog
     */
    private WaitDialog waitDialog;

    /**
     * 扫描图片
     */
    private void scan() {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(this);
        }
        if (!waitDialog.isShowing()) {
            waitDialog.show();
        }
        ThreadUtil.execute(runnable);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
            parseFolder();
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            albumFolders = AlbumScanner.getInstance().scan(App.getInstance());
            Logger.d("扫描到的文件夹数量：" + albumFolders.size());
            handler.obtainMessage().sendToTarget();
        }
    };
}
