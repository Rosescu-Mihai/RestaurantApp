package com.example.managementrestaurantapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managementrestaurantapp.R;
import com.example.managementrestaurantapp.clase.Factura;
import com.example.managementrestaurantapp.clase.Masa;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.FacturiViewHolder>{
    private final List<Factura> facturaList;
    private final FacturaAdapter.OnNotaListner onNotaListner;
    @NonNull
    @NotNull
    @Override
    public FacturaAdapter.FacturiViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.factura_item, parent, false);
        return new FacturaAdapter.FacturiViewHolder(v, onNotaListner);
    }

    public FacturaAdapter(List<Factura> facturaList, FacturaAdapter.OnNotaListner onNotaListner){
        this.facturaList = facturaList;
        this.onNotaListner = onNotaListner;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FacturaAdapter.FacturiViewHolder holder, int position) {
        Factura factura = facturaList.get(position);
        holder.textView2.setText(String.valueOf(factura.getTotalFactura()));
        holder.textView1.setText(factura.getNumeFurnizor());
    }

    @Override
    public int getItemCount() {
        return facturaList.size();
    }

    public static class FacturiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView1;
        public TextView textView1;
        public TextView textView2;
        FacturaAdapter.OnNotaListner onNotaListner;

        public FacturiViewHolder(@NonNull @NotNull View itemView, FacturaAdapter.OnNotaListner onNotaListner)  {
            super(itemView);
            imageView1=itemView.findViewById(R.id.nota_img);
            textView1=itemView.findViewById(R.id.furnizor);
            textView2=itemView.findViewById(R.id.total_plata_factura);
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
