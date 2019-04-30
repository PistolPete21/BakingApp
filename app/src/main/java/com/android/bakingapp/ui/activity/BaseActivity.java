package com.android.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import com.android.bakingapp.R;

public class BaseActivity extends AppCompatActivity {

    CoordinatorLayout fragmentContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        fragmentContainer = findViewById(R.id.fragment_container);
    }
}
