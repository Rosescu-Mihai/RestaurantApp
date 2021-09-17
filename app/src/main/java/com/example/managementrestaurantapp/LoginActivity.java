package com.example.managementrestaurantapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.managementrestaurantapp.clase.Angajat;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final int ADD_ANGAJAT = 200;
    EditText editTextUser, editTextPassword;
    Button logBtn, regBtn;

    List<Angajat> angajatList;

    FirebaseService firebaseService;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComp();
    }

    private void initComp() {
        angajatList = new ArrayList<>();
        editTextUser=findViewById(R.id.user_case);
        editTextPassword=findViewById(R.id.password_case);
        logBtn=findViewById(R.id.login_btn);
        regBtn=findViewById(R.id.register_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseService = FirebaseService.getInstance();
        firebaseService.attachDataChangeListner(getAngajati());
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent , ADD_ANGAJAT);
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextPassword.getText().toString().isEmpty() || editTextUser.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Completarea tuturor spatiilor este obligatorie!", Toast.LENGTH_LONG).show();
                }else {
                    int cod = Integer.parseInt(editTextUser.getText().toString());
                    String parola = editTextPassword.getText().toString();
                    Angajat angajat = new Angajat();
                    for (Angajat ang:angajatList) {
                        if(ang.getUsername() == cod && ang.getPassword().equals(parola)){
                            angajat.setId(ang.getId());
                            angajat.setEmail(ang.getEmail());
                            angajat.setNume(ang.getNume());
                            angajat.setPrenume(ang.getPrenume());
                            angajat.setUsername(cod);
                            angajat.setPassword(parola);
                            angajat.setFunctie(ang.getFunctie());
                        }
                    }
                    if(angajat.getId() != null){
                        firebaseAuth.signInWithEmailAndPassword(angajat.getEmail(), String.valueOf(angajat.getPassword())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    if(angajat.getFunctie().equals("manager")){
                                        Intent intent = new Intent(getApplicationContext(), MeniuOperatiuniManagerActivity.class);
                                        intent.putExtra("user_log", angajat);
                                        startActivity(intent);
                                    }else {
                                        if(angajat.getFunctie().equals("barman") || angajat.getFunctie().equals("ospatar") || angajat.getFunctie().equals("bucatar")){
                                            Intent intent = new Intent(getApplicationContext(), MeniuOperatiuniAngajatActivity.class);
                                            intent.putExtra("user_log", angajat);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Angajatul nu exista!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_ANGAJAT && resultCode == RESULT_OK && data != null){
            Angajat angajat = (Angajat) data.getParcelableExtra("angajatNou");
            firebaseService.upsert(angajat);
            angajatList.add(angajat);
            editTextPassword.setText(String.valueOf(angajat.getPassword()));
            editTextUser.setText(String.valueOf(angajat.getUsername()));
        }
    }

    private Callback<List<Angajat>> getAngajati(){
        return new Callback<List<Angajat>>() {
            @Override
            public void runResultOnUiThread(List<Angajat> result) {
                if(result != null){
                    angajatList.clear();
                    angajatList.addAll(result);
                }
            }
        };
    }
}