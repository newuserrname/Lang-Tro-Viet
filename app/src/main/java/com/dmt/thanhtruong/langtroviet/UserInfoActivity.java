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
import android.widget.Button;

import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    private TextInputLayout layoutName, layoutPhone;
    private TextInputEditText txtName, txtPhone;
    private Button btnContinue;
    private TextView txtSelectAvatar;
    private CircleImageView circleImageView;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        layoutName = findViewById(R.id.txtLayoutNameUserInfo);
        layoutPhone = findViewById(R.id.txtLayoutPhoneUserInfo);
        txtName = findViewById(R.id.txtNameUserInfo);
        txtPhone = findViewById(R.id.txtPhoneUserInfo);
        txtSelectAvatar = findViewById(R.id.txtSelectAvatar);
        btnContinue = findViewById(R.id.btnContinue);
        circleImageView = findViewById(R.id.imgUserInfo);

        txtSelectAvatar.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_PROFILE);
        });

        btnContinue.setOnClickListener(v -> {
            if (kiemtra()) {
                saveUserInfo();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_ADD_PROFILE && resultCode==RESULT_OK) {

            Uri imgUri = data.getData();
            circleImageView.setImageURI(imgUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean kiemtra() {

        if (txtName.getText().toString().isEmpty()) {
            layoutName.setErrorEnabled(true);
            layoutName.setError("B???n ph???i ??i???n t??n c???a m??nh!");
            return false;
        }
        if (txtPhone.getText().toString().isEmpty()) {
            layoutPhone.setErrorEnabled(true);
            layoutPhone.setError("??i???n s??? ??i???n tho???i n???a nh??!");
            return false;
        }

        return true;
    }

    private void saveUserInfo() {
        dialog.setMessage("??ang l??u");
        dialog.show();
        String name = txtName.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, Constant.SAVE_USER_INFO, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("avatar", object.getString("avatar"));
                    editor.apply();
                    startActivity(new Intent(UserInfoActivity.this, HomeActivity.class));
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

            // add token

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError  {
                String token = userPref.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " +token);
                return map;
            }

            // post and edit profile

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("phone", phone);
                map.put("avatar", bitmapToString(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(UserInfoActivity.this);
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
}