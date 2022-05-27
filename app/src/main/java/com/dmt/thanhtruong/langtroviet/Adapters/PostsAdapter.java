package com.dmt.thanhtruong.langtroviet.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmt.thanhtruong.langtroviet.Constant;
import com.dmt.thanhtruong.langtroviet.EditPostActivity;
import com.dmt.thanhtruong.langtroviet.HomeActivity;
import com.dmt.thanhtruong.langtroviet.Models.Post;
import com.dmt.thanhtruong.langtroviet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder> {

    private Context context;
    private ArrayList<Post> list;
    private ArrayList<Post> listAll;
    private SharedPreferences preferences;

    public PostsAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
        preferences = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
        return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = list.get(position);
        Picasso.get().load(Constant.URL+"public/uploads/avatars/"+post.getUser().getAvatar()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL+"public/uploads/images/"+post.getImages()).into(holder.imgPost);
        holder.txtNameProfile.setText(post.getUser().getName());
        holder.txtDate.setText(post.getDate());
        holder.txtTitle.setText(post.getTitle());
        holder.txtPrice.setText(post.getPrice()+ "VNĐ/Tháng");
        holder.txtArea.setText(post.getArea()+" m2");
        holder.txtAddress.setText(post.getAddress());
        holder.txtCount_view.setText(post.getCount_view()+" ");

        if (post.getUser().getId()==preferences.getInt("id", 0)) {
            holder.btnPostOption.setVisibility(View.VISIBLE);
        } else {
            holder.btnPostOption.setVisibility(View.GONE);
        }

        holder.btnPostOption.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(context, holder.btnPostOption);
            popupMenu.inflate(R.menu.menu_post_options);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.item_edit: {
                            Intent intent = new Intent((HomeActivity)context, EditPostActivity.class);
                            intent.putExtra("postId", post.getId());
                            intent.putExtra("position", position);
                            intent.putExtra("title", post.getTitle());
                            intent.putExtra("address", post.getAddress());
                            intent.putExtra("price", post.getPrice());
                            intent.putExtra("area", post.getArea());
                            ((HomeActivity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            context.startActivity(intent);
                            return true;
                        }
                        case R.id.item_delete: {

                        }
                        case R.id.item_hide_post: {

                        }
                    }

                    return false;
                }
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Post> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(listAll);
            } else {
                for (Post post : listAll) {
                    if (post.getAddress().toLowerCase().contains(constraint.toString().toLowerCase())
                    || post.getUser().getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(post);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends Post>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getFilter() {
        return filter;
    }

    class PostsHolder extends RecyclerView.ViewHolder {

        private TextView txtNameProfile, txtDate, txtTitle, txtPrice, txtArea, txtAddress, txtCount_view, txtComment;
        private CircleImageView imgProfile;
        private ImageView imgPost;
        private ImageButton btnPostOption, btnBookmask, btnComment;

        public PostsHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProfile = itemView.findViewById(R.id.txtPostNameProfile);
            txtDate = itemView.findViewById(R.id.txtPostDate);
            txtTitle = itemView.findViewById(R.id.txtPostTitle);
            txtPrice = itemView.findViewById(R.id.txtPostPrice);
            txtArea = itemView.findViewById(R.id.txtPostArea);
            txtAddress = itemView.findViewById(R.id.txtPostAddress);
            txtCount_view = itemView.findViewById(R.id.txtPostCount_view);
            txtComment = itemView.findViewById(R.id.txtPostComment);
            imgProfile = itemView.findViewById(R.id.imgPostProfile);
            imgPost = itemView.findViewById(R.id.imgPostImages);
            btnPostOption = itemView.findViewById(R.id.btnPostOption);
            btnBookmask = itemView.findViewById(R.id.btnPostBookmark);
            btnComment = itemView.findViewById(R.id.btnPostComment);
            btnPostOption.setVisibility(View.GONE);
        }

    }
}
