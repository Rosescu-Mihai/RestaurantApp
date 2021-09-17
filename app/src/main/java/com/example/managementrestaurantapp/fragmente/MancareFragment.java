package com.example.managementrestaurantapp.fragmente;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.managementrestaurantapp.MeniuRestaurantActivity;
import com.example.managementrestaurantapp.adapter.MancareAdapter;
import com.example.managementrestaurantapp.clase.Preparat;
import com.example.managementrestaurantapp.R;
import com.example.managementrestaurantapp.firebase.Callback;
import com.example.managementrestaurantapp.firebase.FirebaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MancareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MancareFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mancarelistView;
    private MancareAdapter adapter;

    private  List<Preparat> mancaremeniuList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MancareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MancareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MancareFragment newInstance(String param1, String param2) {
        MancareFragment fragment = new MancareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mancare, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mancaremeniuList = new ArrayList<>();
        Bundle data = getArguments();
        if(data != null){
            mancaremeniuList.addAll(data.getParcelableArrayList("mancareList"));
        }
        mancarelistView = view.findViewById(R.id.lista_mancare_view);
        mancarelistView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mancarelistView.setLayoutManager(layoutManager);
        adapter = new MancareAdapter(mancaremeniuList);
        mancarelistView.setAdapter(adapter);
        creareListaBauturi();
    }

    private void creareListaBauturi() {
        actualizareListaMancare();

        adapter.setOnItemClickListener(new MancareAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "Produs adaugat.", Toast.LENGTH_SHORT).show();
                MeniuRestaurantActivity meniuRestaurantActivity = (MeniuRestaurantActivity) getActivity();
                assert meniuRestaurantActivity != null;
                mancaremeniuList.get(position).setEfectuat(false);
                meniuRestaurantActivity.adaugainNota(mancaremeniuList.get(position));
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void actualizareListaMancare() {
        MancareAdapter adapter1 = (MancareAdapter) mancarelistView.getAdapter();
        assert adapter1 != null;
        adapter1.notifyDataSetChanged();
    }

}