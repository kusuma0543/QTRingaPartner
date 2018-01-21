package com.ringaapp.ringapartner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SellProdutsFrag extends Fragment {


    public static SellProdutsFrag newInstance() {
        SellProdutsFrag fragment= new SellProdutsFrag();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell_produts, container, false);
    }

}
