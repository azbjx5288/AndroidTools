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

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yolanda.album.R;

/**
 * Created in 2016/5/8 18:19.
 *
 * @author Yolanda;
 */
public abstract class BasicActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setContentView(R.layout.activity_basic);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitleTextColor(Color.GREEN);
        setSupportActionBar(toolbar);
        viewGroup = (ViewGroup) findViewById(R.id.content);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setBackBarEnable(boolean enable) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        viewGroup.removeAllViews();
        getLayoutInflater().inflate(layoutResID, viewGroup, true);
    }

    @Override
    public void setContentView(View view) {
        viewGroup.removeAllViews();
        viewGroup.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        viewGroup.addView(view, params);
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onFinish();
            finish();
            return true;
        }
        return onOptionsItemSelectedCompat(item);
    }

    @Override
    public final void finish() {
        super.finish();
    }

    public void onFinish() {

    }

    public boolean onOptionsItemSelectedCompat(MenuItem item) {
        return false;
    }
}
