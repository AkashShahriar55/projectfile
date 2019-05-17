package com.example.bimvendpro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SettingsFragment extends Fragment {

    private CardView addProductType, addMachineType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        addMachineType = view.findViewById(R.id.addMachineType);
        addProductType = view.findViewById(R.id.addProductType);

        addMachineType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSpinnerTypeActivity.class);
                intent.putExtra("type", AddSpinnerTypeActivity.MACHINE);
                getContext().startActivity(intent);
            }
        });

        addProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSpinnerTypeActivity.class);
                intent.putExtra("type", AddSpinnerTypeActivity.PRODUCT);
                getContext().startActivity(intent);
            }
        });
        hideKeyboard();

        super.onViewCreated(view, savedInstanceState);
    }

    private void hideKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }


    }
}
