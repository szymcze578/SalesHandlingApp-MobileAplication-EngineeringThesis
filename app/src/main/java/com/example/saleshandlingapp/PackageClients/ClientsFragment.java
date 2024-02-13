package com.example.saleshandlingapp.PackageClients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saleshandlingapp.Listeners.RecyclerViewClickListener;
import com.example.saleshandlingapp.Database.Client;
import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.R;
import com.example.saleshandlingapp.SMSServices.SMSSender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Client> clients;
    private RecyclerView recyclerView;
    private FloatingActionButton advertise;

    private RecyclerViewClickListener listener;

    public ClientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientsFragment newInstance(String param1, String param2) {
        ClientsFragment fragment = new ClientsFragment();
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

    private void setAdapter(){
        setOnClickListener();
        ClientsListRecyclerAdapter adapter = new ClientsListRecyclerAdapter(clients,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener(){
        listener = (v, position) -> getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_exit_anim)
                .replace(R.id.frame, ChangeClientDataFragment.newInstance(position+1))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_clients, container, false);
       recyclerView = view.findViewById(R.id.clientsRecyclerAdapter);
       advertise = view.findViewById(R.id.advertise);
       clients = Database.getInstance(getContext()).clientDao().getAllClients();
       setAdapter();

       advertise.setOnClickListener(v -> {

           for(Client client : clients){
               SMSSender.sendAdd(client.getPhoneNumber(),prepareMessage());
           }
       });
       return view;
    }


    private String prepareMessage(){
        StringBuilder builder = new StringBuilder();
        List<Product> products = Database.getInstance(getContext()).productDao().getAll();
        builder.append("REKLAMA SKLEPU - produkty dostępne na magazynie:\n");

        for (Product product : products){
            builder.append(product.getProductName());
            builder.append(" ");
            builder.append(product.getPrice());
            builder.append(" zł, ilość: ");
            builder.append(product.getAmount());
            builder.append("\n");
        }
        return builder.toString();
    }
}