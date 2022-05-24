package com.dmt.thanhtruong.langtroviet.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dmt.thanhtruong.langtroviet.AuthActivity;
import com.dmt.thanhtruong.langtroviet.Constant;
import com.dmt.thanhtruong.langtroviet.HomeActivity;
import com.dmt.thanhtruong.langtroviet.R;
import com.dmt.thanhtruong.langtroviet.UserInfoActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends Fragment{
    private View view;
    private TextView textViewLoginNow;
    private TextInputLayout layoutRegisterUsername, layoutRegisterPassword, layoutConformPassword, layoutRegisterEmail, layoutRegisterName;
    private TextInputEditText txtRegisterUsername, txtRegisterPassword, txtConformPassword, txtRegisterEmail, txtRegisterName;
    private Button btnRegister;
    private ProgressDialog dialog;
    public Register(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_register, container, false);
        init();
        return view;
    }

    private void init() {
        textViewLoginNow = view.findViewById(R.id.txt_LoginNow);
        layoutRegisterUsername = view.findViewById(R.id.txtLayoutRegisterUsername);
        layoutRegisterPassword = view.findViewById(R.id.txtLayoutRegisterPassword);
        layoutConformPassword = view.findViewById(R.id.txtLayoutConformPassword);
        layoutRegisterEmail = view.findViewById(R.id.txtLayoutRegisterEmail);
        layoutRegisterName = view.findViewById(R.id.txtLayoutRegisterName);
        txtRegisterUsername = view.findViewById(R.id.txt_RegisterUsername);
        txtRegisterPassword = view.findViewById(R.id.txt_RegisterPassword);
        txtConformPassword = view.findViewById(R.id.txt_ConformPassword);
        txtRegisterEmail = view.findViewById(R.id.txt_RegisterEmail);
        txtRegisterName = view.findViewById(R.id.txt_RegisterName);
        btnRegister = view.findViewById(R.id.btn_Register);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        textViewLoginNow.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.frameAuthContainer, new Sign()).commit();
        });

        btnRegister.setOnClickListener(v->{
            if (kiemtra()) {
                register();
            }
        });

        txtRegisterUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtRegisterUsername.getText().toString().isEmpty()) {
                    layoutRegisterUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtRegisterPassword.getText().toString().length()>6) {
                    layoutRegisterPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtConformPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtConformPassword.getText().toString().equals(txtRegisterPassword.getText().toString())) {
                    layoutConformPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean kiemtra() {
        if (txtRegisterUsername.getText().toString().isEmpty()) {
            layoutRegisterUsername.setErrorEnabled(true);
            layoutRegisterUsername.setError("Tài khoản phải có nhé!");
            return false;
        }

        if (txtRegisterPassword.getText().toString().length()<6) {
            layoutRegisterPassword.setErrorEnabled(true);
            layoutRegisterPassword.setError("Mật khẩu không được nhỏ hơn 6 ký tự!");
            return false;
        }

        if (!txtConformPassword.getText().toString().equals(txtRegisterPassword.getText().toString())) {
            layoutConformPassword.setErrorEnabled(true);
            layoutConformPassword.setError("Phải giống mật khẩu được nhập!");
            return false;
        }
        return true;
    }

    private void register() {
        dialog.setMessage("Đang Đăng ký cho nè!");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTER, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    JSONObject user = object.getJSONObject("user");

                    SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token", object.getString("token"));
                    editor.putString("name", user.getString("name"));
                    editor.putString("phone", user.getString("phone"));
                    editor.putString("avatar", user.getString("avatar"));
                    editor.putBoolean("ThimdaLogin", true);
                    editor.apply();

                    startActivity(new Intent(((AuthActivity)getContext()), UserInfoActivity.class));
                    ((AuthActivity) getContext()).finish();
                    Toast.makeText(getContext(), "Đăng ký thành công chúc mừng!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }, error -> {

            error.printStackTrace();
            dialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", txtRegisterUsername.getText().toString().trim());
                map.put("password", txtRegisterPassword.getText().toString());
                map.put("email", txtRegisterEmail.getText().toString().trim());
                map.put("name", txtRegisterName.getText().toString().trim());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
