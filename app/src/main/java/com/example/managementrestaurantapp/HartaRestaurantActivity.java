package com.example.managementrestaurantapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.managementrestaurantapp.adapter.NotadePlataAdapter;
import com.example.managementrestaurantapp.clase.Angajat;
import com.example.managementrestaurantapp.clase.Masa;
import com.example.managementrestaurantapp.clase.NotadePlata;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HartaRestaurantActivity extends AppCompatActivity implements NotadePlataAdapter.OnNotaListner {
    private final static int ADD_NOTA_NOUA = 201;
    private final static int CHANGE_NOTA = 202;
    private List<Masa> listaMese;
    private List<Angajat> angajatList;
    private List<Masa> noteDePlataAngajat;

    private ImageView imageView_drept_1, imageView_bar_1,
            imageView_drept_2, imageView_bar_2, imageView_drept_3, imageView_drept_4, imageView_drept_5,
            imageview_rotund_1, imageview_rotund_2, imageview_rotund_3, imageview_rotund_4, imageview_rotund_5;

    private List<ImageView> meseHarta;
    private Angajat user;
    private Masa masaUser;

    private FirebaseService firebaseService;
    private RecyclerView noteDePlataViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harta_restaurant);
        initComp();
    }

    private void initComp() {
        masaUser = new Masa();
        user = new Angajat();
        meseHarta = new ArrayList<>();
        listaMese = new ArrayList<>();
        noteDePlataAngajat = new ArrayList<>();
        angajatList = new ArrayList<>();
        initializareHarta();
        actualizareHarta();
        firebaseService = FirebaseService.getInstance();
        firebaseService.attachDataChangeListner(getAngajati());
        Intent intent_user = getIntent();
        if(intent_user.hasExtra("angajatCheie")){
            user = (Angajat) intent_user.getParcelableExtra("angajatCheie");
        }

        afisareListaNote();

        for(int i = 0; i < meseHarta.size(); ++i){
            meseHarta.get(i).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onClick(View v) {
                    masaUser = new Masa();
                    masaUser.setEsteLibera(false);
                    masaUser.setNrMasa(meseHarta.indexOf(v));
                    masaUser.setNotadePlata(new NotadePlata());
                    for (Masa masa1:listaMese) {
                        if(masaUser.getNrMasa() == masa1.getNrMasa()){
                            masaUser.setIdMasa(masa1.getIdMasa());
                            if(masaUser.getNotadePlata() != null){
                                masaUser.setNotadePlata(masa1.getNotadePlata());
                            }
                        }
                    }
                    for (ImageView imageView : meseHarta) {
                        if (imageView == v) {
                            if (imageView.getId() == R.id.masa_drepthugiulara_1 || imageView.getId() == R.id.masa_drepthugiulara_2 ||
                                    imageView.getId() == R.id.masa_drepthugiulara_3 || imageView.getId() == R.id.masa_drepthugiulara_4 ||
                                    imageView.getId() == R.id.masa_drepthugiulara_5)
                                masaUser.setTipMasa("masa dreptunghiulara");
                            else {
                                if (imageView.getId() == R.id.scaun_bar_1 || imageView.getId() == R.id.scaun_bar_2)
                                    masaUser.setTipMasa("scaun bar");
                                else {
                                    masaUser.setTipMasa("masa rotunda");
                                }
                            }
                        }
                    }
                    Intent intent = new Intent(getApplicationContext(), NotadePlataActivity.class);
                    intent.putExtra("masaSelectata", masaUser);
                    if(masaUser.getNotadePlata().getIdNota() == null){
                        if(meseHarta.get(masaUser.getNrMasa()).getTag() != null){
                            if((Integer) meseHarta.get(masaUser.getNrMasa()).getTag() == R.drawable.masa_dreptunghiulara_2 ||
                                    (Integer)meseHarta.get(masaUser.getNrMasa()).getTag() == R.drawable.masa_rotunda_2  ||
                                    (Integer) meseHarta.get(masaUser.getNrMasa()).getTag() == R.drawable.scaun_bar_2 ){

                                Toast.makeText(getApplicationContext(), "Doar angajatul care a creat nota, o poate accesa!", Toast.LENGTH_SHORT).show();
                            }else{
                                firebaseService.upsertMasa(masaUser, user);
                                listaMese.add(masaUser);
                                actualizareHarta();
                                startActivityForResult(intent, ADD_NOTA_NOUA);
                            }
                        }

                    }else{
                        int ok = 0;
                        for (Angajat angajat:angajatList) {
                            if(angajat.getMese() != null){
                                List<Masa> lista = new ArrayList<>(angajat.getMese());
                                for (Masa masa: lista) {
                                    if(masaUser.getIdMasa().equals(masa.getIdMasa())){
                                        if(angajat.getId().equals(user.getId())){
                                            ok = 1;
                                            break;
                                        }
                                    }
                                }

                                if(ok == 1){
                                    break;
                                }
                            }
                        }

                        if(ok == 1){
                            startActivityForResult(intent, CHANGE_NOTA);
                        }else{
                            Toast.makeText(getApplicationContext(), "Doar angajatul care a creat nota, o poate accesa!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        FloatingActionButton refreshHartaBtn = findViewById(R.id.refresh_harta);
        refreshHartaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               initComp();
            }
        });

    }

    private void redesenareLoc(Masa masa){
        if(masa != null){
            if(meseHarta.get(masa.getNrMasa()) != null && masa.getTipMasa() != null){
                if(masa.getTipMasa().equals("masa rotunda")){
                    meseHarta.get(masa.getNrMasa()).setTag(R.drawable.masa_rotunda_1);
                    meseHarta.get(masa.getNrMasa()).setImageResource(R.drawable.masa_rotunda_1);
                }else {
                    if(masa.getTipMasa().equals("scaun bar")){
                        meseHarta.get(masa.getNrMasa()).setTag(R.drawable.scaun_bar_1);
                        meseHarta.get(masa.getNrMasa()).setImageResource(R.drawable.scaun_bar_1);
                    }else {
                        if(masa.getTipMasa().equals("masa dreptunghiulara")){
                            meseHarta.get(masa.getNrMasa()).setTag(R.drawable.masa_dreptunghiulara_1);
                            meseHarta.get(masa.getNrMasa()).setImageResource(R.drawable.masa_dreptunghiulara_1);
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NOTA_NOUA && resultCode == RESULT_OK && data != null){
           NotadePlata notadePlata = (NotadePlata) data.getParcelableExtra("notaDePlata");
           firebaseService.upsertNotadePlata(notadePlata, masaUser, user);
           noteDePlataAngajat.add(masaUser);
           notifyAdapter();
        }else {
            if(resultCode == RESULT_CANCELED){
               firebaseService.deleteMasa(masaUser, user);
               redesenareLoc(masaUser);
               listaMese.remove(masaUser);
               noteDePlataAngajat.remove(masaUser);
               notifyAdapter();
            }else {
                if(requestCode == CHANGE_NOTA && resultCode == RESULT_OK && data != null){
                    firebaseService.upsertMasa(masaUser, user);
                    NotadePlata notadePlata = (NotadePlata) data.getParcelableExtra("notaDePlata");
                    for (Masa masa:noteDePlataAngajat) {
                        if(masa.getNrMasa() == masaUser.getNrMasa()){
                            masa.setNotadePlata(notadePlata);
                        }
                    }
                    firebaseService.upsertNotadePlata(notadePlata, masaUser, user);
                    notifyAdapter();
                }else{
                    if(resultCode == RESULT_FIRST_USER && data != null){
                        NotadePlata notadePlata = data.getParcelableExtra("notaInchisa");
                        firebaseService.upsertNotadePlata(notadePlata, masaUser, user);
                        redesenareLoc(masaUser);
                        listaMese.remove(masaUser);
                        noteDePlataAngajat.remove(masaUser);
                        notifyAdapter();
                    }
                }
            }
        }
    }

    private void afisareListaNote(){
        noteDePlataViewer = findViewById(R.id.lista_noteDePlata_angajat);
        noteDePlataViewer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new NotadePlataAdapter(noteDePlataAngajat, this);
        noteDePlataViewer.setLayoutManager(layoutManager);
        noteDePlataViewer.setAdapter(adapter);
    }

    private void initializareHarta(){
        imageView_drept_1 = findViewById(R.id.masa_drepthugiulara_1);
        imageView_drept_1.setTag(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_1.setImageResource(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_2 = findViewById(R.id.masa_drepthugiulara_2);
        imageView_drept_2.setTag(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_2.setImageResource(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_3 = findViewById(R.id.masa_drepthugiulara_3);
        imageView_drept_3.setImageResource(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_3.setTag(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_4 = findViewById(R.id.masa_drepthugiulara_4);
        imageView_drept_4.setTag(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_4.setImageResource(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_5 = findViewById(R.id.masa_drepthugiulara_5);
        imageView_drept_5.setTag(R.drawable.masa_dreptunghiulara_1);
        imageView_drept_5.setImageResource(R.drawable.masa_dreptunghiulara_1);
        imageView_bar_1 = findViewById(R.id.scaun_bar_1);
        imageView_bar_1.setImageResource(R.drawable.scaun_bar_1);
        imageView_bar_1.setTag(R.drawable.scaun_bar_1);
        imageView_bar_2 = findViewById(R.id.scaun_bar_2);
        imageView_bar_2.setTag(R.drawable.scaun_bar_1);
        imageView_bar_2.setImageResource(R.drawable.scaun_bar_1);
        imageview_rotund_1 = findViewById(R.id.masa_rotunda_1);
        imageview_rotund_1.setTag(R.drawable.masa_rotunda_1);
        imageview_rotund_1.setImageResource(R.drawable.masa_rotunda_1);
        imageview_rotund_2 = findViewById(R.id.masa_rotunda_2);
        imageview_rotund_2.setTag(R.drawable.masa_rotunda_1);
        imageview_rotund_2.setImageResource(R.drawable.masa_rotunda_1);
        imageview_rotund_3 = findViewById(R.id.masa_rotunda_3);
        imageview_rotund_3.setTag(R.drawable.masa_rotunda_1);
        imageview_rotund_3.setImageResource(R.drawable.masa_rotunda_1);
        imageview_rotund_4 = findViewById(R.id.masa_rotunda_4);
        imageview_rotund_4.setTag(R.drawable.masa_rotunda_1);
        imageview_rotund_4.setImageResource(R.drawable.masa_rotunda_1);
        imageview_rotund_5 = findViewById(R.id.masa_rotunda_5);
        imageview_rotund_5.setTag(R.drawable.masa_rotunda_1);
        imageview_rotund_5.setImageResource(R.drawable.masa_rotunda_1);
        creareListeMese();
    }

    private void creareListeMese() {
        meseHarta.add(imageView_bar_1);
        meseHarta.add(imageView_bar_2);
        meseHarta.add(imageview_rotund_1);
        meseHarta.add(imageview_rotund_2);
        meseHarta.add(imageview_rotund_3);
        meseHarta.add(imageview_rotund_4);
        meseHarta.add(imageview_rotund_5);
        meseHarta.add(imageView_drept_1);
        meseHarta.add(imageView_drept_2);
        meseHarta.add(imageView_drept_3);
        meseHarta.add(imageView_drept_4);
        meseHarta.add(imageView_drept_5);
    }


    private void actualizareHarta(){
        for(int i = 0; i < listaMese.size(); ++i){
            if(listaMese.get(i).getTipMasa().equals("scaun bar")){
                meseHarta.get(listaMese.get(i).getNrMasa()).setTag(R.drawable.scaun_bar_2);
                meseHarta.get(listaMese.get(i).getNrMasa()).setImageResource(R.drawable.scaun_bar_2);
            }else {
                if(listaMese.get(i).getTipMasa().equals("masa dreptunghiulara")){
                    meseHarta.get(listaMese.get(i).getNrMasa()).setTag(R.drawable.masa_dreptunghiulara_2);
                    meseHarta.get(listaMese.get(i).getNrMasa()).setImageResource(R.drawable.masa_dreptunghiulara_2);
                }else{
                    if(listaMese.get(i).getTipMasa().equals("masa rotunda")) {
                        meseHarta.get(listaMese.get(i).getNrMasa()).setTag(R.drawable.masa_rotunda_2);
                        meseHarta.get(listaMese.get(i).getNrMasa()).setImageResource(R.drawable.masa_rotunda_2);
                    }
                }
            }
        }
    }

    private void getMesefromDB(){
        listaMese.clear();
        noteDePlataAngajat.clear();
        for (Angajat angajat:angajatList) {
            if(angajat.getMese() != null){
                for (Masa masa: angajat.getMese()) {
                    if(masa.getNotadePlata().getMetodaPlata() == null || masa.getNotadePlata().getMetodaPlata().equals("-")){
                        listaMese.add(masa);
                        if(angajat.getUsername() == user.getUsername()){
                            noteDePlataAngajat.add(masa);
                        }
                    }
                }
            }
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
                    actualizareHarta();
                    notifyAdapter();
                }
            }
        };
    }

    public void notifyAdapter(){
        NotadePlataAdapter notadePlataAdapter = (NotadePlataAdapter) noteDePlataViewer.getAdapter();
        notadePlataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickNota(int position) {
        Intent intentNota = new Intent(getApplicationContext(), NotadePlataActivity.class);
        masaUser = noteDePlataAngajat.get(position);
        intentNota.putExtra("notaSelectata", noteDePlataAngajat.get(position));
        startActivityForResult(intentNota, CHANGE_NOTA);
    }
}