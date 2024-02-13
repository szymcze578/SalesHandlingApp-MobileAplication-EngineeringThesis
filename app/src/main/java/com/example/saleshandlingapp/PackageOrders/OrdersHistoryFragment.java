package com.example.saleshandlingapp.PackageOrders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.OrderWithOrderItemsAndProducts;
import com.example.saleshandlingapp.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersHistoryFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<OrderWithOrderItemsAndProducts> orders;
    private RecyclerView recyclerView;

    public OrdersHistoryFragment() {
        // Required empty public constructor
    }

    public static OrdersHistoryFragment newInstance(String param1, String param2) {
        OrdersHistoryFragment fragment = new OrdersHistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_orders_history, container, false);
        recyclerView = view.findViewById(R.id.ordersHistoryRecyclerAdapter);
        orders = Database.getInstance(getContext()).orderDao().getOrdersHistory();
        setAdapter();
        return view;
    }

    private void setAdapter(){
        OrderHistoryRecyclerAdapter adapter = new OrderHistoryRecyclerAdapter(orders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}