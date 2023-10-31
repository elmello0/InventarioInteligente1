package com.example.inventariointeligente;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.inventariointeligente.R;

import java.util.List;

public class CofreAdapter extends RecyclerView.Adapter<CofreAdapter.ViewHolder> {
    private List<Cofre> cofres;

    public CofreAdapter(List<Cofre> cofres) {
        this.cofres = cofres;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cofre_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cofre cofre = cofres.get(position);
        holder.nombreCofre.setText(cofre.getNombre());


        Glide.with(holder.itemView.getContext())
                .load(cofre.getImagenUrl())
                .into(holder.imagenCofre);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetalleCofreActivity.class);
            intent.putExtra("COFRE_ID", cofre.getId()); 
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cofres.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreCofre;
        ImageView imagenCofre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreCofre = itemView.findViewById(R.id.nombreCofre);
            imagenCofre = itemView.findViewById(R.id.imagenCofre);
        }
    }
}
