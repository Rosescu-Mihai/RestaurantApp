package com.example.managementrestaurantapp.clase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Factura implements Parcelable {

    private String idFactura;
    private String dataFactura;
    private String numeFurnizor;
    private List<Ingredient> ingredientList;
    private int totalFactura;

    public Factura() {
    }

    protected Factura(Parcel in) {
        idFactura = in.readString();
        dataFactura = in.readString();
        numeFurnizor = in.readString();
        ingredientList = in.createTypedArrayList(Ingredient.CREATOR);
        totalFactura = in.readInt();
    }

    public static final Creator<Factura> CREATOR = new Creator<Factura>() {
        @Override
        public Factura createFromParcel(Parcel in) {
            return new Factura(in);
        }

        @Override
        public Factura[] newArray(int size) {
            return new Factura[size];
        }
    };

    public int getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(int totalFactura) {
        this.totalFactura = totalFactura;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getDataFactura() {
        return dataFactura;
    }

    public void setDataFactura(String dataFactura) {
        this.dataFactura = dataFactura;
    }

    public String getNumeFurnizor() {
        return numeFurnizor;
    }

    public void setNumeFurnizor(String numeFurnizor) {
        this.numeFurnizor = numeFurnizor;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idFactura);
        dest.writeString(dataFactura);
        dest.writeString(numeFurnizor);
        dest.writeTypedList(ingredientList);
        dest.writeInt(totalFactura);
    }
}
