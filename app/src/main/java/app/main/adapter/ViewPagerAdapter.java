package app.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private final List<Integer> layoutList;

    public ViewPagerAdapter(List<Integer> layoutList) {
        this.layoutList = layoutList;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        // No specific binding needed as layouts are pre-configured
        // Add any dynamic binding here if needed in the future
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
            // No need to find individual views as we're using complete layouts
        }
    }
}
