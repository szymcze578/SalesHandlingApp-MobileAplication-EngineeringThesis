package com.example.saleshandlingapp.PackageClients;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

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
 * Use the {@link ChangeClientDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeClientDataFragment extends Fragment {

    private Client client;

    private EditText phoneNumber, name, surname, city, address;
    private Button changeData;
    public ChangeClientDataFragment() {
        // Required empty public constructor
    }


    public static ChangeClientDataFragment newInstance(int clientID) {
        ChangeClientDataFragment fragment = new ChangeClientDataFragment();
        Bundle args = new Bundle();
        args.putInt("ClientID", clientID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int clientID = getArguments().getInt("ClientID");
            client = Database.getInstance(getContext()).clientDao().getClientByID(clientID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_client_data, container, false);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname);
        city = view.findViewById(R.id.city);
        address = view.findViewById(R.id.address);
        changeData = view.findViewById(R.id.changeClientData);

        phoneNumber.setText(client.getPhoneNumber());
        name.setText(client.getName());
        surname.setText(client.getSurname());
        city.setText(client.getCity());
        address.setText(client.getAddress());

        changeData.setOnClickListener(v -> changeClientData());
        return view;
    }

    public void changeClientData(){
        String newPhoneNumber = phoneNumber.getText().toString();
        String newName = name.getText().toString();
        String newSurname = surname.getText().toString();
        String newCity = city.getText().toString();
        String newAddress = address.getText().toString();

        if(newPhoneNumber.equals(client.getPhoneNumber()) && newName.equals(client.getName())
            && newSurname.equals(client.getSurname()) && newCity.equals(client.getCity()) && newAddress.equals(client.getAddress())){
            Toast.makeText(getContext(),"Nie wprowadzono żadnych zmian!",Toast.LENGTH_LONG).show();
            return;
        }

        if(!client.getPhoneNumber().equals(newPhoneNumber))
            client.setPhoneNumber(newPhoneNumber);
        if(!client.getName().equals(newName))
            client.setName(newName);
        if(!client.getSurname().equals(newSurname))
            client.setSurname(newSurname);
        if(!client.getCity().equals(newCity))
            client.setCity(newCity);
        if(!client.getAddress().equals(newAddress))
            client.setAddress(newAddress);

        Database.getInstance(getContext()).clientDao().updateClient(client);
        Toast.makeText(getContext(),"Zmieniono dane klienta!",Toast.LENGTH_LONG).show();
        getParentFragmentManager().popBackStack();
    }

}