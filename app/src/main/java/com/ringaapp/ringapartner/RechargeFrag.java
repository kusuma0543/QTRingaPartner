package com.ringaapp.ringapartner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RechargeFrag extends Fragment {

    public static RechargeFrag newInstance() {
        RechargeFrag fragment= new RechargeFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recharge, container, false);
    }

}
