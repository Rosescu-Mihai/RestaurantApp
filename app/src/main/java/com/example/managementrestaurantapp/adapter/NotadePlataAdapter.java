package com.example.managementrestaurantapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managementrestaurantapp.clase.Masa;
import com.example.managementrestaurantapp.clase.NotadePlata;
import com.example.managementrestaurantapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotadePlataAdapter extends RecyclerView.Adapter<NotadePlataAdapter.NotaDePlataViewHolder> {
    private final List<Masa> notaDePlataList;
    private OnNotaListner onNotaListner;
    @NonNull
    @NotNull
    @Override
    public NotaDePlataViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nota_de_plata_item, parent, false);
        return new NotaDePlataViewHolder(v, onNotaListner);
    }

    public NotadePlataAdapter(List<Masa> notaDePlataList, OnNotaListner onNotaListner){
        this.notaDePlataList=notaDePlataList;
        this.onNotaListner = onNotaListner;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotadePlataAdapter.NotaDePlataViewHolder holder, int position) {
           Masa notaDePlataCurenta = notaDePlataList.get(position);
           holder.textView2.setText(String.valueOf(notaDePlataCurenta.getNotadePlata().getTotalMasa()));
           holder.textView1.setText("Nr. masa: "+String.valueOf(notaDePlataCurenta.getNrMasa()));
    }

    @Override
    public int getItemCount() {
        return notaDePlataList.size();
    }

    public static class NotaDePlataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView1;
        public TextView textView1;
        public TextView textView2;
        OnNotaListner onNotaListner;

        public NotaDePlataViewHolder(@NonNull @NotNull View itemView, OnNotaListner onNotaListner)  {
            super(itemView);
            imageView1=itemView.findViewById(R.id.nota_img);
            textView1=itemView.findViewById(R.id.nr_masa_id);
            textView2=itemView.findViewById(R.id.total_plata_id);
            this.onNotaListner = onNotaListner;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onNotaListner.onClickNota(getAdapterPosition());
        }
    }

    public interface OnNotaListner{
        void onClickNota(int position);
    }
}
