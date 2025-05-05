package app.main.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import app.main.R;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    Context context;
    List<Pet> petList;

    public PetAdapter(Context context, List<Pet> petList) {
        this.context = context;
        this.petList = petList;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pet_item, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.petName.setText(pet.getName());
        holder.petImage.setImageResource(pet.getImage());
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView petImage;
        TextView petName;

        public PetViewHolder(View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.petImage);
            petName = itemView.findViewById(R.id.petName);
        }
    }
}
