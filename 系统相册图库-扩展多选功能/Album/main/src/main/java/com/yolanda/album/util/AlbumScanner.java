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
package com.yolanda.album.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.yolanda.album.entity.AlbumFolder;
import com.yolanda.album.entity.AlbumPicture;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>扫描所有图片的工具类。</p>
 * Created in 2016/5/8 19:04.
 *
 * @author Yolanda;
 */
public class AlbumScanner {

    private static AlbumScanner albumScanner;

    public static AlbumScanner getInstance() {
        if (albumScanner == null)
            synchronized (AlbumScanner.class) {
                albumScanner = new AlbumScanner();
            }
        return albumScanner;
    }

    private AlbumScanner() {
    }

    private static final String[] FIELDS = {
            //文件夹的ID
            MediaStore.Images.Media.BUCKET_ID,
            // 文件夹名称
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            // 图片ID
            MediaStore.Images.Media._ID,
            // 图片名称
            MediaStore.Images.Media.DISPLAY_NAME,
            // 图片路径
            MediaStore.Images.Media.DATA
    };

    /**
     * 开始扫描SD卡
     *
     * @param context
     * @return
     */
    public List<AlbumFolder> scan(Context context) {
        List<AlbumFolder> albumFolders = new ArrayList<>();
        // 扫描系统的媒体库
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, FIELDS);

        SparseArray<AlbumFolder> folderSparseArray = new SparseArray<>();
        // 也可以用Map，但是系统推荐使用SparseArray
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int folderId = cursor.getInt(0);
                String folderName = cursor.getString(1);
                int photoId = cursor.getInt(2);
                String photoName = cursor.getString(3);
                String photoPath = cursor.getString(4);
                AlbumPicture albumPhoto = new AlbumPicture(photoId, photoName, photoPath, false);

                AlbumFolder albumFolder = folderSparseArray.get(folderId);
                if (albumFolder == null) {// 文件夹不存在的时候
                    ArrayList<AlbumPicture> albumPhotos = new ArrayList<>();
                    albumPhotos.add(albumPhoto);
                    albumFolder = new AlbumFolder(folderId, folderName, albumPhotos);

                    folderSparseArray.put(folderId, albumFolder);
                } else {// 文件夹存在
                    albumFolder.getAlbumPictureList().add(albumPhoto);
                }
            }
            for (int i = 0; i < folderSparseArray.size(); i++) {
                albumFolders.add(folderSparseArray.get(folderSparseArray.keyAt(i)));
            }
        }
        return albumFolders;
    }

}

