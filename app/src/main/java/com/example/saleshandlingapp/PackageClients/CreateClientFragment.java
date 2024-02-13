package com.example.saleshandlingapp.PackageClients;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saleshandlingapp.Database.Client;
import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateClientFragment extends Fragment {

    private String phoneNumber;
    private EditText phoneNumberEditText, nameEditText, surnameEditText, cityEditText, addressEditText;
    private Button addClient;
    private String name, surname, city, address;

    public CreateClientFragment() {
        // Required empty public constructor
    }

    public static CreateClientFragment newInstance(String number) {
        CreateClientFragment fragment = new CreateClientFragment();
        Bundle args = new Bundle();
        args.putString("number", number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString("number");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_client, container, false);
        phoneNumberEditText = view.findViewById(R.id.phoneNumber);
        phoneNumberEditText.setText(phoneNumber);

        nameEditText = view.findViewById(R.id.name);
        surnameEditText = view.findViewById(R.id.surname);
        cityEditText = view.findViewById(R.id.city);
        addressEditText = view.findViewById(R.id.address);
        addClient = view.findViewById(R.id.addClient);

        addClient.setOnClickListener(v -> {
            if(!validateData()){
                Toast.makeText(getContext(),"Uzupełnij wszystkie pola...",Toast.LENGTH_LONG).show();
            }
            else{
                Database.getInstance(getContext()).clientDao().insertAll(new Client(name,surname,phoneNumber,city,address));
                Toast.makeText(getContext(),"Dodano nowego klienta!",Toast.LENGTH_LONG).show();
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private boolean validateData() {
        name = nameEditText.getText().toString();
        surname = surnameEditText.getText().toString();
        city = cityEditText.getText().toString();
        address = addressEditText.getText().toString();
        if (name.equals("") || surname.equals("") || city.equals("") || address.equals("")){
            return false;
        }
        return true;
    }
}