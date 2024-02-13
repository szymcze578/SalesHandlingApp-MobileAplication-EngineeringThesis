package com.example.saleshandlingapp.PackageMessages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saleshandlingapp.Listeners.RecyclerViewClickListener;
import com.example.saleshandlingapp.PackageMessages.PackageCreateOrder.CreateOrderFragment;
import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Message;
import com.example.saleshandlingapp.R;
import com.example.saleshandlingapp.SMSServices.SMSReceiverListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment implements SMSReceiverListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Message> messages;
    private RecyclerView recyclerView;
    private RecyclerViewClickListener listener;



    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
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
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerView = view.findViewById(R.id.MessagesRecyclerAdapter);
        messages = Database.getInstance(getContext().getApplicationContext()).messageDao().getAllMesses();
        setAdapter();
        return view;
    }

    private void setAdapter(){
        setOnClickListener();
        MessagesRecyclerAdapter adapter = new MessagesRecyclerAdapter(messages,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = (V, position) ->{
                    getParentFragmentManager()
                    .beginTransaction()
                            .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim)
                    .replace(R.id.frame, CreateOrderFragment.newInstance(messages.get(position).ID))
                            .addToBackStack(null)
                    .commit();
        };
    }
    @Override
    public void onSMSReceived() {
        if(this.isResumed()) {
            refreshList();
        }
    }

    private void refreshList(){
        messages = Database.getInstance(getContext().getApplicationContext()).messageDao().getAllMesses();
        setAdapter();
    }
}