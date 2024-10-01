package com.bigoval.foodfever;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Addaddressmapfragment extends Fragment {
    TextView gpsloc;
    EditText addiaddress;
    LinearLayout searchtoolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View mview = inflater.inflate(R.layout.addcurrentaddressfragment,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
        View lview = inflater1.inflate(R.layout.activity_main,null);
        searchtoolbar = getActivity().findViewById(R.id.searchtoolbar);
//        searchtoolbar.setVisibility(View.GONE);
        addiaddress = mview.findViewById(R.id.addiaddressedittext);
        gpsloc = mview.findViewById(R.id.addressfromlocationtxt);



        return mview;
    }

    @Override
    public void onDestroy() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        super.onDestroy();
    }
}
