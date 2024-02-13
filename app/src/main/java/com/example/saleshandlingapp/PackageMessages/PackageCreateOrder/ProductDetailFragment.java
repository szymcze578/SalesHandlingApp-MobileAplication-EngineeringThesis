package com.example.saleshandlingapp.PackageMessages.PackageCreateOrder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {

    private Product product;
    private double value;
    private ImageView image;
    private TextView productName;
    private TextView productAmountInWarehouse;

    private TextView productsValue;

    private Button incrementButton;
    private Button decrementButton;
    private Button addToCart;

    private int quantityNumber = 0;
    private TextView selectedAmount;

    private int itemID;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    public static ProductDetailFragment newInstance(Product prod) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("item_id",prod);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable("item_id");
            //product = Database.getInstance(getContext()).productDao().getProductByID(itemID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = view.findViewById(R.id.ProductDetailImage);
        productName = view.findViewById(R.id.productName);
        productAmountInWarehouse = view.findViewById(R.id.availability);
        selectedAmount = view.findViewById(R.id.quantityNumber);
        incrementButton = view.findViewById(R.id.incrementproducts);
        decrementButton = view.findViewById(R.id.decrementProduct);
        addToCart = view.findViewById(R.id.addToCart);
        productsValue = view.findViewById(R.id.orderValue);

        image.setImageResource(getImageID(product.getImageName()));
        productName.setText(product.getProductName());
        productAmountInWarehouse.setText("Sztuk na magazynie: " + product.getAmount());
        productsValue.setText(productsValue.getText() + "0.0 zł");

        incrementButton.setOnClickListener(v -> {
            if(quantityNumber<product.getAmount()){
                quantityNumber++;
                selectedAmount.setText(String.valueOf(quantityNumber));
                value = product.getPrice()*quantityNumber;
                productsValue.setText("Wartość produktów: " +value + "zł");
            }
        });

        decrementButton.setOnClickListener(v -> {
            if(quantityNumber!=0){
                quantityNumber--;
                selectedAmount.setText(String.valueOf(quantityNumber));
                value = product.getPrice()*quantityNumber;
                productsValue.setText("Wartość produktów: " + value + "zł");
            }
        });

        addToCart.setOnClickListener(v -> {
            Bundle result = new Bundle();
            Product order = Database.getInstance(getContext()).productDao().getProductByID(product.getID());
            order.setAmount(quantityNumber);
            result.putParcelable("orderToAdd", order);
            getParentFragmentManager().setFragmentResult("resultKey", result);
            getParentFragmentManager().popBackStack();
        });
    }

    private int getImageID(String filename){
        return getContext().getResources().getIdentifier(filename,"drawable",getContext().getPackageName());
    }

}