package com.example.managementrestaurantapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managementrestaurantapp.adapter.PreparateNotaAdapter;
import com.example.managementrestaurantapp.clase.Ingredient;
import com.example.managementrestaurantapp.clase.Masa;
import com.example.managementrestaurantapp.clase.NotadePlata;
import com.example.managementrestaurantapp.clase.Preparat;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotadePlataActivity extends AppCompatActivity {

    private ListView listViewPreparate;
    private Spinner metodeSpinner;

    private List<Preparat> preparatList;
    private List<Ingredient> camaraList;
    private TextView textTotal;


    private NotadePlata notaDePlataTemp = null;
    private Intent intent;

    private FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notade_plata);

        initComp();
    }

    private void initComp(){
        firebaseService = FirebaseService.getInstance();
        firebaseService.attachAllIngrediente(getCamara());
        Button button_acceseaza_meniu = findViewById(R.id.btn_meniu);
        FloatingActionButton floatingActionButton_nota_check = findViewById(R.id.btn_salveaza_nota);
        FloatingActionButton floatingActionButton_nota_inchisa = findViewById(R.id.btn_inchide_nota);
        textTotal = findViewById(R.id.total_nota);
        intent = getIntent();
        metodeSpinner = findViewById(R.id.spin_metoda_plata);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.metodePlata, android.R.layout.simple_spinner_dropdown_item);
        metodeSpinner.setAdapter(adapter);
        preparatList = new ArrayList<>();
        listViewPreparate = findViewById(R.id.list_item_preparate);
        PreparateNotaAdapter preparateNotaAdapter = new PreparateNotaAdapter(getApplicationContext(), R.layout.preparat_nota_item_lv,preparatList, getLayoutInflater());
        listViewPreparate.setAdapter(preparateNotaAdapter);
        if(intent.hasExtra("masaSelectata")){
            Masa masa = intent.getParcelableExtra("masaSelectata");
            if(masa.getNotadePlata().getTotalMasa() != 0){
                notaDePlataTemp = masa.getNotadePlata();
                notaDePlataTemp.setDataNotaDePlata("-");
                preparatList.clear();
                preparatList.addAll(notaDePlataTemp.getPreparateNotadePlata());
                notifyAdapter();
                textTotal.setText(String.valueOf(notaDePlataTemp.getTotalMasa()));
            }else {
                notaDePlataTemp = new NotadePlata();
                notaDePlataTemp.setMetodaPlata("-");
                notaDePlataTemp.setDataNotaDePlata("-");
                textTotal.setText(String.valueOf(notaDePlataTemp.getTotalMasa()));
            }
        }else {
            if(intent.hasExtra("notaSelectata")){
                Masa masa = intent.getParcelableExtra("notaSelectata");
                if(masa.getNotadePlata().getTotalMasa() != 0){
                    notaDePlataTemp = masa.getNotadePlata();
                    preparatList.clear();
                    preparatList.addAll(notaDePlataTemp.getPreparateNotadePlata());
                    notifyAdapter();
                    textTotal.setText(String.valueOf(notaDePlataTemp.getTotalMasa()));
                }
            }else {
                if(intent.hasExtra("notaSelectataIncasata")){
                    Masa masa = intent.getParcelableExtra("notaSelectataIncasata");
                    if(masa.getNotadePlata().getTotalMasa() != 0){
                        notaDePlataTemp = masa.getNotadePlata();
                        preparatList.clear();
                        preparatList.addAll(notaDePlataTemp.getPreparateNotadePlata());
                        notifyAdapter();
                        textTotal.setText(String.valueOf(notaDePlataTemp.getTotalMasa()));
                        floatingActionButton_nota_check.setVisibility(View.GONE);
                        floatingActionButton_nota_inchisa.setVisibility(View.GONE);
                        button_acceseaza_meniu.setVisibility(View.GONE);
                        ArrayAdapter arrayAdapter= (ArrayAdapter) metodeSpinner.getAdapter();
                        for( int i =0; i<arrayAdapter.getCount(); ++i)
                        {
                            String item = (String) arrayAdapter.getItem(i);
                            if(item != null && item.equals(notaDePlataTemp.getMetodaPlata()))
                            {
                                metodeSpinner.setSelection(i);
                            }
                        }
                        metodeSpinner.setEnabled(false);
                    }
                }
            }
        }

        floatingActionButton_nota_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(notaDePlataTemp.getTotalMasa()>0){
                    int ok = 0;
                    List<Preparat> prepList = notaDePlataTemp.getPreparateNotadePlata();
                    for (Preparat preparat: prepList) {
                        List<Ingredient> ingredientList = preparat.getIngrediente();
                        for (Ingredient ingredient:ingredientList) {
                            for (Ingredient ing:camaraList) {
                                if(ingredient.getNumeIngredient().equals(ing.getNumeIngredient())){
                                    float cantTot = ing.getCantitateTotala();
                                    if(cantTot > 0){
                                        ing.setCantitateTotala(cantTot-ingredient.getCantitateTotala());
                                        ing.setNrBuc(ing.getCantitateTotala()/ing.getCantitatePerBuc());
                                        firebaseService.upsertIngredient(ing);
                                        ok = 1;
                                    }else {
                                        ok = 0;
                                        break;
                                    }
                                }
                            }
                            if(ok == 0){
                                break;
                            }
                        }
                    }
                    if(ok == 1){
                        Intent intentHarta = new Intent(getApplicationContext(), HartaRestaurantActivity.class);
                        intentHarta.putExtra("notaDePlata", notaDePlataTemp);
                        setResult(RESULT_OK, intentHarta);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Nu mai sunt destule ingrediente in camara!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NotadePlataActivity.this);
                    builder.setIcon(R.drawable.ic_baseline_vpn_key_24);
                    builder.setTitle("Nu ati incarcat nici un produs!"+ '\n' +"Doriti ca anulati comanda?");
                    builder.setCancelable(false);
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        button_acceseaza_meniu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MeniuRestaurantActivity.class);
                startActivityForResult(intent1, 100);
            }
        });

        floatingActionButton_nota_inchisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notaDePlataTemp.getTotalMasa()>0){
                    Intent intent1 = new Intent(getApplicationContext(), HartaRestaurantActivity.class);
                    notaDePlataTemp.setMetodaPlata(metodeSpinner.getSelectedItem().toString());
                    Date ziCurenta = Calendar.getInstance().getTime();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String dataNota = simpleDateFormat.format(ziCurenta);
                    notaDePlataTemp.setDataNotaDePlata(dataNota);
                    intent1.putExtra("notaInchisa", notaDePlataTemp);
                    setResult(RESULT_FIRST_USER, intent1);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Nota nu contine produse!", Toast.LENGTH_LONG).show();
                }

            }
        });

        listViewPreparate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotadePlataActivity.this);
                builder.setIcon(R.drawable.ic_baseline_vpn_key_24);
                builder.setTitle("Doriti sa eleminati acest preparat din lista?");
                builder.setCancelable(false);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       preparatList.remove(position);
                       calculTotal();
                       notifyAdapter();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(notaDePlataTemp !=null){
            if(!notaDePlataTemp.getDataNotaDePlata().equals("-")){
                NotadePlataActivity.super.onBackPressed();
                finish();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotadePlataActivity.this);
                builder.setIcon(R.drawable.ic_baseline_vpn_key_24);
                builder.setTitle("Doriti sa va intoarceti la harta?");
                builder.setCancelable(false);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(notaDePlataTemp.getTotalMasa() == 0){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(NotadePlataActivity.this);
                            builder1.setCancelable(false);
                            builder1.setIcon(R.drawable.ic_baseline_vpn_key_24);
                            builder1.setTitle("Nu ati incarcat nici un produs!"+ '\n' +"Doriti ca anulati comanda?");
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder1.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(RESULT_CANCELED, intent);
                                    NotadePlataActivity.super.onBackPressed();
                                    finish();
                                    dialog.cancel();

                                }
                            });

                            AlertDialog alertDialog = builder1.create();
                            alertDialog.show();
                        } else {
                            setResult(RESULT_OK);
                            NotadePlataActivity.super.onBackPressed();
                            finish();
                        }

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }else{
            setResult(RESULT_CANCELED);
            finish();
        }

    }

    private void calculTotal(){
        double suma = 0;
        if(!(preparatList.isEmpty())){
            for (Preparat preparat:preparatList) {
                suma = suma + preparat.calcultotal();
            }
        }
        textTotal.setText(String.valueOf(suma));
        notaDePlataTemp.setTotalMasa(suma);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data != null){
            List<Preparat> preparatsAux = new ArrayList<>(data.getParcelableArrayListExtra("listaPreparateNota"));
            preparatList.addAll(preparatsAux);
            notaDePlataTemp.setPreparateNotadePlata(preparatList);
            calculTotal();
            notifyAdapter();
        }
    }

    private Callback<List<Ingredient>> getCamara(){
        return new Callback<List<Ingredient>>() {
            @Override
            public void runResultOnUiThread(List<Ingredient> result) {
                if(result != null){
                    camaraList = new ArrayList<>(result);
                }
            }
        };
    }

    public void notifyAdapter(){
        PreparateNotaAdapter preparateNotaAdapter = (PreparateNotaAdapter) listViewPreparate.getAdapter();
        preparateNotaAdapter.notifyDataSetChanged();
    }
}