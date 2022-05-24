package com.dmt.thanhtruong.langtroviet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dmt.thanhtruong.langtroviet.Fragments.HomeFragment;
import com.dmt.thanhtruong.langtroviet.Models.Post;
import com.dmt.thanhtruong.langtroviet.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private Button btnPost;
    private ImageView imgPost;
    private EditText txtTitle, txtAddress, txtPrice, txtArea, txtPhone, txtDesc;
    private Spinner spnDistrict, spnCategory;
    private Bitmap bitmap = null;
    private static final int GALLERY_CHANGE_POST = 3;
    private ProgressDialog dialog;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        init();
    }

    private void init() {
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnPost = findViewById(R.id.btnAddPost);
        imgPost = findViewById(R.id.imgAddPost);
        txtTitle = findViewById(R.id.txtTitleAddPost);
        txtAddress = findViewById(R.id.txtAddressAddPost);
        txtPrice = findViewById(R.id.txtPriceAddPost);
        txtArea = findViewById(R.id.txtAreaAddPost);
        txtPhone = findViewById(R.id.txtPhoneAddPost);
        txtDesc = findViewById(R.id.txtDescAddPost);
        spnDistrict = findViewById(R.id.spnDistrictAddPost);
        spnCategory = findViewById(R.id.spnCategoryAddPost);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        imgPost.setImageURI(getIntent().getData());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),getIntent().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnPost.setOnClickListener(v->{
            if (!txtTitle.getText().toString().isEmpty()) {
                post();
            } else {
                Toast.makeText(this, "Bạn phải điền tiêu đề", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void kiemtra() {

    }

    private void post() {
        dialog.setMessage("Đang đăng!");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constant.ADD_POST, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    JSONObject postObject = object.getJSONObject("post");
                    JSONObject userObject = postObject.getJSONObject("user");

                    User user = new User();
                    user.setId(userObject.getInt("id"));
                    user.setName(userObject.getString("name"));
                    user.setAvatar(userObject.getString("avatar"));

                    Post post = new Post();
                    post.setUser(user);
                    post.setId(postObject.getInt("id"));
                    post.setTitle(postObject.getString("title"));
                    post.setAddress(postObject.getString("address"));
                    post.setPrice(postObject.getInt("price"));
                    post.setArea(postObject.getInt("area"));
                    post.setPhone(postObject.getString("phone"));
                    post.setDescription(postObject.getString("description"));
                    post.setImages(postObject.getString("images"));
                    post.setSelfLike(false);
                    post.setDate(postObject.getString("created_at"));
                    post.setCount_view(0);

                    HomeFragment.arrayList.add(0, post);
                    HomeFragment.recyclerView.getAdapter().notifyItemInserted(0);
                    HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();
                    Toast.makeText(this, "Đăng thành công!", Toast.LENGTH_SHORT).show();
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        }, error -> {
            error.printStackTrace();
            dialog.dismiss();
        }) {
           //them token den title

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+token);
                return map;
            }
            //post data phong tro


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("title", txtTitle.getText().toString().trim());
                map.put("address", txtAddress.getText().toString().trim());
                map.put("price", txtPrice.getText().toString().trim());
                map.put("area", txtArea.getText().toString().trim());
                map.put("phone", txtPhone.getText().toString().trim());
                map.put("description", txtPhone.getText().toString().trim());
                map.put("images", bitmapToString(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(AddPostActivity.this);
        queue.add(request);

    }

    private String bitmapToString(Bitmap bitmap) {


        if (bitmap!= null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array,Base64.DEFAULT);
        }
        return "";
    }

    public void cancelPost(View view) {
        super.onBackPressed();
    }

    public void changeImages(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_CHANGE_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_CHANGE_POST && resultCode==RESULT_OK) {
            Uri imgUri = data.getData();
            imgPost.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),getIntent().getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}