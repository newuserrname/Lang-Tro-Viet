package com.dmt.thanhtruong.langtroviet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmt.thanhtruong.langtroviet.Adapters.ViewPagerAdapter;

public class OnBoardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button btnLeft, btnRight;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        init();
    }

    private void init(){
        viewPager = findViewById(R.id.view_paper);
        btnLeft = findViewById(R.id.btn_Lefttskip);
        btnRight = findViewById(R.id.btn_Rightnext);
        dotsLayout = findViewById(R.id.dots_layout);
        viewPagerAdapter = new ViewPagerAdapter(this);
        addDots(0);
        viewPager.addOnPageChangeListener(listener);
        viewPager.setAdapter(viewPagerAdapter);

        btnRight.setOnClickListener(v->{
            if (btnRight.getText().toString().equals("Tiếp Tục")) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            } else {
               startActivity(new Intent(OnBoardActivity.this, AuthActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
               finish();
           }
        });

        btnLeft.setOnClickListener(v->{
            viewPager.setCurrentItem(viewPager.getCurrentItem()+2);
        });
    }

    private void addDots(int position) {
        dotsLayout.removeAllViews();
        dots = new TextView[3];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorLightGrey));
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorGrey));
        }
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            if (position == 0) {
                btnLeft.setVisibility(View.VISIBLE);
                btnLeft.setEnabled(true);
                btnRight.setText("Tiếp Tục");
            } else if (position == 1){
                btnLeft.setVisibility(View.GONE);
                btnLeft.setEnabled(false);
                btnRight.setText("Tiếp Tục");
            } else {
                btnLeft.setVisibility(View.GONE);
                btnLeft.setEnabled(false);
                btnRight.setText("Kết Thúc");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}