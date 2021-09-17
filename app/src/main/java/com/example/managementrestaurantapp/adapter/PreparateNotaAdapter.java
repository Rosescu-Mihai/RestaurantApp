package com.example.managementrestaurantapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managementrestaurantapp.clase.Preparat;
import com.example.managementrestaurantapp.R;

import java.util.List;

public class PreparateNotaAdapter extends ArrayAdapter<Preparat> {
    private Context context;
    private int resource;
    private List<Preparat> preparatList;
    private LayoutInflater inflater;

    public PreparateNotaAdapter(@NonNull Context context, int resource, @NonNull List<Preparat> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.preparatList=objects;
        this.inflater=inflater;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =inflater.inflate(resource, parent,false);
        Preparat preparat = preparatList.get(position);

        if(preparat != null){
           getNumePreparat(view, preparat.getNume_preparat());
           getCantitate(view, preparat.getCantitate());
           getPretPreparat(view, preparat.calcultotal());
        }
        return view;
    }

    public void getNumePreparat(View view, String nume){
        TextView textView = view.findViewById(R.id.nume_preparat);
        populateTextViewContent(textView, nume);
    }

    public void getCantitate(View view, double cantitate){
        TextView textView = view.findViewById(R.id.cantitate_preparat);
        populateTextViewContent(textView, String.valueOf(cantitate));
    }

    public void getPretPreparat(View view, double pretPreparat){
        TextView textView = view.findViewById(R.id.pret_preparat);
        populateTextViewContent(textView, String.valueOf(pretPreparat));
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText('-');
        }
    }
}
