package com.example.managementrestaurantapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managementrestaurantapp.R;
import com.example.managementrestaurantapp.clase.Comanda;
import com.example.managementrestaurantapp.clase.Masa;

import java.util.List;

public class ComenziAdapter extends ArrayAdapter<Comanda> {
    private Context context;
    private int resource;
    private List<Comanda> comandasList;
    private LayoutInflater inflater;

    public ComenziAdapter(@NonNull Context context, int resource, @NonNull List<Comanda> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.comandasList=objects;
        this.inflater=inflater;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =inflater.inflate(resource, parent,false);
        Comanda comanda = comandasList.get(position);
        if(comanda != null){
            getNumeAngajat(view, comanda.getNumeAngajat());
            getNrMasa(view, comanda.getNrmMasa());
        }
        return view;
    }

    public void getNumeAngajat(View view, String nume){
        TextView textView = view.findViewById(R.id.angajat_comanda);
        populateTextViewContent(textView, nume);
    }

    public void getNrMasa(View view, int nrMasa){
        TextView textView = view.findViewById(R.id.masa);
        populateTextViewContent(textView, "Nr. masa: "+String.valueOf(nrMasa));
    }


    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText('-');
        }
    }
}
