package com.example.managementrestaurantapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.managementrestaurantapp.adapter.ComenziAdapter;
import com.example.managementrestaurantapp.clase.Angajat;
import com.example.managementrestaurantapp.clase.Comanda;
import com.example.managementrestaurantapp.clase.Masa;
import com.example.managementrestaurantapp.clase.NotadePlata;
import com.example.managementrestaurantapp.clase.Preparat;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ComenzidePreparatActivity extends AppCompatActivity {
    private final int COMANDA_EFECTUATA = 100;

    private List<Comanda> comenziListBar;
    private List<Comanda> comenziListMancare;
    private List<Comanda> comenziListOspatar;
    private List<Angajat> angajatList;
    private List<Masa> meseList;

    private Comanda comandaAlesa;
    private Masa masaAleasa;
    private Angajat angajatAles;
    private Angajat user;
    private Boolean esteBarman;

    private FirebaseService firebaseService;

    private ListView comenziListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comenzide_preparat);
        initComp();
    }

    private void initComp() {
        Intent intentAngajat = getIntent();
        if(intentAngajat.hasExtra("angajatUser")){
            user = intentAngajat.getParcelableExtra("angajatUser");
        }

        angajatList = new ArrayList<>();
        comenziListBar = new ArrayList<>();
        comenziListMancare = new ArrayList<>();
        comenziListOspatar = new ArrayList<>();
        meseList = new ArrayList<>();
        firebaseService = FirebaseService.getInstance();
        comenziListView = findViewById(R.id.lista_comenzi);
        FloatingActionButton refresh_btn = findViewById(R.id.rencarca_comenzi);
        if(user.getFunctie().equals("barman")){
            ComenziAdapter comenziAdapter = new ComenziAdapter(getApplicationContext(), R.layout.comanda_neefctuata, comenziListBar, getLayoutInflater());
            comenziListView.setAdapter(comenziAdapter);
            esteBarman = true;
        }else {
            if(user.getFunctie().equals("bucatar")){
                ComenziAdapter comenziAdapter = new ComenziAdapter(getApplicationContext(), R.layout.comanda_neefctuata, comenziListMancare, getLayoutInflater());
                comenziListView.setAdapter(comenziAdapter);
                esteBarman = false;
            }else {
                if(user.getFunctie().equals("ospatar")){
                    ComenziAdapter comenziAdapter = new ComenziAdapter(getApplicationContext(), R.layout.comanda_neefctuata, comenziListOspatar, getLayoutInflater());
                    comenziListView.setAdapter(comenziAdapter);
                }
            }
        }

        firebaseService.attachDataChangeListner(getAngajati());
        firebaseService.attachAllComenzi(getComenzi());

        comenziListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(esteBarman != null){
                    if(esteBarman){
                        comandaAlesa = comenziListBar.get(position);
                    }else {
                        comandaAlesa = comenziListMancare.get(position);
                    }
                }else {
                    comandaAlesa = comenziListOspatar.get(position);
                }
                     resetareAngajati();

                Intent intentPreparate = new Intent(getApplicationContext(), ListaPreparateActivity.class);
                if(user.getFunctie().equals("barman")){
                    if(comenziListBar.get(position).getPreparateDeEfectuat() != null){
                        intentPreparate.putParcelableArrayListExtra("bauturi", (ArrayList<? extends Parcelable>) comenziListBar.get(position).getPreparateDeEfectuat());
                    }
                } else {
                    if(user.getFunctie().equals("bucatar")){
                        if(comenziListMancare.get(position).getPreparateDeEfectuat() != null){
                            intentPreparate.putParcelableArrayListExtra("mancare", (ArrayList<? extends Parcelable>) comenziListMancare.get(position).getPreparateDeEfectuat());
                        }
                    }else {
                        if(user.getFunctie().equals("ospatar")){
                            if(comenziListOspatar.get(position).getPreparateDeEfectuat() != null){
                                intentPreparate.putParcelableArrayListExtra("ospatar", (ArrayList<? extends Parcelable>) comenziListOspatar.get(position).getPreparateDeEfectuat());
                            }
                        }
                    }
                }
                startActivityForResult(intentPreparate, COMANDA_EFECTUATA);

            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getFunctie().equals("barman")){
                    comenziListBar.clear();
                    notifyAdapter();
                }else {
                    if(user.getFunctie().equals("bucatar")){
                        comenziListMancare.clear();
                        notifyAdapter();
                    }else {
                        if(user.getFunctie().equals("ospatar")){
                            comenziListOspatar.clear();
                            notifyAdapter();
                        }
                    }
                }
                firebaseService.attachDataChangeListner(getAngajati());
                firebaseService.attachAllComenzi(getComenzi());
                notifyAdapter();
            }
        });

    }

    private void resetareAngajati(){
        firebaseService.attachDataChangeListner(getAngajati());
        for (Angajat angajat: angajatList) {
            if(angajat.getMese() != null){
                List<Masa> meseAng = angajat.getMese();
                if(angajat.getMese() != null){
                    for (Masa masa: meseAng) {
                        if(comandaAlesa.getNrmMasa() == masa.getNrMasa()){
                            masaAleasa = masa;
                            angajatAles = angajat;
                            break;
                        }
                    }
                    if(angajatAles != null){
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == COMANDA_EFECTUATA && resultCode == RESULT_OK && data != null){
            resetareAngajati();
            List<Preparat> preparats = data.getParcelableArrayListExtra("listaEfectuata");
            List<Preparat> auxPrep = masaAleasa.getNotadePlata().getPreparateNotadePlata();
            for (Preparat preparatAux: preparats) {
                for (Preparat preparat:auxPrep) {
                    if(preparat.getNume_preparat().equals(preparatAux.getNume_preparat()) && preparat.getCantitate() == preparatAux.getCantitate() &&
                            !preparatAux.isEfectuat() && preparat.getPozitieInNota() == preparatAux.getPozitieInNota()){
                        preparat.setEfectuat(true);
                        break;
                    }
                }
            }
            firebaseService.upsertPreparatNota(auxPrep, masaAleasa.getNotadePlata(), masaAleasa, angajatAles);
            if(esteBarman != null){
                if(esteBarman){
                    firebaseService.upsertComanda(comandaAlesa);
                    comenziListBar.remove(comandaAlesa);
                }else{
                    firebaseService.upsertComanda(comandaAlesa);
                    comenziListMancare.remove(comandaAlesa);
                }
            }else {
                if(user.getFunctie().equals("ospatar")){
                    comandaAlesa.setLivrata(true);
                    firebaseService.upsertComanda(comandaAlesa);
                    comenziListOspatar.remove(comandaAlesa);
                }
            }
            notifyAdapter();
        }
    }

    private Callback<List<Angajat>> getAngajati(){
        return new Callback<List<Angajat>>() {
            @Override
            public void runResultOnUiThread(List<Angajat> result) {
                if(result != null){
                    angajatList.clear();
                    angajatList.addAll(result);
                    getMesefromDB();
                    notifyAdapter();
                }
            }
        };
    }

    private Callback<List<Comanda>> getComenzi(){
        return new Callback<List<Comanda>>() {
            @Override
            public void runResultOnUiThread(List<Comanda> result) {
                if(result != null){
                    List<Comanda> comandaAux = new ArrayList<>(result);
                    comenziListOspatar.clear();
                    for (Comanda comanda:comandaAux) {
                        if(!comanda.isLivrata()){
                            comenziListOspatar.add(comanda);
                        }
                    }
                    notifyAdapter();
                }
            }
        };
    }

    private void getMesefromDB(){
        if(comenziListBar != null){
            comenziListBar.clear();
        }
        if(comenziListMancare != null){
            comenziListMancare.clear();
        }

        if(meseList != null){
            meseList.clear();
        }
        for (Angajat angajat:angajatList) {
            if(angajat.getMese() != null){
                for (Masa masa: angajat.getMese()) {
                    if(masa.getNotadePlata().getMetodaPlata() == null || masa.getNotadePlata().getMetodaPlata().equals("-")){
                        masa.setNumeAngajat(angajat.getPrenume());
                        meseList.add(masa);
                    }
                }
            }
        }
        List<Preparat> mancareList = new ArrayList<>();
        List<Preparat> bauturaList = new ArrayList<>();
        for (Masa masa:meseList) {
            NotadePlata notadePlata = masa.getNotadePlata();
            if(masa.getNotadePlata() != null){
                if(notadePlata.getPreparateNotadePlata() != null){
                    List<Preparat> preparatAux = notadePlata.getPreparateNotadePlata();
                    for (Preparat preparat:preparatAux) {
                        if(!preparat.isEfectuat()){
                            if(preparat.getTip_preparat().equals("mancare")){
                                mancareList.add(preparat);
                            }else{
                                    bauturaList.add(preparat);
                            }
                        }
                    }
                    Comanda comandaBautura = new Comanda(masa.getNumeAngajat(), masa.getNrMasa());
                    comandaBautura.setLivrata(false);
                    if(bauturaList.size() != 0){
                        List<Preparat> auxB = new ArrayList<>(bauturaList);
                        comandaBautura.setPreparateDeEfectuat(auxB);
                        comenziListBar.add(comandaBautura);
                    }
                    Comanda comandaMancare = new Comanda(masa.getNumeAngajat(), masa.getNrMasa());
                    comandaMancare.setLivrata(false);
                    if(mancareList.size() != 0){
                        List<Preparat> auxM = new ArrayList<>(mancareList);
                        comandaMancare.setPreparateDeEfectuat(auxM);
                        comenziListMancare.add(comandaMancare);
                    }

                    bauturaList.clear();
                    mancareList.clear();
                }
            }
        }
    }

    public void notifyAdapter(){
        ComenziAdapter comandaAdapter = (ComenziAdapter) comenziListView.getAdapter();
        comandaAdapter.notifyDataSetChanged();
    }
}