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

public class MeniuOperatiuniAngajatActivity extends AppCompatActivity {
   private LottieAnimationView lottieAnimationView;
   private FloatingActionButton floatingActionButton;
   private Button btn_comanda, btn_factura, btn_comenzi_barman, btn_comenzi_bucatar, btn_comenzi_efectuate;
   private TextView textView;
   private Angajat user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meniu_operatiuni_angajat);
        initializareComponente();
    }

    @SuppressLint("SetTextI18n")
    private void initializareComponente() {
        textView=findViewById(R.id.nume_user);
        btn_comenzi_bucatar=findViewById(R.id.comnada_bucatar);
        btn_comenzi_efectuate=findViewById(R.id.comnada_ospatar);
        btn_comenzi_barman=findViewById(R.id.comanda_barman);
        btn_comanda=findViewById(R.id.comanda);
        btn_factura=findViewById(R.id.factura);
        Intent intent =getIntent();
        if(intent.hasExtra("user_log")){
            user = intent.getParcelableExtra("user_log");
            textView.setText("Welcome "+user.getPrenume()+"!");
            if(user.getFunctie().equals("barman")){
                btn_comenzi_efectuate.setVisibility(View.GONE);
                btn_comenzi_bucatar.setVisibility(View.GONE);
            }else {
                if(user.getFunctie().equals("bucatar")){
                    btn_comenzi_efectuate.setVisibility(View.GONE);
                    btn_comenzi_barman.setVisibility(View.GONE);
                    btn_comanda.setVisibility(View.GONE);
                }else{
                    if(user.getFunctie().equals("ospatar")){
                        btn_comenzi_bucatar.setVisibility(View.GONE);
                        btn_comenzi_barman.setVisibility(View.GONE);
                    }
                }
            }
        }else{
            textView.setText("Welcome user!");
        }

        lottieAnimationView=findViewById(R.id.donut_anim);
        lottieAnimationView.animate().translationXBy(1400).setDuration(1000).setStartDelay(4000);
        floatingActionButton=findViewById(R.id.decon_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MeniuOperatiuniAngajatActivity.this);
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
                        Intent intent = new Intent(MeniuOperatiuniAngajatActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        btn_comanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), HartaRestaurantActivity.class);
                intent1.putExtra("angajatCheie", user);
                startActivity(intent1);
            }
        });
        btn_comenzi_barman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), ComenzidePreparatActivity.class);
                intent2.putExtra("angajatUser", user);
                startActivity(intent2);
            }
        });
        btn_comenzi_bucatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), ComenzidePreparatActivity.class);
                intent2.putExtra("angajatUser", user);
                startActivity(intent2);
            }
        });
        btn_comenzi_efectuate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), ComenzidePreparatActivity.class);
                intent3.putExtra("angajatUser", user);
                startActivity(intent3);
            }
        });
        btn_factura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(getApplicationContext(), FacturaActivity.class);
                startActivity(intent4);
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MeniuOperatiuniAngajatActivity.this);
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
                MeniuOperatiuniAngajatActivity.super.onBackPressed();
                Intent intent = new Intent(MeniuOperatiuniAngajatActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}