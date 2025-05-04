package app.main.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import app.main.R;
import app.main.model.Notification;

public class NotificationAdapter extends ListAdapter<Notification, NotificationAdapter.NotificationViewHolder> {

    private static final String TAG = "NotificationAdapter";

    public NotificationAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
            // Assuming notifications have unique combinations of user image, message and timestamp
            return oldItem.getUserImage() == newItem.getUserImage() &&
                    oldItem.getMessage().equals(newItem.getMessage()) &&
                    oldItem.getTimestamp().equals(newItem.getTimestamp());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
            return oldItem.isLiked() == newItem.isLiked() &&
                    oldItem.isRead() == newItem.isRead();
        }
    };

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = getItem(position);

        holder.userImage.setImageResource(notification.getUserImage());
        holder.notificationText.setText(notification.getMessage());
        holder.timestamp.setText(notification.getTimestamp());

        // Set like button state
        holder.likeButton.setSelected(notification.isLiked());

        // Set like button click listener
        holder.likeButton.setOnClickListener(v -> {
            boolean newLikedState = !notification.isLiked();
            notification.setLiked(newLikedState);
            holder.likeButton.setSelected(newLikedState);

            // Log the like action
            String action = newLikedState ? "Liked" : "Unliked";
            Log.d(TAG, action + " notification: " + notification.getMessage());

            // Update tint color
            if (newLikedState) {
                holder.likeButton.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
            } else {
                holder.likeButton.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            }
        });

        // Set initial tint color
        if (notification.isLiked()) {
            holder.likeButton.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        } else {
            holder.likeButton.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        }

        // Set CardView background based on read status using the cardBackgroundColor attribute
        ((androidx.cardview.widget.CardView) holder.itemView).setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(),
                        notification.isRead() ? R.color.white : R.color.notification_unread)
        );
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView notificationText;
        TextView timestamp;
        ImageView likeButton;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            notificationText = itemView.findViewById(R.id.notificationText);
            timestamp = itemView.findViewById(R.id.timestamp);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }
} 