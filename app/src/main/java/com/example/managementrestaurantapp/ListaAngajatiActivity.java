package com.example.managementrestaurantapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.managementrestaurantapp.adapter.AngajatAdapter;
import com.example.managementrestaurantapp.clase.Angajat;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListaAngajatiActivity extends AppCompatActivity {
    private static final int UPDATE_ANGAJAT_CODE_REQUEST=201;

    private List<Angajat> angajati;
    private FirebaseService firebaseService;
    private ListView listViewAng;
    private Angajat user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_angajati);
        initComp();
    }

    private void initComp() {
        Intent preluareUser = getIntent();
        if(preluareUser.getParcelableExtra(("user")) != null){
            user = (Angajat) preluareUser.getParcelableExtra("user");
        }
        angajati = new ArrayList<>();
        listViewAng = findViewById(R.id.lv_angajati);
        AngajatAdapter angajatAdapter = new AngajatAdapter(getApplicationContext(), R.layout.angajat_item_lv,angajati, getLayoutInflater());
        listViewAng.setAdapter(angajatAdapter);
        firebaseService = FirebaseService.getInstance();
        firebaseService.attachDataChangeListner(getAngajati());


        listViewAng.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
               intent.putExtra("angajatKey", angajati.get(position));
               startActivityForResult(intent, UPDATE_ANGAJAT_CODE_REQUEST);
               return true;
           }
       });

        listViewAng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(angajati.get(position).getUsername() != user.getUsername()){
                    firebaseService.deleteAngajat(angajati.get(position));
                    angajati.remove(angajati.get(position));
                    notifyAdapter();
                }else {
                    Toast.makeText(getApplicationContext(),"Nu va puteti elimina din lista angajatilor!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_ANGAJAT_CODE_REQUEST && resultCode == RESULT_OK && data != null){
           Angajat angajat = (Angajat) data.getParcelableExtra("angajatNou");
           firebaseService.upsert(angajat);
           notifyAdapter();
        }
    }

    private Callback<List<Angajat>> getAngajati(){
        return new Callback<List<Angajat>>() {
            @Override
            public void runResultOnUiThread(List<Angajat> result) {
                if(result != null){
                    angajati.clear();
                    angajati.addAll(result);
                    notifyAdapter();
                }
            }
        };
    }

    public void notifyAdapter(){
        AngajatAdapter angajatAdapter = (AngajatAdapter) listViewAng.getAdapter();
        angajatAdapter.notifyDataSetChanged();
    }
}