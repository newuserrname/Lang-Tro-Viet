package com.dmt.thanhtruong.langtroviet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dmt.thanhtruong.langtroviet.Fragments.Sign;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new Sign()).commit();
    }
}