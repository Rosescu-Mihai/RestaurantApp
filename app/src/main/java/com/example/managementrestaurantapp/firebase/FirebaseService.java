package com.example.managementrestaurantapp.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.managementrestaurantapp.clase.Angajat;
import com.example.managementrestaurantapp.clase.Comanda;
import com.example.managementrestaurantapp.clase.Factura;
import com.example.managementrestaurantapp.clase.Ingredient;
import com.example.managementrestaurantapp.clase.Masa;
import com.example.managementrestaurantapp.clase.NotadePlata;
import com.example.managementrestaurantapp.clase.Preparat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {

    private final DatabaseReference databaseReferenceAngajat;
    private final DatabaseReference databaseReferenceFactura;
    private final DatabaseReference databaseReferenceMeniu;
    private final DatabaseReference databaseReferenceCamara;
    private final DatabaseReference databaseReferenceComenzi;
    private static FirebaseService firebaseService;

    private FirebaseService(){
        databaseReferenceAngajat = FirebaseDatabase.getInstance().getReference("angajati");
        databaseReferenceFactura = FirebaseDatabase.getInstance().getReference("facturi");
        databaseReferenceMeniu = FirebaseDatabase.getInstance().getReference("meniu");
        databaseReferenceCamara = FirebaseDatabase.getInstance().getReference("camara");
        databaseReferenceComenzi = FirebaseDatabase.getInstance().getReference("comenzi");
    }

    public static FirebaseService getInstance(){
        if(firebaseService == null){
            synchronized (FirebaseService.class){
                if(firebaseService == null){
                    firebaseService = new FirebaseService();
                }
            }
        }
        return firebaseService;
    }

    public void upsert(Angajat angajat){
        if(angajat == null){
            return;
        }
        if(angajat.getId() == null || angajat.getId().trim().isEmpty()){
            String id = databaseReferenceAngajat.push().getKey();
            angajat.setId(id);
        }
        databaseReferenceAngajat.child(angajat.getId()).setValue(angajat);
    }

    public void deleteAngajat(Angajat angajat){
        if(angajat == null || angajat.getId() == null || angajat.getId().trim().isEmpty()){
            return;
        }
        databaseReferenceAngajat.child(angajat.getId()).removeValue();
    }

    public void deleteMasa(Masa masa, Angajat angajat){
        if(masa == null || masa.getIdMasa() == null || masa.getIdMasa().trim().isEmpty()){
            return;
        }
        databaseReferenceAngajat.child(angajat.getId()).child("mese").child(masa.getIdMasa()).removeValue();
    }

    public void attachDataChangeListner(Callback<List<Angajat>> callback){
        databaseReferenceAngajat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<Angajat> angajati = new ArrayList<>();
                for (DataSnapshot data: snapshot.getChildren()){
                    Angajat angajat = data.getValue(Angajat.class);
                    if(angajat != null){
                        angajati.add(angajat);
                    }
                }
                callback.runResultOnUiThread(angajati);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.i("Firebase: ", "SELECT don't work!");
            }
        });
    }

    public void upsertMasa(Masa masa, Angajat angajat){
        if(masa == null){
            return;
        }
        if(masa.getIdMasa() == null || masa.getIdMasa().trim().isEmpty()){
            String id = databaseReferenceAngajat.child(angajat.getId()).child("mese").push().getKey();
            masa.setIdMasa(id);
        }
        databaseReferenceAngajat.child(angajat.getId()).child("mese").child(masa.getIdMasa()).setValue(masa);
    }

    public void upsertNotadePlata(NotadePlata notadePlata, Masa masa, Angajat angajat){
        if(notadePlata == null){
            return;
        }
        if(notadePlata.getIdNota() == null || notadePlata.getIdNota().trim().isEmpty()){
            String id = databaseReferenceAngajat.child(angajat.getId()).child(masa.getIdMasa()).child("notadePlata").push().getKey();
            notadePlata.setIdNota(id);
        }
        databaseReferenceAngajat.child(angajat.getId()).child("mese").child(masa.getIdMasa()).child("notadePlata").setValue(notadePlata);
    }

    public void upsertPreparatNota(List<Preparat> preparat, NotadePlata notadePlata, Masa masa, Angajat angajat){
        if(preparat == null){
            return;
        }
        databaseReferenceAngajat.child(angajat.getId()).child("mese").child(masa.getIdMasa()).child("notadePlata").child("preparateNotadePlata").setValue(preparat);
    }

    public void upsertComanda(Comanda comanda){
        if(comanda == null){
            return;
        }
        if(comanda.getIdComanda() == null || comanda.getIdComanda().trim().isEmpty()){
            String id = databaseReferenceComenzi.push().getKey();
            comanda.setIdComanda(id);
        }
        databaseReferenceComenzi.child(comanda.getIdComanda()).setValue(comanda);
    }

    public void attachAllComenzi(Callback<List<Comanda>> comandaCallback){
        databaseReferenceComenzi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comanda> comandaList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Comanda comanda = dataSnapshot.getValue(Comanda.class);
                    if(comanda != null){
                        comandaList.add(comanda);
                    }
                }

                comandaCallback.runResultOnUiThread(comandaList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Firebase: ", "SELECT don't work!");
            }
        });
    }

    public void attachAllIngrediente(Callback<List<Ingredient>> listCallback){
        databaseReferenceCamara.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Ingredient> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Ingredient ingredient = dataSnapshot.getValue(Ingredient.class);
                    if(ingredient != null){
                        list.add(ingredient);
                    }
                }
                listCallback.runResultOnUiThread(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Firebase: ", "SELECT don't work!");
            }
        });
    }

    public void upsertIngredient(Ingredient ingredient){
        if(ingredient == null){
            return;
        }
        if(ingredient.getIdPreparat() == null || ingredient.getIdPreparat().trim().isEmpty() ){
            String id = databaseReferenceCamara.push().getKey();
            ingredient.setIdPreparat(id);
        }
        databaseReferenceCamara.child(ingredient.getIdPreparat()).setValue(ingredient);
    }

    public void attachAllFacturi(Callback<List<Factura>> callbackFacturi){
        databaseReferenceFactura.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Factura> facturaList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Factura factura = dataSnapshot.getValue(Factura.class);
                    if(factura != null){
                        facturaList.add(factura);
                    }
                }
                callbackFacturi.runResultOnUiThread(facturaList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Firebase: ", "SELECT don't work!");
            }
        });
    }

    public void upsertFactura(Factura factura){
        if(factura == null){
            return;
        }
        if(factura.getIdFactura() == null || factura.getIdFactura().trim().isEmpty()){
            String id = databaseReferenceFactura.push().getKey();
            factura.setIdFactura(id);
        }
        databaseReferenceFactura.child(factura.getIdFactura()).setValue(factura);
    }

    public void upsertPreparatMeniu(Preparat preparat){
        if(preparat == null){
            return;
        }
        if(preparat.getId_preparat() == null || preparat.getId_preparat().trim().isEmpty()){
            String id = databaseReferenceMeniu.push().getKey();
            preparat.setId_preparat(id);
        }
        databaseReferenceMeniu.child(preparat.getId_preparat()).setValue(preparat);
    }

    public void attachPreparateMeniu(Callback<List<Preparat>> callbackPreparat){
        databaseReferenceMeniu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Preparat> preparats = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Preparat preparat = dataSnapshot.getValue(Preparat.class);
                    if(preparat != null){
                        preparats.add(preparat);
                    }
                }
                callbackPreparat.runResultOnUiThread(preparats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Firebase: ", "SELECT don't work!");
            }
        });
    }

}
