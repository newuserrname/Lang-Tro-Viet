package com.dmt.thanhtruong.langtroviet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.dmt.thanhtruong.langtroviet.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    private int images[] = {
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
    };

    private String titles[] = {
            "Giải pháp tìm kiếm mới",
            "An toàn",
            "Nhanh chóng"
    };

    private String descs[] = {
            "Tiết kiệm nhiều thời gian cho bạn với giải pháp và công nghệ mới",
            "Với đội ngũ Quản trị viên kiểm duyệt hiệu quả",
            "Chất Lượng đem lại sự tin tưởng"
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_pager, container, false);

        ImageView imageView = v.findViewById(R.id.img_ViewPaper);
        TextView textViewTitle = v.findViewById(R.id.txt_TitleViewPaper);
        TextView textViewDesc = v.findViewById(R.id.txt_DescViewPaper);

        imageView.setImageResource(images[position]);
        textViewTitle.setText(titles[position]);
        textViewDesc.setText(descs[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
