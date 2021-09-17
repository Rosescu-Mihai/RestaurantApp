package com.example.managementrestaurantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managementrestaurantapp.adapter.NotadePlataAdapter;
import com.example.managementrestaurantapp.clase.Angajat;
import com.example.managementrestaurantapp.clase.Masa;
import com.example.managementrestaurantapp.clase.NotadePlata;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class IncasariActivity extends AppCompatActivity implements NotadePlataAdapter.OnNotaListner {

    private RecyclerView noteIncasateViewer;
    private List<Angajat> angajatList;
    private List<Masa> notadePlataList;
    private List<Masa> noteAfisate;
    private FloatingActionButton btn_cauta;
    private TextView sumaCard, sumaNumerar;
    private EditText dataIntrodusa;
    private double totalCard, totalCash;

    private FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incasari);
        initComponente();
    }

    private void initComponente() {
        btn_cauta = findViewById(R.id.btn_cauta);
        dataIntrodusa = findViewById(R.id.data_inscrisa);
        sumaCard = findViewById(R.id.suma_card);
        sumaNumerar = findViewById(R.id.suma_cash);
        angajatList = new ArrayList<>();
        notadePlataList = new ArrayList<>();
        noteAfisate = new ArrayList<>();
        firebaseService = FirebaseService.getInstance();
        firebaseService.attachDataChangeListner(getAngajati());
        afisareListaNote();

        btn_cauta.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateFormat.setLenient(false);
                
                    try {
                        dateFormat.parse(dataIntrodusa.getText().toString());
                        for (Masa masa:notadePlataList) {
                            if(masa.getNotadePlata().getDataNotaDePlata().equals(dataIntrodusa.getText().toString())){
                                noteAfisate.add(masa);
                                if(masa.getNotadePlata().getMetodaPlata().equals("Card")){
                                    totalCard = totalCard+masa.getNotadePlata().getTotalMasa();
                                }else {
                                    totalCash = totalCash+masa.getNotadePlata().getTotalMasa();
                                }
                            }
                        }
                        if(noteAfisate.size() == 0){
                            Toast.makeText(getApplicationContext(), "Nu s-au incasat note de plata in aceasta data.", Toast.LENGTH_SHORT).show();
                        }else {
                            notifyAdapter();
                            sumaCard.setText("Suma card: "+String.valueOf(totalCard));
                            sumaNumerar.setText("Suma numerar: "+String.valueOf(totalCash));
                        }
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Data nu are formatul corespunzator!", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void afisareListaNote() {
        noteIncasateViewer = findViewById(R.id.lista_note);
        noteIncasateViewer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new NotadePlataAdapter(noteAfisate, this);
        noteIncasateViewer.setLayoutManager(layoutManager);
        noteIncasateViewer.setAdapter(adapter);
    }

    @Override
    public void onClickNota(int position) {
        Intent intentNota = new Intent(getApplicationContext(), NotadePlataActivity.class);
        intentNota.putExtra("notaSelectataIncasata", notadePlataList.get(position));
        startActivity(intentNota);
    }

    private void getMesefromDB() {
        notadePlataList.clear();
        for (Angajat angajat : angajatList) {
            if (angajat.getMese() != null) {
                notadePlataList.addAll(angajat.getMese());
            }
        }
    }

    private Callback<List<Angajat>> getAngajati () {
        return new Callback<List<Angajat>>() {
            @Override
            public void runResultOnUiThread(List<Angajat> result) {
                if (result != null) {
                    angajatList.clear();
                    angajatList.addAll(result);
                    getMesefromDB();
                }
            }
        };
    }

    public void notifyAdapter(){
        NotadePlataAdapter notadePlataAdapter = (NotadePlataAdapter) noteIncasateViewer.getAdapter();
        notadePlataAdapter.notifyDataSetChanged();
    }
}