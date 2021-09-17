package com.example.managementrestaurantapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.example.managementrestaurantapp.clase.Preparat;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.example.managementrestaurantapp.fragmente.BauturaFragment;
import com.example.managementrestaurantapp.fragmente.MancareFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MeniuRestaurantActivity extends AppCompatActivity {
    private Button btn_mancare, btn_bautura;
    private List<Preparat> list = new ArrayList<>();
    private int pozitiePreparatInNota = 0;
    private List<Preparat> bauturameniuList;
    private List<Preparat> mancaremeniuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meniu_restaurant);
        initComponents();
    }

    private void initComponents() {
        bauturameniuList = new ArrayList<>();
        mancaremeniuList = new ArrayList<>();
        btn_bautura = findViewById(R.id.btn_mn_bauturi);
        btn_mancare = findViewById(R.id.btn_mn_mancare);
        FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.attachPreparateMeniu(getBauturi());

        btn_mancare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MancareFragment mancareFragment = new MancareFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle data = new Bundle();
                data.putParcelableArrayList("mancareList", (ArrayList<? extends Parcelable>) mancaremeniuList);
                mancareFragment.setArguments(data);
                fragmentTransaction.replace(R.id.frame_layout_mn, mancareFragment);
                fragmentTransaction.commit();
            }
        });

        btn_bautura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BauturaFragment bauturaFragment = new BauturaFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle data = new Bundle();
                data.putParcelableArrayList("bauturaList", (ArrayList<? extends Parcelable>) bauturameniuList);
                bauturaFragment.setArguments(data);
                fragmentTransaction.replace(R.id.frame_layout_mn, bauturaFragment);
                fragmentTransaction.commit();
            }
        });
    }

    public void adaugainNota(Preparat preparat){
        preparat.setPozitieInNota(pozitiePreparatInNota);
        list.add(preparat);
        pozitiePreparatInNota++;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void prelucrareLista(List<Preparat> preparatList){
        List<Preparat> preparats = preparatList.stream().distinct().collect(Collectors.toList());
        for (Preparat preparat: preparats) {
            preparat.setCantitate(Collections.frequency(preparatList, preparat));
        }
        preparatList.clear();
        preparatList.addAll(preparats);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBackPressed() {
        if(!list.isEmpty()){
            prelucrareLista(list);
            Intent intent = getIntent();
            intent.putParcelableArrayListExtra("listaPreparateNota", (ArrayList<? extends Parcelable>) list);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
            finish();
        }else
        {
            super.onBackPressed();
            finish();
        }

    }
    private Callback<List<Preparat>> getBauturi(){
        return new Callback<List<Preparat>>() {
            @Override
            public void runResultOnUiThread(List<Preparat> result) {
                if(result != null){
                    List<Preparat> aux = new ArrayList<>(result);
                    for (Preparat preparat:aux) {
                        if(preparat.getTip_preparat().equals("bautura")){
                            bauturameniuList.add(preparat);
                        }else{
                            mancaremeniuList.add(preparat);
                        }
                    }
                }
            }
        };
    }
}