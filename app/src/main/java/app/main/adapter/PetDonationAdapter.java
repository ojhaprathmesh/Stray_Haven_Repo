package app.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.main.R;
import app.main.model.PetDonation;

public class PetDonationAdapter extends RecyclerView.Adapter<PetDonationAdapter.ViewHolder> {

    private final List<PetDonation> petDonations;
    private OnDonateClickListener listener;

    public interface OnDonateClickListener {
        void onDonateClick(PetDonation petDonation, int position);
    }

    public PetDonationAdapter(List<PetDonation> petDonations) {
        this.petDonations = petDonations;
    }

    public void setOnDonateClickListener(OnDonateClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet_donation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PetDonation pet = petDonations.get(position);

        // Set pet image
        holder.petImage.setImageResource(pet.getImageResId());

        // Set pet description
        holder.petDescription.setText(pet.getDescription());

        // Set donation amounts
        holder.amountRaised.setText(pet.getAmountRaisedFormatted());
        holder.amountNeeded.setText(pet.getAmountNeededFormatted());

        // Set progress
        holder.donationProgress.setProgress(pet.getProgressPercentage());

        // Set days left
        holder.daysLeft.setText(pet.getDaysLeftFormatted());

        // Set button click listener
        holder.donateButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDonateClick(pet, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petDonations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView petImage;
        final TextView petDescription;
        final TextView amountRaised;
        final TextView amountNeeded;
        final ProgressBar donationProgress;
        final Button donateButton;
        final TextView daysLeft;

        ViewHolder(View view) {
            super(view);
            petImage = view.findViewById(R.id.petImage);
            petDescription = view.findViewById(R.id.petDescription);
            amountRaised = view.findViewById(R.id.amountRaised);
            amountNeeded = view.findViewById(R.id.amountNeeded);
            donationProgress = view.findViewById(R.id.donationProgress);
            donateButton = view.findViewById(R.id.donateButton);
            daysLeft = view.findViewById(R.id.daysLeft);
        }
    }
} 