/*
Copyright 2016 florent37, Inc.
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
package com.github.shareme.greenandroid.fiftyshadesofapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.shareme.greenandroid.fiftyshadesof.FiftyShadesOf;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load();
            }
        });
    }

    void load(){
        final FiftyShadesOf fiftyShadesOf = FiftyShadesOf.with(this)
            .on(R.id.layout, R.id.layout1, R.id.layout2)
            .start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fiftyShadesOf.stop();
            }
        }, 2000);
    }
}
