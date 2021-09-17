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
import com.example.managementrestaurantapp.clase.Angajat;
import java.util.List;

public class AngajatAdapter extends ArrayAdapter<Angajat> {
    private Context context;
    private int resource;
    private List<Angajat> angajatList;
    private LayoutInflater inflater;

    public AngajatAdapter(@NonNull Context context, int resource, @NonNull List<Angajat> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.angajatList=objects;
        this.inflater=inflater;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =inflater.inflate(resource, parent,false);
        Angajat angajat = angajatList.get(position);
        if(angajat != null){
           getNumeAngajat(view, angajat.getNume());
           getPrenumeAngajat(view, angajat.getPrenume());
           getFunctieAngajat(view, angajat.getFunctie());
        }
        return view;
    }

    public void getNumeAngajat(View view, String nume){
        TextView textView = view.findViewById(R.id.nume_angajat_lista);
        populateTextViewContent(textView, nume);
    }

    public void getPrenumeAngajat(View view, String prenume){
        TextView textView = view.findViewById(R.id.prenume_angajat);
        populateTextViewContent(textView, prenume);
    }

    public void getFunctieAngajat(View view, String functie){
        TextView textView = view.findViewById(R.id.functie_angajat);
        populateTextViewContent(textView, functie);
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText('-');
        }
    }
}
