package com.example.managementrestaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managementrestaurantapp.adapter.IngredientAdapter;
import com.example.managementrestaurantapp.clase.Factura;
import com.example.managementrestaurantapp.clase.Ingredient;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FacturaActivity extends AppCompatActivity {

    private EditText editTextNume, editTextCantitate, editTextPretBuc, editTextBucati, editTextFurnizor;
    private TextView totalFactura;

    private List<Ingredient> ingredientList;
    private List<Ingredient> ingredientsFireBase;
    private ListView ingredienteListView;
    private Factura facturaSelectata;

    private FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        initElemente();
    }

    @SuppressLint("SetTextI18n")
    private void initElemente() {
        firebaseService = FirebaseService.getInstance();
        firebaseService.attachAllIngrediente(getIngrediente());
        editTextNume = findViewById(R.id.nume_ingredient);
        editTextCantitate = findViewById(R.id.cantitate_bucata);
        editTextPretBuc = findViewById(R.id.pret_bucata);
        editTextBucati = findViewById(R.id.cantitate_totala);
        editTextFurnizor = findViewById(R.id.nume_furnizor);
        TextView textNume = findViewById(R.id.ingredient);
        TextView textCantitate = findViewById(R.id.cnt_per_buc);
        TextView textBucati = findViewById(R.id.nr_bucati);
        TextView textPretBuc = findViewById(R.id.pret_buc);
        TextView textMlKg = findViewById(R.id.ml_kg);
        Button adaugaIngredient = findViewById(R.id.adauga_produs);
        Button incaracaFactura = findViewById(R.id.incaraca_factura);
        ingredienteListView = findViewById(R.id.lista_Factura);
        totalFactura = findViewById(R.id.total_suma_factura);
        Intent intentFactura = getIntent();
        if(intentFactura.hasExtra("facturaVizualizata")){
            facturaSelectata = intentFactura.getParcelableExtra("facturaVizualizata");
            ingredientList = new ArrayList<>(facturaSelectata.getIngredientList());
            adaugaIngredient.setVisibility(View.GONE);
            incaracaFactura.setVisibility(View.GONE);
            editTextNume.setVisibility(View.GONE);
            editTextCantitate.setVisibility(View.GONE);
            editTextPretBuc.setVisibility(View.GONE);
            editTextBucati .setVisibility(View.GONE);
            textNume.setVisibility(View.GONE);
            textCantitate.setVisibility(View.GONE);
            textBucati.setVisibility(View.GONE);
            textPretBuc.setVisibility(View.GONE);
            textMlKg.setVisibility(View.GONE);
            editTextFurnizor.setText(facturaSelectata.getNumeFurnizor());
            editTextFurnizor.setEnabled(false);
            totalFactura.setText("Total: "+String.valueOf(facturaSelectata.getTotalFactura()));
        }else {
            facturaSelectata = new Factura();
            ingredientList = new ArrayList<>();

            ingredienteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ingredientList.remove(ingredientList.get(position));
                    notifyAdapter();
                    return false;
                }
            });
        }
        IngredientAdapter ingredientAdapter = new IngredientAdapter(getApplicationContext(), R.layout.ingredient_item, ingredientList, getLayoutInflater());
        ingredienteListView.setAdapter(ingredientAdapter);

        adaugaIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ingredient ingredient = new Ingredient();
                if(editTextNume.getText() != null && editTextCantitate.getText() != null &&
                      editTextBucati.getText() != null && editTextPretBuc.getText() != null){
                    if(!editTextNume.getText().toString().isEmpty() && !editTextCantitate.getText().toString().isEmpty() &&
                            !editTextBucati.getText().toString().isEmpty() && !editTextPretBuc.getText().toString().isEmpty()){
                        ingredient.setNumeIngredient(editTextNume.getText().toString());
                        ingredient.setCantitatePerBuc(Integer.parseInt(editTextCantitate.getText().toString()));
                        ingredient.setNrBuc(Integer.parseInt(editTextBucati.getText().toString()));
                        ingredient.setPretPerBuc(Integer.parseInt(editTextPretBuc.getText().toString()));
                        ingredient.setCantitateTotala(0);

                        ingredientList.add(ingredient);
                        notifyAdapter();
                        calculFactura();

                        editTextNume.getText().clear();
                        editTextBucati.getText().clear();
                        editTextCantitate.getText().clear();
                        editTextPretBuc.getText().clear();

                    }else {
                        Toast.makeText(getApplicationContext(), "Completaraea spatiilor, privind datele despre produs, sunt obligatorii!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Completaraea spatiilor, privind datele despre produs, sunt obligatorii!", Toast.LENGTH_LONG).show();
                }
            }
        });

        incaracaFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextFurnizor.getText() != null && ingredientList.size() > 0){
                    if(!editTextFurnizor.getText().toString().isEmpty()){
                        facturaSelectata.setNumeFurnizor(editTextFurnizor.getText().toString());
                        String totalAux = totalFactura.getText().toString();
                        String total = totalAux.substring(7);
                        facturaSelectata.setTotalFactura(Integer.parseInt(total));
                        facturaSelectata.setIngredientList(ingredientList);
                        Date ziCurenta = Calendar.getInstance().getTime();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dataNota = simpleDateFormat.format(ziCurenta);
                        facturaSelectata.setDataFactura(dataNota);

                        for (Ingredient ingredient:ingredientList) {
                            if(ingredientsFireBase != null && ingredientsFireBase.size() > 0){
                                int ok = 0;
                                for (Ingredient ing: ingredientsFireBase) {
                                    if(ing.getNumeIngredient().equals(ingredient.getNumeIngredient()) && ing.getPretPerBuc() == ingredient.getPretPerBuc()){
                                        ing.setNrBuc(ing.getNrBuc()+ingredient.getNrBuc());
                                        ing.setCantitateTotala(0);
                                        firebaseService.upsertIngredient(ing);
                                        ok =1;
                                    }
                                }
                                if(ok == 0){
                                    firebaseService.upsertIngredient(ingredient);
                                }
                            }else {
                                firebaseService.upsertIngredient(ingredient);
                            }
                        }

                        firebaseService.upsertFactura(facturaSelectata);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Completaraea furnizorului este obligatorie!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Completaraea furnizorului este obligatorie!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    @SuppressLint("SetTextI18n")
    private void calculFactura(){
        int totalAux2 = facturaSelectata.getTotalFactura();
        totalAux2 = totalAux2 + (Integer.parseInt(editTextPretBuc.getText().toString())*Integer.parseInt(editTextBucati.getText().toString()));
        facturaSelectata.setTotalFactura(totalAux2);
        totalFactura.setText("Total: "+String.valueOf(totalAux2));
    }

    private Callback<List<Ingredient>> getIngrediente(){
        return new Callback<List<Ingredient>>() {
            @Override
            public void runResultOnUiThread(List<Ingredient> result) {
                if(result != null){
                   ingredientsFireBase = new ArrayList<>(result);
                }
            }
        };
    }

    private void notifyAdapter(){
        IngredientAdapter ingredientAdapter = (IngredientAdapter) ingredienteListView.getAdapter();
        ingredientAdapter.notifyDataSetChanged();
    }
}