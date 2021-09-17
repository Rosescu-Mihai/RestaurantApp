package com.example.managementrestaurantapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.managementrestaurantapp.clase.Angajat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MeniuOperatiuniManagerActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    TextView textView;
    Button btn_ang, btn_comanda_manager, btn_incasari, btn_facturi, btn_preparat_meniu;
    Angajat user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meniu_operatiuni_manager);
        inititalizareComponente();
    }

    @SuppressLint("SetTextI18n")
    private void inititalizareComponente() {
        textView=findViewById(R.id.nume_user);
        Intent intent =getIntent();
        if(intent.hasExtra("user_log")){
            user = (Angajat) intent.getParcelableExtra("user_log");
            textView.setText("Welcome "+user.getPrenume()+"!");
        }else{
            textView.setText("Welcome user!");
        }

        btn_comanda_manager=findViewById(R.id.comanda_manager);
        btn_ang=findViewById(R.id.btn_ireg_ang);
        btn_incasari=findViewById(R.id.btn_incasari);
        btn_facturi=findViewById(R.id.btn_facturi);
        btn_preparat_meniu=findViewById(R.id.btn_meniu_preparat);
        btn_ang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MeniuOperatiuniManagerActivity.this, ListaAngajatiActivity.class);
                intent1.putExtra("user", user);
                startActivity(intent1);
            }
        });
        lottieAnimationView=findViewById(R.id.donut_anim);
        lottieAnimationView.animate().translationXBy(1400).setDuration(1000).setStartDelay(4000);
        btn_comanda_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), HartaRestaurantActivity.class);
                intent1.putExtra("angajatCheie", user);
                startActivity(intent1);
            }
        });
        btn_incasari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentIncasari = new Intent(getApplicationContext(), IncasariActivity.class);
                startActivity(intentIncasari);
            }
        });
        btn_facturi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inentFacturi = new Intent(getApplicationContext(), FacturaListActivity.class);
                startActivity(inentFacturi);
            }
        });
        btn_preparat_meniu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPreparat = new Intent(getApplicationContext(), IntroducerePreparatActivity.class);
                startActivity(intentPreparat);
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MeniuOperatiuniManagerActivity.this);
        builder.setIcon(R.drawable.ic_baseline_vpn_key_24);
        builder.setTitle("Doriti sa va deconectati?");
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
                MeniuOperatiuniManagerActivity.super.onBackPressed();
                Intent intent = new Intent(MeniuOperatiuniManagerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}