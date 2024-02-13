package com.example.saleshandlingapp.PackageWarehouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeItemPropertiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeItemPropertiesFragment extends Fragment {

    private Product product;

    private ImageView image;
    private TextView name;
    private EditText price;
    private EditText amount;

    private Button confirmButton;

    public ChangeItemPropertiesFragment() {
        // Required empty public constructor
    }

    public static ChangeItemPropertiesFragment newInstance(int productID) {
        ChangeItemPropertiesFragment fragment = new ChangeItemPropertiesFragment();
        Bundle args = new Bundle();
        args.putInt("productID", productID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = Database.getInstance(getContext())
                    .productDao()
                    .getProductByID(getArguments()
                            .getInt("productID"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_item_properties, container, false);
        image = view.findViewById(R.id.ProductDetailImage);
        name = view.findViewById(R.id.productName);
        price = view.findViewById(R.id.price);
        amount = view.findViewById(R.id.warehouseAmount);
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
        confirmButton = view.findViewById(R.id.confirmChanges);
        int id = getResources()
                .getIdentifier(product.getImageName(),"drawable",getContext().getPackageName());
        image.setImageResource(id);
        name.setText(product.getProductName());
        price.setText(Double.toString(product.getPrice()));
        amount.setText(Integer.toString(product.getAmount()));
        confirmButton.setOnClickListener(v -> changeProperties());
        return view;
    }

    public void changeProperties(){
        try{
            int newAmount = Integer.parseInt(amount.getText().toString());
            double newPrice = Double.parseDouble(price.getText().toString());

            if(newAmount == product.getAmount() && newPrice == product.getPrice()){
                Toast.makeText(getContext(),"Nie wprowadzono żadnych zmian!",Toast.LENGTH_LONG).show();
            }
            else {
                Database.getInstance(getContext()).productDao().updateProduct(newAmount, newPrice, product.getID());
                Toast.makeText(getContext(), "Zaktualizowano szczegóły produktu.", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();

            }
        }catch (NumberFormatException e){
            Toast.makeText(getContext(),"Wprowadzono nieprawidłowe dane!",Toast.LENGTH_LONG).show();
        }
    }

}