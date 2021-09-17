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
import com.example.managementrestaurantapp.clase.Masa;
import com.example.managementrestaurantapp.clase.Preparat;

import java.util.List;

public class PreparatComandaAdapter extends ArrayAdapter<Preparat> {

    private Context context;
    private int resource;
    private List<Preparat> preparats;
    private LayoutInflater inflater;

    public PreparatComandaAdapter(@NonNull Context context, int resource, @NonNull List<Preparat> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.preparats=objects;
        this.inflater=inflater;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =inflater.inflate(resource, parent,false);
       Preparat preparat = preparats.get(position);
        if(preparat != null){
            getNumePreparat(view, preparat.getNume_preparat());
            getCantitate(view, preparat.getCantitate());
        }
        return view;
    }

    public void getNumePreparat(View view, String nume){
        TextView textView = view.findViewById(R.id.denumire_preparat);
        populateTextViewContent(textView, nume);
    }

    public void getCantitate(View view, int cantitate){
        TextView textView = view.findViewById(R.id.bucati_preparat);
        populateTextViewContent(textView, "X"+String.valueOf(cantitate));
    }


    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText('-');
        }
    }
}
