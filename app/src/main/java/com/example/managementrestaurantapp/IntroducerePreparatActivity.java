package com.example.managementrestaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.managementrestaurantapp.adapter.IngredientPreparatAdapter;
import com.example.managementrestaurantapp.clase.Ingredient;
import com.example.managementrestaurantapp.clase.Preparat;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;

import java.util.ArrayList;
import java.util.List;

public class IntroducerePreparatActivity extends AppCompatActivity {
    private EditText cantitateEditText;
    private ListView ingredienteListView;
    private Spinner tipPreparat, listaIngrediente;
    private EditText pretEditText;

    private List<Ingredient> ingredients;
    private List<String> denumireIngredient;
    private List<Ingredient> ingredientListPreparat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducere_preparat);
        initializareComponente();
    }

    private void initializareComponente() {
        EditText numeEditText = findViewById(R.id.preparat);
        pretEditText = findViewById(R.id.pret_preparat);
        cantitateEditText = findViewById(R.id.ingredient_cantitate);
        ingredienteListView = findViewById(R.id.ingrediente);
        tipPreparat = findViewById(R.id.tip_preparat);
        listaIngrediente = findViewById(R.id.lista_ingrediente);
        Button btnAdaugaPreparat = findViewById(R.id.adg_preparat);
        Button btnPlus = findViewById(R.id.adg_ingredient);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.tipPreparat, android.R.layout.simple_spinner_dropdown_item);
        tipPreparat.setAdapter(adapter);
        ingredientListPreparat = new ArrayList<>();
        IngredientPreparatAdapter ingredientPreparatAdapter = new IngredientPreparatAdapter(getApplicationContext(), R.layout.ingredient_preparat_item, ingredientListPreparat, getLayoutInflater());
        ingredienteListView.setAdapter(ingredientPreparatAdapter);
        FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.attachAllIngrediente(getIngrediente());

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((listaIngrediente.getSelectedItem() != null || !listaIngrediente.getSelectedItem().toString().isEmpty())
                        && !cantitateEditText.getText().toString().isEmpty()){
                    Ingredient ingredient = new Ingredient();
                    ingredient.setNumeIngredient(listaIngrediente.getSelectedItem().toString());
                    ingredient.setCantitateTotala(Integer.parseInt(cantitateEditText.getText().toString()));
                    ingredientListPreparat.add(ingredient);
                    actualizareListaIngrediente();
                }
            }
        });
        btnAdaugaPreparat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!numeEditText.getText().toString().isEmpty() && ingredientListPreparat.size() > 0 && !pretEditText.getText().toString().isEmpty()){
                    Preparat preparat = new Preparat();
                    preparat.setNume_preparat(numeEditText.getText().toString());
                    preparat.setIngrediente(ingredientListPreparat);
                    preparat.setTip_preparat(tipPreparat.getSelectedItem().toString());
                    preparat.setPret_unitar(Integer.parseInt(pretEditText.getText().toString()));
                    firebaseService.upsertPreparatMeniu(preparat);
                    finish();
                }
            }
        });
    }

    private Callback<List<Ingredient>> getIngrediente(){
        return new Callback<List<Ingredient>>() {
            @Override
            public void runResultOnUiThread(List<Ingredient> result) {
                if(result != null){
                    ingredients = new ArrayList<>(result);
                    denumireIngredient = new ArrayList<>();

                    for (Ingredient ingredient: ingredients) {
                        denumireIngredient.add(ingredient.getNumeIngredient());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, denumireIngredient);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    listaIngrediente.setAdapter(arrayAdapter);
                }
            }
        };
    }

    private void actualizareListaIngrediente(){
        IngredientPreparatAdapter adapter = (IngredientPreparatAdapter) ingredienteListView.getAdapter();
        adapter.notifyDataSetChanged();
    }
}