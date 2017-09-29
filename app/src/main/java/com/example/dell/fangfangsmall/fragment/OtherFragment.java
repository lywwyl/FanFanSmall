package com.example.dell.fangfangsmall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.fangfangsmall.R;


/**
 * Created by lyw on 17/9/22.
 */
public class OtherFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        return  inflater.inflate(R.layout.fragment_other,container,false);
    }
}
