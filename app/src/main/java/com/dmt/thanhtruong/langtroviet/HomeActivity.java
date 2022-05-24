package com.dmt.thanhtruong.langtroviet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;

import com.dmt.thanhtruong.langtroviet.Fragments.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private static final int GALLERY_AND_POST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new HomeFragment()).commit();
        init();
    }

    private void init() {
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(v-> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_AND_POST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_AND_POST && resultCode==RESULT_OK);
            Uri imgUri = data.getData();
            Intent i = new Intent(HomeActivity.this, AddPostActivity.class);
            i.setData(imgUri);
            startActivity(i);
    }
}