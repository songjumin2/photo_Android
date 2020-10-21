package com.songjumin.photoapp2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.songjumin.photoapp2.R;
import com.songjumin.photoapp2.model.Item;
import com.songjumin.photoapp2.model.Post;
import com.songjumin.photoapp2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Item> postArrayList;

    public RecyclerViewAdapter(Context context, List<Item> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Item post = postArrayList.get(position);

        holder.txtContent.setText(post.getContent());
        holder.txtDate.setText(post.getCreatedAt());

        String url = Utils.BASE_URL + "/uploads/" + post.getPhotoUrl();
        Glide.with(context).load(url).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPhoto;
        TextView txtContent;
        TextView txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}