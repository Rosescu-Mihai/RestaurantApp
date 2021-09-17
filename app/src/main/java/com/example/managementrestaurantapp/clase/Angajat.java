package com.example.managementrestaurantapp.clase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Angajat implements Parcelable {

    private String id;
    private String nume;
    private String prenume;
    private int username;
    private String password;
    private String functie;
    private String email;
    private List<Masa> mese;

    public Angajat(){

    }

    public Angajat(String id, String nume, String prenume, int username, String password, String functie, String email, List<Masa> mese) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.username = username;
        this.password = password;
        this.functie = functie;
        this.email = email;
        this.mese = mese;
    }

    protected Angajat(Parcel in) {
        id = in.readString();
        nume = in.readString();
        prenume = in.readString();
        username = in.readInt();
        password = in.readString();
        functie = in.readString();
        email = in.readString();
        mese = in.createTypedArrayList(Masa.CREATOR);
    }

    public static final Creator<Angajat> CREATOR = new Creator<Angajat>() {
        @Override
        public Angajat createFromParcel(Parcel in) {
            return new Angajat(in);
        }

        @Override
        public Angajat[] newArray(int size) {
            return new Angajat[size];
        }
    };

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNume() { return nume; }

    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }

    public void setPrenume(String prenume) { this.prenume = prenume; }

    public int getUsername() { return username; }

    public void setUsername(int username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getFunctie() { return functie; }

    public void setFunctie(String functie) { this.functie = functie; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public void setMese(){ this.mese = new ArrayList<>(); }

    public List<Masa> getMese() { return mese; }

    public void setMese(Map<String, Masa> masaMap) {
        this.mese = new ArrayList<>();
        this.mese.addAll(masaMap.values());
    }

    public void adaugaMasa(Masa masa){
        mese.add(masa);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nume);
        dest.writeString(prenume);
        dest.writeInt(username);
        dest.writeString(password);
        dest.writeString(functie);
        dest.writeString(email);
        dest.writeTypedList(mese);
    }
}
