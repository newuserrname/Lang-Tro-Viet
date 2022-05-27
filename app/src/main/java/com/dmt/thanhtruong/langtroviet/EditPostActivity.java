package com.dmt.thanhtruong.langtroviet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dmt.thanhtruong.langtroviet.Fragments.HomeFragment;
import com.dmt.thanhtruong.langtroviet.Models.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {

    private int position =0, id= 0;
    private Button btnEditPost;
    private ImageView imgPost;
    private EditText txtTitle, txtAddress, txtPrice, txtArea, txtPhone, txtDesc;
    private Spinner spnDistrict, spnCategory;
    private Bitmap bitmap = null;
    private static final int GALLERY_CHANGE_EDIT_POST = 4;
    private ProgressDialog dialog;
    private Context context;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        init();
    }

    private void init() {
        Post post = new Post();
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnEditPost = findViewById(R.id.btnEditPost);
        imgPost = findViewById(R.id.imgEditPost);
        txtTitle = findViewById(R.id.txtTitleEditPost);
        txtAddress = findViewById(R.id.txtAddressEditPost);
        txtPrice = findViewById(R.id.txtPriceEditPost);
        txtArea = findViewById(R.id.txtAreaEditPost);
        txtPhone = findViewById(R.id.txtPhoneEditPost);
        txtDesc = findViewById(R.id.txtDescEditPost);
        spnDistrict = findViewById(R.id.spnDistrictEditPost);
        spnCategory = findViewById(R.id.spnCategoryEditPost);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        id = getIntent().getIntExtra("postId", 0);
        txtTitle.setText(getIntent().getStringExtra("title"));
        txtAddress.setText(getIntent().getStringExtra("address"));
        txtPrice.setText(getIntent().getStringExtra("price"));
        txtArea.setText(getIntent().getStringExtra("area"));
        btnEditPost.setOnClickListener(v->{
                savePost();
        });
    }

    private void savePost() {
        dialog.setMessage("Đang lưu");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,Constant.UPDATE_POST, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    // update the post in recycler view
                    Post post = HomeFragment.arrayList.get(position);
                    post.setTitle(txtTitle.getText().toString());
                    post.setAddress(txtAddress.getText().toString());
                    post.setPrice(txtPrice.getText().toString());
                    post.setArea(txtArea.getText().toString());
                    HomeFragment.arrayList.set(position,post);
                    HomeFragment.recyclerView.getAdapter().notifyItemChanged(position);
                    HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token",  "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization","Bearer"+ token);
                return map;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id+"");
                map.put("title", txtTitle.getText().toString());
                map.put("address", txtAddress.getText().toString());
                map.put("price", txtPrice.getText().toString());
                map.put("area", txtArea.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(EditPostActivity.this);
        requestQueue.add(request);

    }


    public void cancelEdit(View view) {
        super.onBackPressed();
    }

    public void changeImages(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_CHANGE_EDIT_POST);
    }
}