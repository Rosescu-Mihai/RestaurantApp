package com.example.managementrestaurantapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managementrestaurantapp.R;
import com.example.managementrestaurantapp.clase.Ingredient;
import com.example.managementrestaurantapp.clase.Preparat;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BauturaAdpater extends RecyclerView.Adapter<BauturaAdpater.BauturaViewHolder> {
    private final List<Preparat> bauturaList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
          this.onItemClickListener = onItemClickListener;
    }

    public static class BauturaViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_nume_bautura_meniu;
        public TextView textView_pret_bautura_meniu;
        public TextView textView_ingrediente_bautura;

        public BauturaViewHolder(@NonNull @NotNull View itemView,  final OnItemClickListener onItemClickListener) {
            super(itemView);
            textView_nume_bautura_meniu = itemView.findViewById(R.id.nume_preparat_bautura);
            textView_pret_bautura_meniu = itemView.findViewById(R.id.pret_preparat_bautura);
            textView_ingrediente_bautura = itemView.findViewById(R.id.ingrediente_preparat_bautura);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener !=null){
                        int position = getAdapterPosition();
                        if( position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public BauturaViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meniu_bautura_item, parent, false);
        return new BauturaViewHolder(v, onItemClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull BauturaAdpater.BauturaViewHolder holder, int position) {
        Preparat preparat = bauturaList.get(position);
        holder.textView_nume_bautura_meniu.setText(preparat.getNume_preparat());
        holder.textView_pret_bautura_meniu.setText(String.valueOf(preparat.getPret_unitar()) + " lei");
        StringBuilder ing = new StringBuilder();
        for (Ingredient ingredient:preparat.getIngrediente()) {
            String ingAux = ingredient.getNumeIngredient()+" "+ingredient.getCantitateTotala();
            ing.append(ingAux);
        }
        holder. textView_ingrediente_bautura.setText(ing);
    }

    public BauturaAdpater(List<Preparat> bauturaList){
        this.bauturaList = bauturaList;
    }


    @Override
    public int getItemCount() { return bauturaList.size(); }


}
