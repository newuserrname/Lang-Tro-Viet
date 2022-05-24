package com.dmt.thanhtruong.langtroviet.Fragments;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sign extends Fragment {
    private View view;
    private TextView textViewRegisterNow;
    private TextInputLayout layoutUsername, layoutPassword;
    private TextInputEditText txtLoginUsername, txtLoginPassword;
    private Button btnLogin;
    private ProgressDialog dialog;
    public Sign(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_sign,container, false);
        init();
        return view;
    }

    private void init() {
        textViewRegisterNow = view.findViewById(R.id.txt_RegisterNow);
        layoutPassword = view.findViewById(R.id.txtLayoutLoginPassword);
        layoutUsername = view.findViewById(R.id.txtLayoutLoginUsername);
        txtLoginUsername = view.findViewById(R.id.txt_LoginUsername);
        txtLoginPassword = view.findViewById(R.id.txt_LoginPassword);
        btnLogin = view.findViewById(R.id.btn_login);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        textViewRegisterNow.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.frameAuthContainer, new Register()).commit();
        });

        btnLogin.setOnClickListener(v->{
            if (kiemtra()) {
                login();
            }
        });

        txtLoginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtLoginUsername.getText().toString().isEmpty()) {
                    layoutUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtLoginPassword.getText().toString().length()>=6) {
                    layoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean kiemtra () {
        if (txtLoginUsername.getText().toString().isEmpty()) {
            layoutUsername.setErrorEnabled(true);
            layoutUsername.setError("Bạn phải nhập tài khoản!");
            return false;
        }
        if (txtLoginPassword.getText().toString().length()<6) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Mật khẩu không được nhỏ hơn 6 ký tự!");
            return false;
        }
        return true;
    }

    private void login() {
        dialog.setMessage("Chờ mình xíu!");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN, response -> {

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

                    startActivity(new Intent(((AuthActivity)getContext()), HomeActivity.class));
                    ((AuthActivity) getContext()).finish();
                    Toast.makeText(getContext(), "Chào thím!", Toast.LENGTH_SHORT).show();
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
                map.put("username", txtLoginUsername.getText().toString().trim());
                map.put("password", txtLoginPassword.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
