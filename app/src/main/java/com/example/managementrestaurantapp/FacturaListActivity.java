package com.example.managementrestaurantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managementrestaurantapp.adapter.FacturaAdapter;
import com.example.managementrestaurantapp.adapter.NotadePlataAdapter;
import com.example.managementrestaurantapp.clase.Factura;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FacturaListActivity extends AppCompatActivity implements FacturaAdapter.OnNotaListner{

    private RecyclerView facturiRecycleViewer;
    private EditText dataEditText;
    private TextView totalFactura;

    private FirebaseService firebaseService;

    private List<Factura> facturaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_list);
        creeazaComp();
    }

    private void creeazaComp() {
        firebaseService = FirebaseService.getInstance();
        facturiRecycleViewer = findViewById(R.id.lista_facturi);
        dataEditText = findViewById(R.id.data_inscrisa_factura);
        FloatingActionButton dataIntrodusa = findViewById(R.id.btn_cauta);
        totalFactura = findViewById(R.id.suma_Facturi);
        dataIntrodusa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    firebaseService.attachAllFacturi(getFacturi());
            }
        });

    }

    private void afisareFacturi(){
        facturiRecycleViewer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new FacturaAdapter(facturaList, this);
        facturiRecycleViewer.setLayoutManager(layoutManager);
        facturiRecycleViewer.setAdapter(adapter);
    }

    private Callback<List<Factura>> getFacturi(){
        return new Callback<List<Factura>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void runResultOnUiThread(List<Factura> result) {
                if(result != null){
                    int totalFacturi = 0;
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    dateFormat.setLenient(false);

                    try {
                        dateFormat.parse(dataEditText.getText().toString());
                        facturaList = new ArrayList<>();
                        for (Factura factura :result) {
                            if(factura.getDataFactura().equals(dataEditText.getText().toString())){
                                facturaList.add(factura);
                                totalFacturi = totalFacturi+factura.getTotalFactura();
                            }
                        }
                        if(facturaList.size() == 0){
                            Toast.makeText(getApplicationContext(), "Nu s-au incarcat facturi in sistem la aceasta data.", Toast.LENGTH_LONG).show();
                        }else {
                            afisareFacturi();
                            totalFactura.setText("Suma facturi: "+String.valueOf(totalFacturi));
                        }
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Data nu are formatul corespunzator!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

//    private void notifyAdapter(){
//        FacturaAdapter adapter = (FacturaAdapter) facturiRecycleViewer.getAdapter();
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onClickNota(int position) {
        Intent intent = new Intent(getApplicationContext(), FacturaActivity.class);
        intent.putExtra("facturaVizualizata", facturaList.get(position));
        startActivity(intent);
    }
}