/*
Copyright 2016 RÃºben Sousa
Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */
package com.github.shareme.greenandroid.floatingtoolbarapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.shareme.greenandroid.floatingtoolbar.FloatingToolbar;


public class MainActivity extends AppCompatActivity implements FloatingToolbar.ItemClickListener, Toolbar.OnMenuItemClickListener {

    private Toolbar mToolbar;
    private FloatingToolbar mFloatingToolbar;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFloatingToolbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);

        mToolbar.setTitle(R.string.app_name);
        mToolbar.inflateMenu(R.menu.menu_toolbar);
        mToolbar.setOnMenuItemClickListener(this);

        mAdapter = new CustomAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);


        mFloatingToolbar.setClickListener(this);
        mFloatingToolbar.attachFab(fab);
        mFloatingToolbar.attachRecyclerView(recyclerView);

        View customView = mFloatingToolbar.getCustomView();
        if (customView != null) {
            customView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFloatingToolbar.hide();
                }
            });
        }
    }

    @Override
    public void onItemClick(MenuItem item) {
        mAdapter.addItem(item);
    }

    @Override
    public void onItemLongClick(MenuItem item) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_snackbar) {
            Snackbar.make(mToolbar, "Here's a SnackBar", Snackbar.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
