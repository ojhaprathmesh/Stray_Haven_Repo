package app.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.main.R;
import app.main.model.NGO;

public class NGOAdapter extends RecyclerView.Adapter<NGOAdapter.NGOViewHolder> {

    private final List<NGO> ngoList;
    private final Context context;
    private final OnNGOClickListener listener;

    // Interface for click events
    public interface OnNGOClickListener {
        void onNGOClick(NGO ngo, int position);
    }

    public NGOAdapter(Context context, List<NGO> ngoList, OnNGOClickListener listener) {
        this.context = context;
        this.ngoList = ngoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NGOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ngo_card, parent, false);
        return new NGOViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NGOViewHolder holder, int position) {
        NGO ngo = ngoList.get(position);

        // Set data to views
        holder.nameTextView.setText(ngo.getName());
        holder.locationTextView.setText(ngo.getLocation());
        holder.petsTextView.setText(ngo.getPets());
        holder.contactTextView.setText(ngo.getContact());
        holder.taglineTextView.setText("-" + ngo.getTagline());
        holder.ratingTextView.setText("4.5"); // In a real app, this would come from the NGO data

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNGOClick(ngo, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ngoList.size();
    }

    // ViewHolder class
    public static class NGOViewHolder extends RecyclerView.ViewHolder {
        ImageView logoImageView;
        TextView nameTextView, locationTextView, petsTextView, contactTextView, taglineTextView, ratingTextView;

        NGOViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(R.id.ngo_logo);
            nameTextView = itemView.findViewById(R.id.ngo_name);
            locationTextView = itemView.findViewById(R.id.ngo_location);
            petsTextView = itemView.findViewById(R.id.ngo_pets);
            contactTextView = itemView.findViewById(R.id.ngo_contact);
            taglineTextView = itemView.findViewById(R.id.ngo_tagline);
            ratingTextView = itemView.findViewById(R.id.ngo_rating);
        }
    }

    // Method to update the dataset
    public void updateData(List<NGO> newNGOList) {
        this.ngoList.clear();
        this.ngoList.addAll(newNGOList);
        notifyDataSetChanged();
    }
} 