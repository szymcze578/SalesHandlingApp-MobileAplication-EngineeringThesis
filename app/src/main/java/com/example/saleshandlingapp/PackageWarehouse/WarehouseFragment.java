package com.example.saleshandlingapp.PackageWarehouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WarehouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WarehouseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Product> products;
    private RecyclerView recyclerView;

    public WarehouseFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_warehouse, container, false);
        recyclerView = view.findViewById(R.id.WarehouseRecyclerAdapter);
        products = Database.getInstance(getContext().getApplicationContext()).productDao().getAll();
        setAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshFragment();
    }

    private void setAdapter(){
        WarehouseRecyclerAdapter adapter = new WarehouseRecyclerAdapter(products, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void refreshFragment(){
        products = Database.getInstance(getContext().getApplicationContext()).productDao().getAll();
        setAdapter();
    }
}