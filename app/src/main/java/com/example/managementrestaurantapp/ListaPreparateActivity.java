package com.example.managementrestaurantapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;

import com.example.managementrestaurantapp.adapter.PreparatComandaAdapter;
import com.example.managementrestaurantapp.clase.Preparat;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListaPreparateActivity extends AppCompatActivity {

    private List<Preparat> preparatList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_preparate);
        intializeazaComp();
    }

    private void intializeazaComp() {
        Intent intentPreparate = getIntent();
        preparatList = new ArrayList<>();

        if(intentPreparate.hasExtra("mancare")){
            preparatList.addAll(intentPreparate.getParcelableArrayListExtra("mancare"));
        }else {
            if(intentPreparate.hasExtra("bauturi")){
                preparatList.addAll(intentPreparate.getParcelableArrayListExtra("bauturi"));
            }else {
                if(intentPreparate.hasExtra("ospatar")){
                    preparatList.addAll(intentPreparate.getParcelableArrayListExtra("ospatar"));
                }
            }
        }
        ListView preparateListView = findViewById(R.id.lista_preparate);
        PreparatComandaAdapter preparatComandaAdapter = new PreparatComandaAdapter(getApplicationContext(), R.layout.preparat_comanda_item, preparatList, getLayoutInflater());
        preparateListView.setAdapter(preparatComandaAdapter);
        FloatingActionButton btn_comanda_efectuata = findViewById(R.id.comanda_efectuata);

        btn_comanda_efectuata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ComenzidePreparatActivity.class);
                intent.putParcelableArrayListExtra("listaEfectuata", (ArrayList<? extends Parcelable>) preparatList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListaPreparateActivity.this);
        builder.setIcon(R.drawable.ic_baseline_vpn_key_24);
        builder.setTitle("Ai efectuat comanda?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(getApplicationContext(), ComenzidePreparatActivity.class);
                intent.putParcelableArrayListExtra("listaEfectuata", (ArrayList<? extends Parcelable>) preparatList);
                setResult(RESULT_OK, intent);
                ListaPreparateActivity.super.onBackPressed();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}