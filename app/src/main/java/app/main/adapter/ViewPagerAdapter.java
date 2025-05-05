package app.main.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.main.R;
import app.main.activity.SP1Activity;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private final List<Integer> layoutList;

    public ViewPagerAdapter(List<Integer> layoutList) {
        this.layoutList = layoutList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set up click listeners for buttons in each layout
        if (position == 0) {
            // First ad layout - find subscribe and win button
            Button subscribeButton = holder.itemView.findViewById(R.id.adButton);
            if (subscribeButton != null) {
                subscribeButton.setOnClickListener(v -> {
                    // Navigate to SP1 Activity
                    Intent intent = new Intent(holder.itemView.getContext(), SP1Activity.class);
                    holder.itemView.getContext().startActivity(intent);
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return layoutList.get(position);
    }

    @Override
    public int getItemCount() {
        return layoutList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            // No need to find individual views as we're handling them in onBindViewHolder
        }
    }
}
