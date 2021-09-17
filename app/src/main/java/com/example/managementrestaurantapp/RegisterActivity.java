package com.example.managementrestaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.managementrestaurantapp.clase.Angajat;
import com.example.managementrestaurantapp.clase.Masa;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    Button btn_inreg;
    EditText nume, prenume, cod, parola, email;
    Spinner functie;

    Angajat angajat;
    List<Angajat> angajatList;
    Boolean upadteAngajat;

    FirebaseAuth firebaseAuth;
    FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComp();

        btn_inreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailContext = email.getText().toString();
                String passwordContext = parola.getText().toString();
                String numeContext = nume.getText().toString();
                String prenumeContext = prenume.getText().toString();
                String codContext = cod.getText().toString();

                if(emailContext.isEmpty() || passwordContext.isEmpty() || numeContext.isEmpty()
                || prenumeContext.isEmpty() || codContext.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Completarea tuturor spatiilor este obligatorie!", Toast.LENGTH_LONG).show();
                }else {
                    if(!upadteAngajat){
                        boolean NotOk = false;
                        for (Angajat angajat:angajatList) {
                            if(Integer.parseInt(codContext) == angajat.getUsername()){
                                NotOk = true;
                                break;
                            }
                        }

                        if(!NotOk){
                            firebaseAuth.createUserWithEmailAndPassword(emailContext, passwordContext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        creareAngajat(numeContext, prenumeContext, Integer.parseInt(codContext),
                                                passwordContext, functie.getSelectedItem().toString(), emailContext);
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.putExtra("angajatNou",angajat);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Error! "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                        Log.i("Firebase", "Error! "+ Objects.requireNonNull(task.getException()).getMessage());
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(getApplicationContext(), "Codul este folosit de un alt angajat!", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        creareAngajat(numeContext, prenumeContext, Integer.parseInt(codContext),
                                passwordContext, functie.getSelectedItem().toString(), emailContext);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("angajatNou",angajat);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }
            }
        });
    }

    private void creareAngajat(String numeContext, String prenumeContext, int parseInt, String parola, String toString, String emailContext) {
        angajat.setNume(numeContext);
        angajat.setPrenume(prenumeContext);
        angajat.setUsername(parseInt);
        angajat.setPassword(parola);
        angajat.setFunctie(toString);
        angajat.setEmail(emailContext);
        angajat.setMese();
    }

    private void populareSpatii(){
        nume.setText(angajat.getNume());
        prenume.setText(angajat.getPrenume());
        ArrayAdapter arrayAdapter= (ArrayAdapter) functie.getAdapter();
        for( int i =0; i<arrayAdapter.getCount(); ++i)
        {
            String item = (String) arrayAdapter.getItem(i);
            if(item != null && item.equals(angajat.getFunctie()))
            {
                functie.setSelection(i);
            }
        }
        cod.setText(String.valueOf(angajat.getUsername()));
        parola.setText(angajat.getPassword());
        email.setText(angajat.getEmail());
    }

    private void initComp() {
        angajatList = new ArrayList<>();
        btn_inreg=findViewById(R.id.inregistrare_btn);
        nume=findViewById(R.id.nume_case);
        prenume=findViewById(R.id.prenume_case);
        functie=findViewById(R.id.spin_functie);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.functii, android.R.layout.simple_spinner_dropdown_item);
        functie.setAdapter(adapter);
        cod=findViewById(R.id.cod_case);
        parola=findViewById(R.id.parola_case);
        email=findViewById(R.id.email_case);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseService = FirebaseService.getInstance();
        firebaseService.attachDataChangeListner(getAngajati());
        Intent intent = getIntent();
        if(intent.hasExtra("angajatKey")){
            angajat = (Angajat) intent.getParcelableExtra("angajatKey");
            upadteAngajat = true;
            populareSpatii();
        }else {
            upadteAngajat = false;
            angajat = new Angajat();
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