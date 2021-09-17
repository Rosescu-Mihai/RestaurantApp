package com.example.managementrestaurantapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managementrestaurantapp.R;
import com.example.managementrestaurantapp.clase.Ingredient;

import java.util.List;

public class IngredientPreparatAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private int resource;
    private List<Ingredient> ingredientList;
    private LayoutInflater inflater;

    public IngredientPreparatAdapter(@NonNull Context context, int resource, @NonNull List<Ingredient> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.ingredientList=objects;
        this.inflater=inflater;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view =inflater.inflate(resource, parent,false);
        Ingredient ingredient = ingredientList.get(position);
        if(ingredient != null){
            getNumePreparat(view, ingredient.getNumeIngredient());
            getCantitatePerBuc(view, ingredient.getCantitateTotala());
        }
        return view;
    }

    public void getNumePreparat(View view, String nume){
        TextView textView = view.findViewById(R.id.denumire_ingredient_preparat);
        populateTextViewContent(textView, nume);
    }

    public void getCantitatePerBuc(View view, float cantitate){
        TextView textView = view.findViewById(R.id.cantitate_ingredient_preparat);
        populateTextViewContent(textView,String.valueOf(cantitate));
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText('-');
        }
    }
}
