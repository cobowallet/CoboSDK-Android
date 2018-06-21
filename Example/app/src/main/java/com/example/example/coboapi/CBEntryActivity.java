package com.example.example.coboapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cobo.sdk.CoboSDK;
import com.example.example.R;

public class CBEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbentry);

        Intent intent = getIntent();
        CoboSDK.getInstance().handleIntent(intent);
        finish();
    }
}
