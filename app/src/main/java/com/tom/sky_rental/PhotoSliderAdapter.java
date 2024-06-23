package com.tom.sky_rental;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PhotoSliderAdapter extends RecyclerView.Adapter<PhotoSliderAdapter.PhotoViewHolder> {

    private List<String> photoUrls;
    private Context context;

    public PhotoSliderAdapter(List<String> photoUrls, Context context) {
        this.photoUrls = photoUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photoUrl = photoUrls.get(position);
        Glide.with(context).load(photoUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photoUrls.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoImageView);
        }
    }
}

