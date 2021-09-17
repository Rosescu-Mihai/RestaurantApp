package com.example.managementrestaurantapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managementrestaurantapp.clase.Ingredient;
import com.example.managementrestaurantapp.clase.Preparat;
import com.example.managementrestaurantapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MancareAdapter extends RecyclerView.Adapter<MancareAdapter.MancareViewHolder> {
    private final List<Preparat> mancareList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public static class MancareViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_nume;
        public TextView textView_pret_mancare;
        public TextView textView_ingrediente;

        public MancareViewHolder(@NonNull @NotNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            textView_nume = itemView.findViewById(R.id.nume_preparat_mancare);
            textView_pret_mancare = itemView.findViewById(R.id.pret_preparat_mancare);
            textView_ingrediente = itemView.findViewById(R.id.ingrediente_preparat_mancare);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MancareViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meniu_mancare_item, parent, false);
        MancareViewHolder mancareViewHolder = new MancareViewHolder(v, onItemClickListener);
        return mancareViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MancareAdapter.MancareViewHolder holder, int position) {
        Preparat preparat = mancareList.get(position);
        holder.textView_nume.setText(preparat.getNume_preparat());
        holder.textView_pret_mancare.setText(String.valueOf(preparat.getPret_unitar())+" lei");
        StringBuilder ing = new StringBuilder();
        for (Ingredient ingredient:preparat.getIngrediente()) {
            String ingAux = ingredient.getNumeIngredient()+" "+ingredient.getCantitateTotala();
            ing.append(ingAux);
        }
        holder.textView_ingrediente.setText(ing);
    }

    public MancareAdapter(List<Preparat> preparats){
        this.mancareList = preparats;
    }

    @Override
    public int getItemCount() { return mancareList.size(); }

}
