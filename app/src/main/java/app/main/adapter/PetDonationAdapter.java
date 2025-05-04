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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import app.main.R;
import app.main.model.PetDonation;

public class PetDonationAdapter extends RecyclerView.Adapter<PetDonationAdapter.PetDonationViewHolder> {

    private final List<PetDonation> petDonations;
    private OnDonateClickListener donateClickListener;

    public PetDonationAdapter(List<PetDonation> petDonations) {
        this.petDonations = petDonations;
    }

    public void setOnDonateClickListener(OnDonateClickListener listener) {
        this.donateClickListener = listener;
    }

    @NonNull
    @Override
    public PetDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_donation, parent, false);
        return new PetDonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetDonationViewHolder holder, int position) {
        PetDonation donation = petDonations.get(position);
        holder.bind(donation);
    }

    @Override
    public int getItemCount() {
        return petDonations.size();
    }

    public interface OnDonateClickListener {
        void onDonateClick(PetDonation petDonation, int position);
    }

    public class PetDonationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView petImage;
        private final TextView petDescription;
        private final TextView amountRaised;
        private final TextView amountNeeded;
        private final ProgressBar donationProgress;
        private final TextView daysLeft;
        private final Button donateButton;

        public PetDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.petImage);
            petDescription = itemView.findViewById(R.id.petDescription);
            amountRaised = itemView.findViewById(R.id.amountRaised);
            amountNeeded = itemView.findViewById(R.id.amountNeeded);
            donationProgress = itemView.findViewById(R.id.donationProgress);
            daysLeft = itemView.findViewById(R.id.daysLeft);
            donateButton = itemView.findViewById(R.id.donateButton);
        }

        public void bind(PetDonation donation) {
            // Set image
            petImage.setImageResource(donation.getImageResourceId());
            
            // Set description
            petDescription.setText(donation.getDescription());
            
            // Format currency amounts
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            String raisedFormatted = currencyFormat.format(donation.getAmountRaised() / 100.0)
                    .replace("$", "₹");
            String neededFormatted = currencyFormat.format(donation.getAmountNeeded() / 100.0)
                    .replace("$", "₹");
            
            // Set formatted text
            amountRaised.setText(String.format("%s raised", raisedFormatted));
            amountNeeded.setText(String.format("%s needed", neededFormatted));
            
            // Set progress
            donationProgress.setProgress(donation.getProgressPercentage());
            
            // Set days left
            daysLeft.setText(String.format("%d Days Left", donation.getDaysLeft()));
            
            // Set click listener for donate button
            donateButton.setOnClickListener(v -> {
                if (donateClickListener != null) {
                    donateClickListener.onDonateClick(donation, getAdapterPosition());
                }
            });
        }
    }
} 