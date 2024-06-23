package com.tom.sky_rental;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ResidencesAdapter extends RecyclerView.Adapter<ResidencesAdapter.ResidenceViewHolder> {

    private List<Residence> residencesList;
    private Context context;

    public ResidencesAdapter(Context context, List<Residence> residenceList) {
        this.context = context;
        this.residencesList = residenceList;
    }

    @NonNull
    @Override
    public ResidenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.residence_item, parent, false);
        return new ResidenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidenceViewHolder holder, int position) {
        Residence residence = residencesList.get(position);

        holder.nameTextView.setText(residence.getName());
        holder.locationTextView.setText(residence.getLocation());
        holder.ratingsTextView.setText(String.valueOf(residence.getRatings()));
        holder.numReviewsTextView.setText(String.format("(%d reviews)", residence.getNumReviews()));
        System.out.println(residence.getRooms());
        Glide.with(context)
                .load(residence.getImageUrl())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("RESIDENCE", residence);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return residencesList.size();
    }

    static class ResidenceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView locationTextView;
        TextView ratingsTextView;
        TextView numReviewsTextView;
        ImageView imageView;

        public ResidenceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.residenceNameTextView);
            locationTextView = itemView.findViewById(R.id.residenceLocationTextView);
            ratingsTextView = itemView.findViewById(R.id.residenceRatingsTextView);
            numReviewsTextView = itemView.findViewById(R.id.residenceNumReviewsTextView);
            imageView = itemView.findViewById(R.id.residenceImageView);
        }
    }
}
